package engine.dashboard;

import constants.BlankConstants;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ClickableButton {
    public boolean selected;
    public BufferedImage image;
    public Color color;
    public int count;
    public BlankConstants constants;
    int dimAlpha = 150;
    int brightAlpha = 255;
    String name;

    public void unselect() {
        this.selected = false;
        // dim the border color
        this.color = new Color(
                this.color.getRed(), this.color.getGreen(), this.color.getBlue(), this.dimAlpha);
    }

    public void select() {
        this.selected = false;
        // brighten the border color
        this.color = new Color(
                this.color.getRed(), this.color.getGreen(), this.color.getBlue(), this.brightAlpha);
    }

    public ClickableButton(BlankConstants constants) {
        this.constants = constants;
        this.selected = false;
        this.image = constants.mainImage.getImage(0, 255);
        this.color = constants.color;
        this.count = 0;
        this.unselect();
        this.name = constants.name;
    }
}