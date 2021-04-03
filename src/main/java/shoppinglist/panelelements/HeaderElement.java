package shoppinglist.panelelements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.FontHelper;
import shoppinglist.ShoppingListPanel;

// Labels the two columns of the panel.
public class HeaderElement extends PanelElement {
    private String ltitle, rtitle;

    public HeaderElement(ShoppingListPanel panel, String ltitle, String rtitle) {
        super(panel, Color.GOLDENROD.cpy());

        this.ltitle = ltitle;
        this.rtitle = rtitle;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        FontHelper.renderFontCentered(spriteBatch, PanelFont.contentFont, ltitle,
                x + width * 0.3f, y - height * 0.5f, Color.WHITE);
        FontHelper.renderFontCentered(spriteBatch, PanelFont.contentFont, rtitle,
                x + width * 0.8f, y - height * 0.5f, Color.WHITE);
    }
}
