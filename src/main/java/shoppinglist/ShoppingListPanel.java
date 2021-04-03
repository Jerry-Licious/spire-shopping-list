package shoppinglist;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;
import com.megacrit.cardcrawl.shop.StoreRelic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shoppinglist.panelelements.*;
import shoppinglist.panelelements.items.*;

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

    public ShoppingListPanel() {
        x = Settings.WIDTH * 0.7f;
        y = Settings.HEIGHT * 0.5f;

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
        toRemove.clear();

        total.update();
        balance.update();

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
        return items.stream().map((element) -> element.price).reduce(0, Integer::sum);
    }

    public void addItem(StoreRelic storeRelic) {
        if (items.stream().anyMatch((item) -> item instanceof RelicItemElement &&
                ((RelicItemElement) item).relic == storeRelic.relic)) {
            return;
        }
        if (storeRelic.price > AbstractDungeon.player.gold - totalCost()) {
            balance.flash();
            return;
        }
        items.add(new RelicItemElement(this, storeRelic));
    }

    public void removeItem(StoreRelic storeRelic) {
        items.removeIf((item) -> item instanceof RelicItemElement && ((RelicItemElement) item).relic == storeRelic.relic);
    }

    public void addItem(AbstractCard card) {
        if (items.stream().anyMatch((item) -> item instanceof CardItemElement && ((CardItemElement) item).card == card)) {
            return;
        }
        if (card.price > AbstractDungeon.player.gold - totalCost()) {
            balance.flash();
            return;
        }
        items.add(new CardItemElement(this, card));
    }

    public void removeItem(AbstractCard card) {
        items.removeIf((item) -> item instanceof CardItemElement && ((CardItemElement) item).card == card);
    }

    public void addItem(StorePotion storePotion) {
        if (items.stream().anyMatch((item) -> item instanceof PotionItemElement &&
                ((PotionItemElement) item).potion == storePotion.potion)) {
            return;
        }
        if (storePotion.price > AbstractDungeon.player.gold - totalCost()) {
            balance.flash();
            return;
        }
        items.add(new PotionItemElement(this, storePotion));
    }

    public void removeItem(StorePotion storePotion) {
        items.removeIf((item) -> item instanceof PotionItemElement &&
                ((PotionItemElement) item).potion == storePotion.potion);
    }

    public void addCardRemoval() {
        if (items.stream().anyMatch((item) -> item instanceof CardRemovalItemElement)) {
            return;
        }
        if (ShopScreen.actualPurgeCost > AbstractDungeon.player.gold - totalCost()) {
            balance.flash();
            return;
        }
        items.add(new CardRemovalItemElement(this, ShopScreen.actualPurgeCost));
    }

    public void removeCardRemoval() {
        items.removeIf((item) -> item instanceof CardRemovalItemElement);
    }
}
