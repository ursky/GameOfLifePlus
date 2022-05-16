package engine.dashboard;

import things.Classes.CreatureConstants;
import engine.Engine;

import java.awt.*;
import java.util.ArrayList;

public class Buttons extends LinePlot {
    Engine engine;
    public ArrayList<ClickableButton> buttons;
    int size, lineWidth;
    public CreatureConstants selectedConstants;

    @Override
    public void update(ArrayList<Integer> values) {
        for (int i=0; i<values.size(); i++) {
            this.buttons.get(i).count = values.get(i);
        }
    }

    @Override
    public void draw() {
        this.drawString(this.label, (this.maxX + this.minX) / 2, this.minY + 10, this.font);
        for (ClickableButton button : this.buttons) {
            this.drawButton(button);
        }
    }

    private void drawButton(ClickableButton button) {
        // draw label
        this.drawString(
                button.name, button.xMin + this.size / 2,
                this.midPointY + this.size / 2, this.font);
        this.drawString(
                this.asString(button.count), button.xMin + this.size / 2 - this.lineWidth,
                this.midPointY + this.size / 2 + 10, this.font);

        // draw borders
        this.engine.g2D.setStroke(new BasicStroke(this.lineWidth));
        this.engine.g2D.setColor(Color.black);
        this.engine.g2D.fillRect(
                button.xMin, button.yMin, this.size - 2 * this.lineWidth, this.size - 2 * this.lineWidth);

        this.engine.g2D.setColor(button.color);
        this.engine.g2D.drawRect(
                button.xMin, button.yMin, this.size - 2 * this.lineWidth, this.size - 2 * this.lineWidth);

        // draw critter image
        this.engine.paintImage(button.image, button.xMin, button.yMin, this.size - 2 * this.lineWidth);
    }

    public void click(int x, int y) {
        // check if click was in the buttons field
        if (x > this.minX && x < this.maxX && y > this.minY && y < this.maxY) {
            for (ClickableButton button : this.buttons) {
                // check if click was on the button
                if (x >= button.xMin && x <= button.xMax && y >= button.yMin && y <= button.yMax) {
                    toggleOn(button);
                    break;
                }
            }
        }
        // check if click was on the play field
        else if (y < this.engine.dashboard.minY) {
            float trueX = this.engine.userIO.reverseTransformX(x);
            float trueY = this.engine.userIO.reverseTransformY(y);
            this.engine.world.initThings.createThing(trueX, trueY, this.selectedConstants.maxSize,
                    this.selectedConstants);
        }
    }

    public void toggleOff(ClickableButton button) {
        button.selected = false;
        button.dim();
    }

    public void toggleOn(ClickableButton button) {
        button.selected = false;
        this.selectedConstants = button.constants;
        button.brighten();
        // un-click all other buttons
        for (ClickableButton otherButton : this.buttons) {
            if (button != otherButton) {
                this.toggleOff(otherButton);
            }
        }
    }

    public Buttons(Engine engine, int startX, int endX, String label) {
        super(engine, startX, endX, 0, label);
        this.engine = engine;
        this.size = (this.maxX - this.minX) / (this.engine.world.initThings.orderedCreatureConstants.size());
        this.lineWidth = this.size / 10;
        this.buttons = new ArrayList<>();

        // create the buttons
        int xMin = this.minX;
        int yMin = this.midPointY - size / 2;
        int yMax = yMin + this.size;
        for (CreatureConstants constants: this.engine.world.initThings.orderedCreatureConstants) {
            int xMax = xMin + this.size + this.lineWidth;
            this.buttons.add(new ClickableButton(constants, xMin, xMax, yMin, yMax));
            xMin = xMax;
        }
        this.toggleOn(this.buttons.get(0));
    }
}