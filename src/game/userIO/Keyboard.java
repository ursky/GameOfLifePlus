package game.userIO;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Detect keyboard presses
 */
public class Keyboard {
    /**
     * Initialize keyboard tracker
     */
    private static final Map<Integer, Boolean> pressedKeys = new HashMap<>();
    static {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(event -> {
            synchronized (Keyboard.class) {
                if (event.getID() == KeyEvent.KEY_PRESSED) pressedKeys.put(event.getKeyCode(), true);
                else if (event.getID() == KeyEvent.KEY_RELEASED) pressedKeys.put(event.getKeyCode(), false);
                return false;
            }
        });
    }

    /**
     * Check if a given key is pressed this moment
     * @param keyCode: key to check
     * @return: was it pressed this frame?
     */
    public static boolean isKeyPressed(int keyCode) {
        return pressedKeys.getOrDefault(keyCode, false);
    }
}