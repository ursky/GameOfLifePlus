package constants;

import world.World;

public final class GrassConstants extends BlankConstants {
    public GrassConstants(World world) {
        super(world);

        // thing constants
        this.name = "Grass";
        this.type = "Plant";
        this.imagePath = "graphics/grass.png";
        this.youngImagePath = "graphics/seed.png";
        this.deadImagePath = "graphics/dead_plant.png";
        this.startingDensity = 2; // per 100pixels^2
        this.maxSize = 20;
        this.minSizeToShow = 1f;
        this.offScreenCoolDown = 10;
        this.onScreenCoolDown = 10;

        // organism constants
        this.metabolismRate = 5f; // adjust to FPS and cool down
        this.growAtHealth = 50;
        this.maxGrowthRate = 15; // adjust to FPS and cool down
        this.reproduceAtSize = maxSize;
        this.reproduceAtHealth = 90;
        this.reproductionPenalty = -1f;
        this.maxOffsprings = 10;
        this.startHealth = 50;

        // plant constants
        this.dispersalRange = 30;
        this.maxShadeRange = 1.5f * this.maxSize / 2;
        this.shadePenalty = -20f; // adjust to FPS and cool down
        this.sproutTime = 0.1f; // adjust to FPS and cool down
    }
}
