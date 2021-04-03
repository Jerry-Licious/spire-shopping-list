package shoppinglist.panelelements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.megacrit.cardcrawl.core.Settings;
import shoppinglist.ShoppingListPanel;
import shoppinglist.UIElement;

// Represents an element that can be displayed on a panel. Has a fixed size based on Settings.scale and a background
// colour.
public abstract class PanelElement extends UIElement {
    public static float WIDTH = 280f;
    public static float HEIGHT = 50f;

    public Color backgroundColour;
    public ShapeRenderer shapeRenderer;
    public ShoppingListPanel panel;

    public PanelElement(ShoppingListPanel panel, Color backgroundColour) {
        this.panel = panel;
        this.backgroundColour = backgroundColour;

        shapeRenderer = new ShapeRenderer();

        width = WIDTH * Settings.scale;
        height = HEIGHT * Settings.scale;

        updateHitboxPosition();
    }

    // The background is rendered separately and before the foreground, thus the background of one element will not
    // block the content of another element.
    public void renderBackground(SpriteBatch spriteBatch) {
        shapeRenderer.setProjectionMatrix(spriteBatch.getProjectionMatrix());

        spriteBatch.end();

        // Enable alpha channel.
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(backgroundColour);
        shapeRenderer.rect(x, y - height, width, height);
        shapeRenderer.end();

        spriteBatch.begin();
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
