package game.dashboard;

import game.quadsearch.Point;
import game.world.things.Classes.CreatureConstants;
import game.Game;

import java.awt.*;
import java.util.ArrayList;

/**
 * This class handles all the creature icons and buttons at the bottom right of the screen.
 * Inherits from the LinePlot (to be able to draw more efficiently).
 */
public class CreatureIcons extends LinePlot {
    Game game;
    // stored list of the icons/buttons
    public ArrayList<ClickableButton> buttons;
    int size, lineWidth;
    // this stores which creature is currently toggled ON (selected)
    public CreatureConstants selectedConstants;

    /**
     * Updates the counts of each creature icon
     * @param values: list of creature counts
     */
    @Override
    public void update(ArrayList<Integer> values) {
        for (int i=0; i<values.size(); i++) {
            this.buttons.get(i).count = values.get(i);
        }
    }

    /**
     * Draws the buttons/icons onto the Graphics panel
     */
    @Override
    public void draw() {
        this.drawString(this.label, (this.maxX + this.minX) / 2, this.minY + resize(10), this.font);
        for (ClickableButton button : this.buttons) {
            this.drawButton(button);
        }
    }

    /**
     * Draws a button onto the correct location on the panel
     * @param button: the button/icon to plot
     */
    private void drawButton(ClickableButton button) {
        // draw label
        this.drawString(
                button.name, button.xMin + this.size / 2 - this.lineWidth,
                this.midPointY + this.size / 2 + 1, this.font);
        this.drawString(
                this.asString(button.count), button.xMin + this.size / 2 - this.lineWidth,
                this.midPointY + this.size / 2 + resize(10) + 1, this.font);

        // draw borders
        this.game.g2D.setStroke(new BasicStroke(this.lineWidth));
        this.game.g2D.setColor(Color.black);
        this.game.g2D.fillRect(
                button.xMin, button.yMin, this.size - 2 * this.lineWidth, this.size - 2 * this.lineWidth);

        this.game.g2D.setColor(button.color);
        this.game.g2D.drawRect(
                button.xMin, button.yMin, this.size - 2 * this.lineWidth, this.size - 2 * this.lineWidth);

        // draw critter image
        this.game.paintImage(button.image, button.xMin, button.yMin, this.size - 2 * this.lineWidth);
    }

    /**
     * Handle a click event! Check where the click occured and either select the corresponding button or add a creature.
     * @param x: click position
     * @param y: click position
     */
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
        else if (y < this.game.dashboard.minY) {
            Point trueCoordinate = this.game.userIO.reverseTransformCoordinate(x, y);
            this.game.world.initThings.createThing(trueCoordinate, this.selectedConstants.maxSize,
                    this.selectedConstants);
        }
    }

    /**
     * De-select a button
     * @param button: the button to de-select
     */
    public void toggleOff(ClickableButton button) {
        button.selected = false;
        button.dim();
    }

    /**
     * Select a button. Then un-select all other buttons.
     * @param button: button to select
     */
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

    /**
     * Initialize the buttons. Start off with the first one being selected.
     * @param game: game engine
     * @param startX: buttons left bounds
     * @param endX: buttons right bounds
     * @param label: the name of dashboard piece
     */
    public CreatureIcons(Game game, int startX, int endX, String label) {
        super(game, startX, endX, 0, label);
        this.game = game;
        this.size = (this.maxX - this.minX) / (this.game.world.initThings.orderedCreatureConstants.size());
        this.lineWidth = this.size / 10;
        this.buttons = new ArrayList<>();

        // create the buttons
        int xMin = this.minX;
        int yMin = this.midPointY - size / 2;
        int yMax = yMin + this.size;
        for (CreatureConstants constants: this.game.world.initThings.orderedCreatureConstants) {
            int xMax = xMin + this.size + this.lineWidth;
            this.buttons.add(new ClickableButton(constants, xMin, xMax, yMin, yMax));
            xMin = xMax;
        }
        // start by having the first button selected
        this.toggleOn(this.buttons.get(0));
    }
}