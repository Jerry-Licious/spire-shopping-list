package shoppinglist;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

// Represents an UI element that can be displayed on the screen.
public abstract class UIElement {
    public Hitbox hitbox = new Hitbox(0f, 0f, 0f, 0f);
    public float x, y, width, height;

    public abstract void render(SpriteBatch spriteBatch);

    public void update() {
        updateHitboxPosition();
        hitbox.update();

        if (hitbox.hovered && InputHelper.justClickedLeft) {
            hitbox.clickStarted = true;
        }
    }

    // Change the hitbox's position to always follow the element's position.
    public void updateHitboxPosition(){
        hitbox.x = x;
        hitbox.y = y - height;
        hitbox.width = width;
        hitbox.height = height;
    }
}
