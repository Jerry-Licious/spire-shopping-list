package shoppinglist;

import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import shoppinglist.config.Config;

@SpireInitializer
public class ShoppingListMod implements PostInitializeSubscriber {
    public static void initialize() {
        new ShoppingListMod();
    }

    public static Config config;

    public ShoppingListMod() {
        BaseMod.subscribe(this);
    }

    @Override
    public void receivePostInitialize() {
        config = new Config();
        Config.setupConfigMenu();
    }
}
