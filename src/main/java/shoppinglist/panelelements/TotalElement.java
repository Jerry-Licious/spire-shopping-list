package shoppinglist.panelelements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import shoppinglist.ShoppingListMod;
import shoppinglist.ShoppingListPanel;

// Displays the total price of all items on the panel.
public class TotalElement extends PanelElement {
    public TotalElement(ShoppingListPanel panel) {
        super(panel, new Color(0.4f, 0.4f, 0.4f, 1f));
    }

    private float goldImageSize = PanelElement.HEIGHT * Settings.scale;

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.setColor(Color.WHITE);

        if (ShoppingListMod.config.useIcons()) {
            spriteBatch.draw(ImageMaster.UI_GOLD, x + width * 0.3f - goldImageSize / 2f,
                    y - height * 0.5f - goldImageSize / 2f, goldImageSize, goldImageSize);
        } else {
            FontHelper.renderFontLeftTopAligned(spriteBatch, PanelFont.contentFont, "Total",
                    x + width * 0.08f, y - height * 0.3f, Color.WHITE);
        }

        FontHelper.renderFontCentered(spriteBatch, PanelFont.contentFont, Integer.toString(panel.totalCost()),
                x + width * 0.8f, y - height * 0.5f, Color.GOLD);
    }
}
