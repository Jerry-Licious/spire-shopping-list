package shoppinglist.panelelements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import shoppinglist.ShoppingListPanel;

// Displays the leftover money if the player purchases all items in the list.
public class BalanceElement  extends PanelElement {
    public BalanceElement(ShoppingListPanel panel) {
        super(panel, new Color(0.3f, 0.3f, 0.3f, 1f));
    }

    private float goldImageSize = PanelElement.HEIGHT * Settings.scale;

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.setColor(Color.WHITE);
        spriteBatch.draw(ImageMaster.TP_GOLD, x + width * 0.3f - goldImageSize / 2f,
                y - height * 0.5f - goldImageSize / 2f, goldImageSize, goldImageSize);

        FontHelper.renderFontCentered(spriteBatch, PanelFont.contentFont,
                Integer.toString(AbstractDungeon.player.gold - panel.totalCost()),
                x + width * 0.8f, y - height * 0.5f, Color.GOLD);
    }

    private static float totalFlashDuration = 0.3f;
    private float flashDuration = 0f;

    public void flash() {
        flashDuration = totalFlashDuration;
    }

    @Override
    public void update() {
        super.update();

        ShoppingListPanel.logger.info(flashDuration / totalFlashDuration);
        flashDuration -= Gdx.graphics.getDeltaTime();
        backgroundColour = new Color(0.3f, 0.3f, 0.3f, 1f).add(new Color(1f, 0f, 0f, 0.3f * flashDuration / totalFlashDuration).premultiplyAlpha());
    }
}
