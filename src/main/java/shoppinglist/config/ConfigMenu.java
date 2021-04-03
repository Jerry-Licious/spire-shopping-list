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
                ShoppingListMod.config.useCardArt(), this, (modLabel) -> {}, (modToggleButton) -> {
                    ShoppingListMod.config.toggleUseCardArt();
                    ShoppingListMod.config.save();
        });
        addUIElement(enableCardArt);

        ModLabeledToggleButton enableReplyMessages = new ModLabeledToggleButton(
                "Allow the shopping list to talk back to the merchant.",
                Settings.WIDTH * 0.15f, Settings.HEIGHT * 0.45f,
                Settings.CREAM_COLOR, FontHelper.charDescFont,
                ShoppingListMod.config.allowReplies(), this, (modLabel) -> {}, (modToggleButton) -> {
            ShoppingListMod.config.toggleAllowReplies();
            ShoppingListMod.config.save();
        });
        addUIElement(enableReplyMessages);
    }
}
