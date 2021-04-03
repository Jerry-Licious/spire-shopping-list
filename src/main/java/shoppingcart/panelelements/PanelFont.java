package shoppingcart.panelelements;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.helpers.FontHelper;

@SpirePatch(clz = FontHelper.class, method = "initialize")
public class PanelFont {
    public static BitmapFont titleFont;
    public static BitmapFont contentFont;

    // Create fonts with the same configurations as the tooltip description font.
    @SpireInsertPatch(rloc = 115)
    public static void initialisePlainFonts() {
        titleFont = ReflectionHacks.privateStaticMethod(FontHelper.class, "prepFont",
                float.class, boolean.class).invoke(new Object[]{PanelElement.HEIGHT * 0.8f, false});

        contentFont = ReflectionHacks.privateStaticMethod(FontHelper.class, "prepFont",
                float.class, boolean.class).invoke(new Object[]{PanelElement.HEIGHT * 0.5f, false});
    }
}
