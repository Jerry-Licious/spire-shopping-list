package shoppinglist.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.shop.ShopScreen;
import shoppinglist.ShoppingListPanel;
import shoppinglist.util.KeyHelper;

// Add a card removal to the shopping list instead of buying it directly when the player tries to purchase a card
// removal while the Alt key is pressed.
@SpirePatch(clz = ShopScreen.class, method = "purchasePurge")
public class StorePurgePurchasePatch {
    @SpirePrefixPatch
    public static SpireReturn addCardRemoveToList(ShopScreen _shopScreen) {
        // Block all purchases that click through the panel.
        ShoppingListPanel panel = ShoppingListPanelField.shoppingList.get(AbstractDungeon.shopScreen);
        if (panel.hitbox.hovered) {
            return SpireReturn.Return(null);
        }

        // Intercept the interaction if the item is intended to be added to the list.
        if (KeyHelper.isAltPressed()) {
            // Add the item to the list.
            ShoppingListPanelField.shoppingList.get(AbstractDungeon.shopScreen).addCardRemoval();

            return SpireReturn.Return(null);
        }

        return SpireReturn.Continue();
    }
}
