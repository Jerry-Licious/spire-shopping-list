package shoppinglist.panelelements.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;
import shoppinglist.ShoppingListMod;
import shoppinglist.ShoppingListPanel;
import shoppinglist.panelelements.PanelElement;
import shoppinglist.panelelements.PanelFont;

public class CardRemovalItemElement extends ShopItemElement {
    private TextureAtlas.AtlasRegion noDrawIcon;
    private static float iconImageSize = PanelElement.HEIGHT * Settings.scale * 0.8f;

    public CardRemovalItemElement(ShoppingListPanel panel, int cost) {
        super(panel, cost);

        noDrawIcon = AbstractPower.atlas.findRegion("128/noDraw");
    }

    @Override
    public void renderItem(SpriteBatch spriteBatch) {
        if (ShoppingListMod.config.useIcons()) {
            spriteBatch.setColor(Color.WHITE);
            spriteBatch.draw(noDrawIcon, x + width * 0.3f - iconImageSize / 2, y - height * 0.5f - iconImageSize / 2,
                    iconImageSize, iconImageSize);
        } else {
            FontHelper.renderFontLeftTopAligned(spriteBatch, PanelFont.contentFont, "Card Removal",
                    x + width * 0.08f, y - height * 0.3f, Color.WHITE);
        }
    }
}
