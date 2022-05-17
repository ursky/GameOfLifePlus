package game.dashboard;

import game.world.things.Classes.CreatureConstants;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This stores an icon that can be "clicked" (toggle on/off). Used to display images in dashboard.
 */
public class ClickableButton {
    // this is the toggle switch
    public boolean selected;
    public final BufferedImage image;
    // this is the icon border color
    public Color color;
    // this stores how many of this thing there is
    public int count;
    // this stores the creature constants of this icon
    public CreatureConstants constants;
    // the border can be bright or dim - low alpha means dimmer.
    int dimAlpha = 100;
    int brightAlpha = 255;
    int xMin, xMax, yMin, yMax;
    String name;

    /**
     * Reduce alpha of border (when the button is de-selected)
     */
    public void dim() {
        // dim the border color
        this.color = new Color(
                this.color.getRed(), this.color.getGreen(), this.color.getBlue(), this.dimAlpha);
    }

    /**
     * Increase alpha of border (when button is selected)
     */
    public void brighten() {
        // brighten the border color
        this.color = new Color(
                this.color.getRed(), this.color.getGreen(), this.color.getBlue(), this.brightAlpha);
    }

    /**
     * The button initialization. Start off de-selected.
     * @param constants: the constants of the creature this icon is meant to represent
     * @param xMin: button coordinate
     * @param xMax: button coordinate
     * @param yMin: button coordinate
     * @param yMax: button coordinate
     */
    public ClickableButton(CreatureConstants constants, int xMin, int xMax, int yMin, int yMax) {
        this.constants = constants;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.selected = false;
        this.image = constants.mainImage.getImage(0, 255);
        this.color = constants.color;
        this.count = 0;
        this.name = constants.name;
    }
}