package shoppinglist.config;

import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import shoppinglist.ShoppingListMod;

public class ConfigMenu extends ModPanel {
    public ConfigMenu() {
        ModLabeledToggleButton enableCardArt = new ModLabeledToggleButton(
                "Use card arts to represent cards in the shopping list.",
                Settings.WIDTH * 0.15f, Settings.HEIGHT * 0.5f,
                Settings.CREAM_COLOR, FontHelper.charDescFont,
                ShoppingListMod.config.usesCardArt(), this, (modLabel) -> {}, (modToggleButton) -> {
                    ShoppingListMod.config.toggleUseCardArt();
                    ShoppingListMod.config.save();
        });
        addUIElement(enableCardArt);
    }
}
