package constants;

import engine.ImageStack;
import world.World;

public final class GrassConstants extends BlankConstants {
    public GrassConstants(World world) {
        super(world);

        // thing constants
        this.name = "Grass";
        this.type = "Plant";
        this.mainImage = new ImageStack("grass.png",
                0, 10, 1, 400, 0);
        this.youngImage = new ImageStack("seed.png",
                0, 3, 1, 200, 15);
        this.deadImage = new ImageStack("dead_grass.png",
                0, 3, 10, 400, 0);
        this.startingDensity = InitialSeedDensities.grassStartingDensity; // per 100pixels^2
        this.maxSize = 20;
        this.maxBiomass = 100;
        this.startSize = 1;
        this.frameCoolDown = 10;

        // organism constants
        this.metabolismRate = 5f; // adjust to FPS and cool down
        this.decayRate = -500; // adjust to FPS and cool down
        this.growAtHealth = 50;
        this.maxGrowthRate = 15; // adjust to FPS and cool down
        this.reproduceAtSize = maxSize;
        this.reproduceAtHealth = 90;
        this.reproductionPenalty = 0.1f;
        this.maxOffsprings = 10;
        this.startHealth = 60;

        // plant constants
        this.dispersalRange = 30;
        this.maxShadeRange = 1.3f * this.maxSize / 2;
        this.shadePenalty = -10f; // adjust to FPS and cool down
        this.sproutTime = 1f; // adjust to FPS and cool down
    }
}
