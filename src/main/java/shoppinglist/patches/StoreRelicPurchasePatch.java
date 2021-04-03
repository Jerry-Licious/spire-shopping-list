package shoppinglist.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StoreRelic;
import shoppinglist.ShoppingListPanel;
import shoppinglist.util.KeyHelper;

// Add the relic that the player intends to purchase to the shopping list instead of buying it directly when the Alt
// key is pressed.
@SpirePatch(clz = StoreRelic.class, method = "purchaseRelic")
public class StoreRelicPurchasePatch {
    @SpirePrefixPatch
    public static SpireReturn addStoreRelicToList(StoreRelic storeRelic) {
        // Block all purchases that click through the panel.
        ShoppingListPanel panel = ShoppingListPanelField.shoppingList.get(AbstractDungeon.shopScreen);
        if (panel.hitbox.hovered) {
            return SpireReturn.Return(null);
        }

        // Intercept the interaction if the item is intended to be added to the list.
        if (KeyHelper.isAltPressed()) {
            // Add the item to the list.
            panel.addItem(storeRelic);

            return SpireReturn.Return(null);
        } else if (AbstractDungeon.player.gold >= storeRelic.price) {
            panel.removeItem(storeRelic);
        }

        return SpireReturn.Continue();
    }
}
