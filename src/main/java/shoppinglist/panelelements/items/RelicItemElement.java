package shoppinglist.panelelements.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.MembershipCard;
import com.megacrit.cardcrawl.shop.StoreRelic;
import shoppinglist.ShoppingListMod;
import shoppinglist.ShoppingListPanel;
import shoppinglist.panelelements.PanelElement;
import shoppinglist.panelelements.PanelFont;

public class RelicItemElement extends ShopItemElement {
    public AbstractRelic relic;

    public RelicItemElement(ShoppingListPanel panel, AbstractRelic relic, int price) {
        super(panel, price);

        this.relic = relic;
    }

    public RelicItemElement(ShoppingListPanel panel, StoreRelic relic) {
        this(panel, relic.relic, relic.price);
    }

    private static float relicImageSize = PanelElement.HEIGHT * Settings.scale * 1.6f;

    @Override
    public void renderItem(SpriteBatch spriteBatch) {
        spriteBatch.setColor(Color.WHITE);

        if (ShoppingListMod.config.useIcons()) {
            spriteBatch.draw(relic.outlineImg, x + width * 0.3f - relicImageSize / 2, y - height * 0.5f - relicImageSize / 2,
                    relicImageSize, relicImageSize);
            spriteBatch.draw(relic.img, x + width * 0.3f - relicImageSize / 2, y - height * 0.5f - relicImageSize / 2,
                    relicImageSize, relicImageSize);
        } else {
            FontHelper.renderFontLeftTopAligned(spriteBatch, PanelFont.contentFont, relic.name,
                    x + width * 0.08f, y - height * 0.3f, Color.WHITE);
        }
    }

    @Override
    public void applyDiscount(float multiplier) {
        if (!(relic instanceof MembershipCard)) {
            super.applyDiscount(multiplier);
        }
    }
}
