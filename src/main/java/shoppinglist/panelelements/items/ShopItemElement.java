package shoppinglist.panelelements.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.megacrit.cardcrawl.helpers.FontHelper;
import shoppinglist.ShoppingListPanel;
import shoppinglist.panelelements.PanelElement;
import shoppinglist.panelelements.PanelFont;

// Represents a shop item, has a price and an item that can be rendered.
public abstract class ShopItemElement extends PanelElement {
    public ShapeRenderer shapeRenderer;
    public int price;

    public ShopItemElement(ShoppingListPanel panel, int price) {
        super(panel, Color.GRAY.cpy());

        shapeRenderer = new ShapeRenderer();

        this.price = price;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        // Not using the super render call because shop items after this can block the line with their background.

        renderItem(spriteBatch);
        renderLineUnderItem(spriteBatch);
        FontHelper.renderFontCentered(spriteBatch, PanelFont.contentFont, Integer.toString(price),
                x + width * 0.8f, y - height * 0.5f, Color.WHITE);

        hitbox.render(spriteBatch);
    }

    // Render a line under each shop item element to separate them from each other.
    private void renderLineUnderItem(SpriteBatch spriteBatch) {
        shapeRenderer.setProjectionMatrix(spriteBatch.getProjectionMatrix());

        spriteBatch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0.4f, 0.4f, 0.4f, 1f));
        shapeRenderer.rectLine(x + width * 0.05f, y - height, x + width * 0.95f, y - height, 2f);
        shapeRenderer.end();

        spriteBatch.begin();
    }

    public abstract void renderItem(SpriteBatch spriteBatch);

    @Override
    public void update() {
        super.update();

        hitbox.update();

        if (hitbox.hovered) {
            backgroundColour = new Color(0.8f, 0.4f, 0.4f, 1f);
        } else {
            backgroundColour = Color.GRAY.cpy();
        }

        if (hitbox.clicked) {
            panel.addToToRemove(this);
            hitbox.clicked = false;
        }
    }
}
