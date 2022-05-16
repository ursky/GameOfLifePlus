package engine.userIO;

import engine.Engine;
import engine.dashboard.ClickableButton;

import java.awt.event.MouseEvent;

public class Mouse extends java.awt.event.MouseAdapter {
    Engine engine;
    public void mouseReleased(MouseEvent event) {
         if (event.getButton() == 1) {
             // button was pressed and release here - check if anything needs doing
             this.handleClick(event.getX(), event.getY());
         }
    }

    private void handleClick(int x, int y) {
        this.engine.dashboard.buttons.click(x, y);
    }

    public Mouse(Engine engine) {
        this.engine = engine;
    }

}
