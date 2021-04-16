package shoppinglist;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.MembershipCard;
import com.megacrit.cardcrawl.relics.SmilingMask;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;
import com.megacrit.cardcrawl.shop.StoreRelic;
import com.megacrit.cardcrawl.ui.DialogWord;
import com.megacrit.cardcrawl.vfx.ShopSpeechBubble;
import com.megacrit.cardcrawl.vfx.SpeechTextEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shoppinglist.panelelements.*;
import shoppinglist.panelelements.items.*;
import shoppinglist.patches.StorePotionPurchasePatch;

import java.util.ArrayList;

// The shopping list panel is a panel added to a shop screen to help the player to plan out their shop.
public class ShoppingListPanel extends UIElement {
    public static final Logger logger = LogManager.getLogger(ShoppingListPanel.class.getName());

    public ShopScreen shopScreen;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    private HeaderElement header = new HeaderElement(this, "Item", "Cost");
    // Displays the total price of all items in the list.
    private TotalElement total = new TotalElement(this);
    // Displays the leftover money if the player purchases all items in the list.
    private BalanceElement balance = new BalanceElement(this);
    // The items in the shopping list.
    private ArrayList<ShopItemElement> items = new ArrayList<>();

    private float speechTimer = 5f;

    private float costMultiplier = 1f;

    public ShoppingListPanel() {
        x = 10f;
        y = Settings.HEIGHT * 0.8f;

        width = PanelElement.WIDTH * Settings.scale;
        height = PanelElement.HEIGHT * Settings.scale;

        updateItemPositions();
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        renderBackground(spriteBatch);

        header.renderBackground(spriteBatch);
        items.forEach((element) -> element.renderBackground(spriteBatch));
        total.renderBackground(spriteBatch);
        balance.renderBackground(spriteBatch);

        header.render(spriteBatch);
        items.forEach((element) -> element.render(spriteBatch));
        total.render(spriteBatch);
        balance.render(spriteBatch);

        renderSpeech(spriteBatch);

        // Render all the hitboxes for debugging purposes.
        hitbox.render(spriteBatch);
        dragHitbox.render(spriteBatch);
    }

