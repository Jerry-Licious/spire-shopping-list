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

        ModLabeledToggleButton enableIcons = new ModLabeledToggleButton(
                "Use icons instead of names to represent potions, relics and card removals.",
                Settings.WIDTH * 0.15f, Settings.HEIGHT * 0.4f,
                Settings.CREAM_COLOR, FontHelper.charDescFont,
                ShoppingListMod.config.useIcons(), this, (modLabel) -> {}, (modToggleButton) -> {
            ShoppingListMod.config.toggleUseIcons();
            ShoppingListMod.config.save();
        });
        addUIElement(enableIcons);

        ModLabeledToggleButton preventOverdraw = new ModLabeledToggleButton(
                "Prevent you from adding items that you cannot afford to the list.",
                Settings.WIDTH * 0.15f, Settings.HEIGHT * 0.4f,
                Settings.CREAM_COLOR, FontHelper.charDescFont,
                ShoppingListMod.config.preventOverdraw(), this, (modLabel) -> {}, (modToggleButton) -> {
            ShoppingListMod.config.togglePreventOverdraw();
            ShoppingListMod.config.save();
        });
        addUIElement(preventOverdraw);
    }
}
