package game.dashboard;

import game.things.Classes.CreatureConstants;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ClickableButton {
    public boolean selected;
    public BufferedImage image;
    public Color color;
    public int count;
    public CreatureConstants constants;
    int dimAlpha = 150;
    int brightAlpha = 255;
    int xMin, xMax, yMin, yMax;
    String name;

    public void dim() {
        // dim the border color
        this.color = new Color(
                this.color.getRed(), this.color.getGreen(), this.color.getBlue(), this.dimAlpha);
    }

    public void brighten() {
        // brighten the border color
        this.color = new Color(
                this.color.getRed(), this.color.getGreen(), this.color.getBlue(), this.brightAlpha);
    }

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