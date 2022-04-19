package constants;

import engine.ImageStack;
import world.World;

public final class GrassConstants extends BlankConstants {
    public GrassConstants(World world) {
        super(world);

        // thing constants
        this.name = "Grass";
        this.type = "Plant";
        this.mainImage = new ImageStack("grass.png", 0, 10, 1);
        this.youngImage = new ImageStack("seed.png", 0, 3, 1);
        this.deadImage = new ImageStack("dead_grass.png", 0, 3, 10);
        this.startingDensity = 2; // per 100pixels^2
        this.maxSize = 20;
        this.minSizeToShow = 1f;
        this.offScreenCoolDown = 10;
        this.onScreenCoolDown = 10;

        // organism constants
        this.metabolismRate = 5f; // adjust to FPS and cool down
        this.decayRate = -500; // adjust to FPS and cool down
        this.growAtHealth = 50;
        this.maxGrowthRate = 15; // adjust to FPS and cool down
        this.reproduceAtSize = maxSize;
        this.reproduceAtHealth = 90;
        this.reproductionPenalty = -1f;
        this.maxOffsprings = 10;
        this.startHealth = 90;

        // plant constants
        this.dispersalRange = 30;
        this.maxShadeRange = 1.5f * this.maxSize / 2;
        // todo: if a small plant grows into the shade of a larger one, it should not die - just stay at that size
        this.shadePenalty = -20f; // adjust to FPS and cool down
        this.sproutTime = 0.1f; // adjust to FPS and cool down
    }
}
