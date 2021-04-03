package shoppingcart.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class KeyHelper {
    public static boolean isAltPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.ALT_RIGHT);
    }
}
