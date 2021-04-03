package shoppinglist.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.shop.ShopScreen;
import shoppinglist.ShoppingListPanel;

// Add a shopping list field to the ShopScreen class so it is included on every shop screen.
@SpirePatch(clz = ShopScreen.class, method = SpirePatch.CLASS)
public class ShoppingListPanelField {
    public static SpireField<ShoppingListPanel> shoppingList = new SpireField<>(ShoppingListPanel::new);
}
