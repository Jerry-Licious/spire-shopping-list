package shoppinglist.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.shop.ShopScreen;
import shoppinglist.ShoppingListPanel;
import shoppinglist.util.KeyHelper;

// Add the card that the player intends to purchase to the shopping list instead of buying it directly when the Alt
// key is pressed.
@SpirePatch(clz = ShopScreen.class, method = "purchaseCard")
public class StoreCardPurchasePatch {
    @SpirePrefixPatch
    public static SpireReturn addCardToList(ShopScreen shopScreen, AbstractCard hoveredCard) {
        // Block all purchases that click through the panel.
        ShoppingListPanel panel = ShoppingListPanelField.shoppingList.get(AbstractDungeon.shopScreen);
        if (panel.hitbox.hovered) {
            return SpireReturn.Return(null);
        }

        // Intercept the interaction if the item is intended to be added to the list.
        if (KeyHelper.isAltPressed()) {
            // Add the item to the list.
            ShoppingListPanelField.shoppingList.get(shopScreen).addItem(hoveredCard);

            return SpireReturn.Return(null);
        }

        return SpireReturn.Continue();
    }
}
