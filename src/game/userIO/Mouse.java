package game.userIO;

import game.Engine;

import java.awt.event.MouseEvent;

/**
 * Listener for mouse events. Detect any clicks and handle them.
 */
public class Mouse extends java.awt.event.MouseAdapter {
    Engine engine;

    /**
     * A mouse click (release) was detected!
     * @param event the event to be processed
     */
    public void mouseReleased(MouseEvent event) {
         if (event.getButton() == 1) {
             // button was pressed and release here - check if anything needs doing
             this.handleClick(event.getX(), event.getY());
         }
    }

    /**
     * A click was detected here - do something
     * @param x: click location of field
     * @param y: click location of field
     */
    private void handleClick(int x, int y) {
        this.engine.dashboard.creatureIcons.click(x, y);
    }

    /**
     * Initialize mouse listener
     * @param engine: game engine
     */
    public Mouse(Engine engine) {
        this.engine = engine;
    }

}
