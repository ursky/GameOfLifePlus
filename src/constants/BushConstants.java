package constants;

import world.World;

public final class BushConstants extends BlankConstants {
    public BushConstants(World world) {
        super(world);

        // thing constants
        this.name = "Bush";
        this.type = "Plant";
        this.imagePath = "graphics/bush.png";
        this.youngImagePath = "graphics/seed.png";
        this.deadImagePath = "graphics/dead_plant.png";
        this.startingDensity = 1; // per 100pixels^2
        this.maxSize = 30;
        this.minSizeToShow = 1f;
        this.offScreenCoolDown = 10;
        this.onScreenCoolDown = 10;

        // organism constants
        this.metabolismRate = 5f; // adjust to FPS and cool down
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
