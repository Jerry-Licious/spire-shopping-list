package shoppingcart.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.shop.ShopScreen;
import shoppingcart.ShoppingListPanel;

public class ShopScreenPatches {
    @SpirePatch(clz = ShopScreen.class, method = SpirePatch.CONSTRUCTOR)
    public static class ConstructorPatch {
        @SpirePostfixPatch
        // Set the shop screen property of the shopping cart panel to allow it to access the shop screen.
        public static void setShopScreen(ShopScreen instance) {
            ShoppingListPanelField.shoppingList.get(instance).shopScreen = instance;
        }
    }

    // Render the shopping list when the render method is called on the shop screen.
    @SpirePatch(clz = ShopScreen.class, method = "render")
    public static class RenderPatch {
        @SpirePostfixPatch
        public static void renderShoppingCart(ShopScreen instance, SpriteBatch spriteBatch) {
            ShoppingListPanelField.shoppingList.get(instance).render(spriteBatch);
        }
    }

    // Update the shopping list when the update method is called on the shopping screen.
    @SpirePatch(clz = ShopScreen.class, method = "update")
    public static class UpdatePatch {
        @SpirePostfixPatch
        public static void updateShoppingCart(ShopScreen instance) {
            ShoppingListPanelField.shoppingList.get(instance).update();
        }
    }

    // Re-initialise the panel whenever the shop screen is initialized (apparently the game reuses the same
    // shopscreen object).
    @SpirePatch(clz = ShopScreen.class, method = "init")
    public static class InitPatch {
        @SpirePostfixPatch
        public static void initialisePanel(ShopScreen instance) {
            ShoppingListPanelField.shoppingList.set(instance, new ShoppingListPanel());
        }
    }
}
