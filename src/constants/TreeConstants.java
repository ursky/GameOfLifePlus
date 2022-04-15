package constants;

import world.World;

import javax.swing.*;

public final class TreeConstants extends ThingConstants {
    public TreeConstants(World world) {
        super(world);

        // thing constants
        this.name = "Tree";
        this.type = "Plant";
        this.imagePath = "graphics/tree_4.png";
        this.image = new ImageIcon(this.imagePath).getImage();
        this.startingDensity = 5; // per 100pixels^2
        this.maxSize = 30;
        this.minSizeToShow = 1f;
        this.maxCoolDownFrames = 20;

        // organism constants
        this.metabolismRate = 5.0f; // adjust to FPS and cool down
        this.growAtHealth = 80;
        this.maxGrowthRate = 15.0f; // adjust to FPS and cool down
        this.reproduceAtSize = 0.8f * maxSize;
        this.reproduceAtHealth = 90;
        this.reproductionPenalty = 0.5f;
        this.maxOffsprings = 5;

        // plant constants
        this.dispersalRange = 50;
        this.maxShadeRange = 1.5f * this.maxSize / 2;
        this.shadePenalty = -20f; // adjust to FPS and cool down
        this.sproutTime = 3f; // adjust to FPS and cool down

        this.update();
    }
}
