package shoppinglist.panelelements.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.shop.StorePotion;
import shoppinglist.ShoppingListMod;
import shoppinglist.ShoppingListPanel;
import shoppinglist.panelelements.PanelFont;

public class PotionItemElement extends ShopItemElement {
    public AbstractPotion potion;

    public PotionItemElement(ShoppingListPanel panel, AbstractPotion potion, int price) {
        super(panel, price);

        this.potion = potion.makeCopy();
    }

    public PotionItemElement(ShoppingListPanel panel, StorePotion storePotion) {
        this(panel, storePotion.potion, storePotion.price);
    }

    @Override
    public void renderItem(SpriteBatch spriteBatch) {
        if (ShoppingListMod.config.useIcons()) {
            potion.posX = x + width * 0.3f;
            potion.posY = y - height * 0.4f;

            potion.render(spriteBatch);
        } else {
            FontHelper.renderFontLeftTopAligned(spriteBatch, PanelFont.contentFont, potion.name,
                    x + width * 0.08f, y - height * 0.3f, Color.WHITE);
        }
    }

    @Override
    public void update() {
        super.update();
        potion.update();
    }

    @Override
    public void updateHitboxPosition() {
        super.updateHitboxPosition();
    }
}
