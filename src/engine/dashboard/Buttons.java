package engine.dashboard;

import constants.BlankConstants;
import engine.Engine;

import java.awt.*;
import java.util.ArrayList;

public class Buttons extends LinePlot {
    Engine engine;
    public ArrayList<ClickableButton> buttons;
    int xPos, yPos, size, lineWidth;

    @Override
    public void update(ArrayList<Integer> values) {
        for (int i=0; i<values.size(); i++) {
            this.buttons.get(i).count = values.get(i);
        }
    }

    @Override
    public void draw() {
        this.drawString(this.label, (this.maxX + this.minX) / 2, this.minY + 10, this.font);
        this.xPos = this.minX + this.lineWidth;
        for (ClickableButton button : this.buttons) {
            this.drawButton(button, xPos);
            this.xPos += this.xIncrement;
        }
    }

    private void drawButton(ClickableButton button, int xPos) {
        // draw label
        this.drawString(
                button.name, xPos + this.size / 2 - this.lineWidth,
                this.midPointY + this.size / 2, this.font);
        this.drawString(
                this.asString(button.count), xPos + this.size / 2 - this.lineWidth,
                this.midPointY + this.size / 2 + 10, this.font);

        // draw borders
        this.engine.g2D.setStroke(new BasicStroke(this.lineWidth));
        this.engine.g2D.setColor(Color.black);
        this.engine.g2D.fillRect(
                this.xPos, this.yPos, this.size - 2 * this.lineWidth, this.size - 2 * this.lineWidth);
        this.engine.g2D.setColor(button.color);
        this.engine.g2D.drawRect(
                this.xPos, this.yPos, this.size - 2 * this.lineWidth, this.size - 2 * this.lineWidth);

        // draw critter image
        this.engine.paintImage(button.image, this.xPos, this.yPos, this.size - 2 * this.lineWidth);
    }

    public Buttons(Engine engine, int startX, int endX, int xIncrement, String label) {
        super(engine, startX, endX, xIncrement, label);
        this.engine = engine;
        this.buttons = new ArrayList<>();
        for (BlankConstants constants: this.engine.world.initThings.orderedBlankConstants) {
            this.buttons.add(new ClickableButton(constants));
        }
        this.xIncrement = (this.maxX - this.minX) / (this.buttons.size());
        this.yPos = this.midPointY - this.xIncrement / 2;
        this.size = this.xIncrement;
        this.lineWidth = this.size / 10;
    }
}