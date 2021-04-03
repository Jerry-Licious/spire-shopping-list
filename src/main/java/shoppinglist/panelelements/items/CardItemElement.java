package shoppinglist.panelelements.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import shoppinglist.ShoppingListMod;
import shoppinglist.ShoppingListPanel;
import shoppinglist.panelelements.PanelElement;
import shoppinglist.panelelements.PanelFont;

public class CardItemElement extends ShopItemElement {
    public AbstractCard card;
    private TextureAtlas.AtlasRegion cardImage;
    private Color cardNameColour;

    public CardItemElement(ShoppingListPanel panel, AbstractCard card, int cost) {
        super(panel, cost);

        this.card = card;

        // Use the beta art if it is enabled.
        if (UnlockTracker.betaCardPref.getBoolean(card.cardID, false) || Settings.PLAYTESTER_ART_MODE) {
            cardImage = new TextureAtlas.AtlasRegion(card.jokePortrait.getTexture(),
                    card.portrait.getRegionX(), (int)(card.portrait.packedHeight * 0.3 + card.portrait.getRegionY()),
                    card.portrait.packedWidth, (int)(card.portrait.packedHeight * PanelElement.HEIGHT / PanelElement.WIDTH));
        } else {
            cardImage = new TextureAtlas.AtlasRegion(card.portrait.getTexture(),
                    card.portrait.getRegionX(), (int)(card.portrait.packedHeight * 0.3 + card.portrait.getRegionY()),
                    card.portrait.packedWidth, (int)(card.portrait.packedHeight * PanelElement.HEIGHT / PanelElement.WIDTH));
        }

        switch (card.rarity) {
            case UNCOMMON:
                cardNameColour = Color.CYAN.cpy();
                break;
            case RARE:
                cardNameColour = Color.GOLD.cpy();
                break;
            default:
                cardNameColour = Color.WHITE.cpy();
        }

        if (ShoppingListMod.config.usesCardArt()) {
            backgroundColour = new Color(0f, 0f, 0f, 0.5f);
        }
    }

    public CardItemElement(ShoppingListPanel panel, AbstractCard card) {
        this(panel, card, card.price);
    }

    @Override
    public void renderItem(SpriteBatch spriteBatch) {
        FontHelper.renderFontLeftTopAligned(spriteBatch, PanelFont.contentFont, card.name,
                x + width * 0.08f, y - height * 0.3f, cardNameColour);
    }

    @Override
    public void renderBackground(SpriteBatch spriteBatch) {
        if (ShoppingListMod.config.usesCardArt()) {
            spriteBatch.setColor(Color.WHITE);
            spriteBatch.draw(cardImage, x, y - height,
                    PanelElement.WIDTH * Settings.scale, PanelElement.HEIGHT * Settings.scale);
        }

        super.renderBackground(spriteBatch);
    }

    @Override
    public void update() {
        super.update();

        if (ShoppingListMod.config.usesCardArt()) {
            if (hitbox.hovered) {
                backgroundColour = new Color(1f, 0f, 0f, 0.5f);
            } else {
                backgroundColour = new Color(0f, 0f, 0f, 0.4f);
            }
        }
    }
}
