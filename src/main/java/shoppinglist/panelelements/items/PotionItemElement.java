package shoppinglist.panelelements.items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.shop.StorePotion;
import shoppinglist.ShoppingListPanel;

public class PotionItemElement extends ShopItemElement {
    public AbstractPotion potion;

    public PotionItemElement(ShoppingListPanel panel, AbstractPotion potion, int cost) {
        super(panel, cost);

        this.potion = potion.makeCopy();
    }

    public PotionItemElement(ShoppingListPanel panel, StorePotion storePotion) {
        this(panel, storePotion.potion, storePotion.price);
    }

    @Override
    public void renderItem(SpriteBatch spriteBatch) {
        potion.posX = x + width * 0.3f;
        potion.posY = y - height * 0.4f;

        potion.render(spriteBatch);
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
