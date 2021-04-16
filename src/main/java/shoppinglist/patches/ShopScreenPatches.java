package shoppinglist.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.shop.ShopScreen;
import shoppinglist.ShoppingListMod;
import shoppinglist.ShoppingListPanel;

import java.util.HashMap;

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

    // The shopping list can "reply" to the merchant if the merchant says something.
    @SpirePatch(clz = ShopScreen.class, method = "createSpeech")
    public static class SpeechPatch {
        public static HashMap<String, String> replies = new HashMap<>();
        static {
            replies.put("Hi! NL Buy something! NL Buy buy buy!", "I'm gonna take a look around first.");
            replies.put("I like your haircut", "I grew it myself.");
            replies.put("Do you have the time?", "Lots and lots of time.");
            replies.put("Are you a dog person or a cat person?", "I'm a money person.");
            replies.put("Take your time... NL ..... NL ... or not", "...");
            replies.put("Do you like this rug? NL It's not for sale", "You'll change your mind eventually");
            replies.put("Have you seen my courier?", "Yeah it's adorable.");
            replies.put("It's dangerous to go alone! Give me all your gold!", "I'd rather not.");
            replies.put("Buy somethin'", "Let me think about it.");
            replies.put("I like gold.", "Me too.");
            replies.put("My favorite color is blue. NL How bout you?", "My favourite colour is gold.");
        }

        @SpirePostfixPatch
        public static void createReply(ShopScreen instance, String message) {
            if (ShoppingListMod.config.allowReplies() && replies.containsKey(message)) {
                ShoppingListPanelField.shoppingList.get(instance).queueMessage(replies.get(message));
            }
        }
    }
}
