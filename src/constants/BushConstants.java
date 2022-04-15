package constants;

import world.World;

import javax.swing.*;

public final class BushConstants extends ThingConstants {
    public BushConstants(World world) {
        super(world);

        // thing constants
        this.name = "Bush";
        this.type = "Plant";
        this.imagePath = "graphics/tree_1.png";
        this.image = new ImageIcon(this.imagePath).getImage();
        this.startingDensity = 30; // per 100pixels^2
        this.maxSize = 10;
        this.minSizeToShow = 1f;
        this.maxCoolDownFrames = 20;

        // organism constants
        this.metabolismRate = 5f; // adjust to FPS and cool down
        this.growAtHealth = 50;
        this.maxGrowthRate = 15.0f; // adjust to FPS and cool down
        this.reproduceAtSize = 0.6f * maxSize;
        this.reproduceAtHealth = 90;
        this.reproductionPenalty = 0.8f;
        this.maxOffsprings = 10;

        // plant constants
        this.dispersalRange = 30;
        this.maxShadeRange = 1.5f * this.maxSize / 2;
        this.shadePenalty = -20f; // adjust to FPS and cool down
        this.sproutTime = 1.1f; // adjust to FPS and cool down
    }
}