    private static float borderSize = 6;
    private static float shadowOffset = 8;
    private void renderBackground(SpriteBatch spriteBatch) {
        shapeRenderer.setProjectionMatrix(spriteBatch.getProjectionMatrix());

        spriteBatch.end();

        // Draw the shadow.
        // Enable alpha channel.
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0f, 0f, 0f, 0.3f));
        shapeRenderer.rect(x - borderSize + shadowOffset, y - borderSize - shadowOffset - height,
                width + borderSize * 2, height + borderSize * 2);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(x - borderSize, y - borderSize - height,
                width + borderSize * 2, height + borderSize * 2);
        shapeRenderer.end();

        spriteBatch.begin();
    }

    // The cart can only be dragged from the title bar, so a hitbox is placed there.
    private Hitbox dragHitbox = new Hitbox(0f, 0f, PanelElement.WIDTH * Settings.scale, PanelElement.HEIGHT * Settings.scale);
    private float mouseXOffset, mouseYOffset;
    private boolean mouseDown;

    private void updateDragging() {
        dragHitbox.update();

        // When the mouse is pressed, record the position and mark the mouse as down.
        // A marker is used because sometimes mouse movements are fast, and the mouse moves out of the frame of the
        // hitbox very quickly without the hitbox following up, causing the mouse to lose the hitbox and have to
        // reenter it to keep dragging it.
        if (dragHitbox.hovered && InputHelper.justClickedLeft) {
            mouseXOffset = InputHelper.mX - x;
            mouseYOffset = InputHelper.mY - y;
            mouseDown = true;
        }

        // Then make the panel move according to that offset.
        if (mouseDown) {
            x = InputHelper.mX - mouseXOffset;
            y = InputHelper.mY - mouseYOffset;
        }

        if (!InputHelper.isMouseDown) mouseDown = false;
    }

    private ArrayList<ShopItemElement> toRemove = new ArrayList<>();
    public void addToToRemove(ShopItemElement item) {
        toRemove.add(item);
    }

    @Override
    public void update() {
        super.update();
        header.update();

        items.forEach(ShopItemElement::update);
        items.removeAll(toRemove);
        // Reset the discount if the membership card is removed from the list manually.
        // The price does not need to be changed from removeItem because purchasing the membership card maintains the
        // discount.
        if (toRemove.stream().anyMatch((element) -> element instanceof RelicItemElement
            && ((RelicItemElement) element).relic instanceof MembershipCard)) {
            costMultiplier = 1f;
            updateDiscounts();
        }
        toRemove.clear();

        total.update();
        balance.update();

        speechTimer -= Gdx.graphics.getDeltaTime();
        attemptToShowTooltip();
        attemptToShowQueuedMessages();
        updateSpeech();

        updateDragging();
        updateItemPositions();
    }

    @Override
    public void updateHitboxPosition() {
        super.updateHitboxPosition();

        dragHitbox.x = x;
        dragHitbox.y = y - PanelElement.HEIGHT * Settings.scale;
    }

    // Line up the header, the total, the balance and all the items according to the panel's position.
    private void updateItemPositions() {
        header.setPosition(x, y);
        for (int i = 0; i < items.size(); i++) {
            // Include the title element.
            items.get(i).setPosition(x, y - PanelElement.HEIGHT * Settings.scale * (i + 1));
        }
        total.setPosition(x, y - PanelElement.HEIGHT * Settings.scale * (items.size() + 1));
        balance.setPosition(x, y - PanelElement.HEIGHT * Settings.scale * (items.size() + 2));

        // Include the title element.
        height = (items.size() + 3) * PanelElement.HEIGHT * Settings.scale;
    }

    public int totalCost() {
        return items.stream().map((element) -> element.basePrice).reduce(0, Integer::sum);
    }

    public void addRelic(StoreRelic storeRelic) {
        if (items.stream().anyMatch((item) -> item instanceof RelicItemElement &&
                ((RelicItemElement) item).relic == storeRelic.relic)) {
            return;
        }
        if (storeRelic.price * costMultiplier > AbstractDungeon.player.gold - totalCost()) {
            balance.flash();
            createSpeech(ShopScreen.getCantBuyMsg());
            return;
        }

        // Set the cost multiplier when the membership card is picked.
        if (storeRelic.relic instanceof MembershipCard) {
            costMultiplier = 0.5f;
            updateDiscounts();
        }

        addItemElement(new RelicItemElement(this, storeRelic));
    }

    public void removeItem(StoreRelic storeRelic) {
        items.removeIf((item) -> item instanceof RelicItemElement && ((RelicItemElement) item).relic == storeRelic.relic);
    }

    public void addCard(AbstractCard card) {
        if (items.stream().anyMatch((item) -> item instanceof CardItemElement && ((CardItemElement) item).card == card)) {
            return;
        }
        if (card.price * costMultiplier > AbstractDungeon.player.gold - totalCost()) {
            balance.flash();
            createSpeech(ShopScreen.getCantBuyMsg());
            return;
        }
        addItemElement(new CardItemElement(this, card));
    }

    public void removeItem(AbstractCard card) {
        items.removeIf((item) -> item instanceof CardItemElement && ((CardItemElement) item).card == card);
    }

    public void addPotion(StorePotion storePotion) {
        if (items.stream().anyMatch((item) -> item instanceof PotionItemElement &&
                StorePotionPurchasePatch.potionEq(((PotionItemElement) item).potion,storePotion.potion))) {
            return;
        }
        if (storePotion.price * costMultiplier > AbstractDungeon.player.gold - totalCost()) {
            balance.flash();
            createSpeech(ShopScreen.getCantBuyMsg());
            return;
        }
        addItemElement(new PotionItemElement(this, storePotion));
    }

    public void removeItem(StorePotion storePotion) {
        items.removeIf((item) -> item instanceof PotionItemElement &&
                (StorePotionPurchasePatch.potionEq(((PotionItemElement) item).potion,storePotion.potion)));
    }

    public void addCardRemoval() {
        if (items.stream().anyMatch((item) -> item instanceof CardRemovalItemElement)) {
            return;
        }
        if ((AbstractDungeon.player.hasRelic(SmilingMask.ID) ?
                50 : ShopScreen.actualPurgeCost * costMultiplier) > AbstractDungeon.player.gold - totalCost()) {
            balance.flash();
            createSpeech(ShopScreen.getCantBuyMsg());
            return;
        }
        addItemElement(new CardRemovalItemElement(this, ShopScreen.actualPurgeCost));
    }

    public void removeCardRemoval() {
        items.removeIf((item) -> item instanceof CardRemovalItemElement);
    }

    private void addItemElement(ShopItemElement itemElement) {
        itemElement.applyDiscount(costMultiplier);
        items.add(itemElement);
    }

    private ShopSpeechBubble speechBubble;
    private SpeechTextEffect dialogTextEffect;
    private void renderSpeech(SpriteBatch spriteBatch) {
        if (speechBubble != null) speechBubble.render(spriteBatch);
        if (dialogTextEffect != null) dialogTextEffect.render(spriteBatch);
    }

    private void updateSpeech() {
        if (speechBubble != null) {
            speechBubble.update();
            if (speechBubble.isDone) {
                speechBubble = null;
            }
        }
        if (dialogTextEffect != null) {
            dialogTextEffect.update();
            if (dialogTextEffect.isDone) {
                dialogTextEffect = null;
            }
        }
    }

    public void createSpeech(String message) {
        // The size of a speech bubble is 350 by 270.
        float bubbleX = x + width / 2f - 350f / 2f * Settings.scale;
        float bubbleY = y - height - 270f * Settings.scale;

        speechBubble = new ShopSpeechBubble(bubbleX, bubbleY, 4f, message, true);

        float textX = bubbleX + 164f * Settings.scale;
        float textY = bubbleY + 126f * Settings.scale;
        dialogTextEffect = new SpeechTextEffect(textX, textY, 4f, message, DialogWord.AppearEffect.BUMP_IN);
    }

    private ArrayList<String> queuedMessages = new ArrayList<>();
    public void queueMessage(String message) {
        speechTimer = 0.5f;
        queuedMessages.add(message);
    }
    private void attemptToShowQueuedMessages() {
        if (speechBubble == null && dialogTextEffect == null && speechTimer < 0f && !queuedMessages.isEmpty()) {
            createSpeech(queuedMessages.get(0));
            queuedMessages.remove(0);

            speechTimer = MathUtils.random(6f, 9f);
        }
    }

    private void attemptToShowTooltip() {
        // 1% chance to show a new tooltip every tick.
        if (speechBubble == null && dialogTextEffect == null && MathUtils.random() < 0.01 && speechTimer < 0f) {
            if (!ShoppingListMod.config.getBoolean("shownDragTooltip")) {
                createSpeech("Click and hold on to the orange bar to drag the list around.");
                ShoppingListMod.config.setBoolean("shownDragTooltip", true);
                ShoppingListMod.config.save();

                speechTimer = MathUtils.random(10f, 15f);
                return;
            }
            if (!ShoppingListMod.config.getBoolean("shownAltClickTooltip")) {
                createSpeech("Alt+Click an item to add it to the shopping list.");
                ShoppingListMod.config.setBoolean("shownAltClickTooltip", true);
                ShoppingListMod.config.save();

                speechTimer = MathUtils.random(10f, 15f);
                return;
            }
            if (!ShoppingListMod.config.getBoolean("shownRemoveTooltip")) {
                createSpeech("Click on an item in the shopping list to remove it.");
                ShoppingListMod.config.setBoolean("shownRemoveTooltip", true);
                ShoppingListMod.config.save();

                speechTimer = MathUtils.random(10f, 15f);
            }
        }
    }

    private void updateDiscounts() {
        items.forEach((item) -> item.applyDiscount(costMultiplier));
    }
}
