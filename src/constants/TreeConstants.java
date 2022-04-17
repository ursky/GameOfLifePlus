package constants;

import world.World;

public final class TreeConstants extends BlankConstants {
    public TreeConstants(World world) {
        super(world);

        // thing constants
        this.name = "Tree";
        this.type = "Plant";
        this.imagePath = "graphics/tree.png";
        this.youngImagePath = "graphics/seed.png";
        this.deadImagePath = "graphics/dead_plant.png";
        this.startingDensity = 0.1f; // per 100pixels^2
        this.maxSize = 50;
        this.minSizeToShow = 1f;
        this.offScreenCoolDown = 10;
        this.onScreenCoolDown = 10;

        // organism constants
        this.metabolismRate = 15.0f; // adjust to FPS and cool down
        this.growAtHealth = 80;
        this.maxGrowthRate = 15.0f; // adjust to FPS and cool down
        this.reproduceAtSize = 0.8f * maxSize;
        this.reproduceAtHealth = 90;
        this.reproductionPenalty = 0.5f;
        this.maxOffsprings = 5;
        this.startHealth = 50;

        // plant constants
        this.dispersalRange = 50;
        this.maxShadeRange = 1.2f * this.maxSize / 2;
        this.shadePenalty = -30f; // adjust to FPS and cool down
        this.sproutTime = 1f; // adjust to FPS and cool down

        this.update();
    }
}
