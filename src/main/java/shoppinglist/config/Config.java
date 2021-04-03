package shoppinglist.config;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;

import java.io.IOException;
import java.util.Properties;

public class Config {
    private Properties defaults = new Properties();
    private SpireConfig spireConfig;
    public Config() {
        defaults.put("useCardArt", true);

        defaults.put("shownDragTooltip", false);
        defaults.put("shownAltClickTooltip", false);
        defaults.put("shownRemoveTooltip", false);

        try {
            spireConfig = new SpireConfig("shopping_list", "config", defaults);
            spireConfig.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUseCardArt(boolean value) {
        spireConfig.setBool("useCardArt", value);
    }
    public boolean usesCardArt() {
        return spireConfig.getBool("useCardArt");
    }
    public void toggleUseCardArt() {
        setUseCardArt(!usesCardArt());
    }

    public void save() {
        try {
            spireConfig.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setupConfigMenu() {
        BaseMod.registerModBadge(new Texture("shopping_list/icon.png"), "Shopping List", "Jerry",
                "Plan your shopping ahead of time instead of pulling up a calculator!",
                new ConfigMenu());
    }

    public boolean getBoolean(String key) {
        return spireConfig.getBool(key);
    }
    public void setBoolean(String key, boolean value) {
        spireConfig.setBool(key, value);
    }
}
