package constants;

import engine.ImageStack;
import world.World;

import java.awt.*;

public final class BushConstants extends BlankConstants {
    public BushConstants(World world) {
        super(world);

        // thing constants
        this.name = "Bush";
        this.type = "Plant";
        this.mainImage = new ImageStack("bush.png", 0, 10, 1);
        this.youngImage = new ImageStack("seed.png", 0, 3, 1);
        this.deadImage = new ImageStack("dead_tree.png", 0, 3, 10);
        this.startingDensity = 1; // per 100pixels^2
        this.maxSize = 30;
        this.minSizeToShow = 1f;
        this.offScreenCoolDown = 10;
        this.onScreenCoolDown = 10;

        // organism constants
        this.metabolismRate = 5f; // adjust to FPS and cool down
        this.decayRate = -200; // adjust to FPS and cool down
        this.growAtHealth = 50;
        this.maxGrowthRate = 10.0f; // adjust to FPS and cool down
        this.reproduceAtSize = 0.6f * maxSize;
        this.reproduceAtHealth = 90;
        this.reproductionPenalty = 0.6f;
        this.maxOffsprings = 5;
        this.startHealth = 50;

        // plant constants
        this.dispersalRange = 30;
        this.maxShadeRange = 1.5f * this.maxSize / 2;
        this.shadePenalty = -20f; // adjust to FPS and cool down
        this.sproutTime = 1f; // adjust to FPS and cool down
    }
}
