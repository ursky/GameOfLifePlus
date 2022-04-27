package things.PlantConstants;

import constants.BlankConstants;
import constants.InitialSeedDensities;
import engine.visuals.ImageStack;
import engine.World;

public final class BushConstants extends BlankConstants {
    public BushConstants(World world) {
        super(world);

        // thing constants
        this.name = "Bush";
        this.type = "Plant";
        this.mainImage = new ImageStack("bush.png",
                0, 10, 1, 500, 50);
        this.youngImage = new ImageStack("seed.png",
                0, 3, 1, 200, 15);
        this.deadImage = new ImageStack("dead_tree.png",
                0, 3, 10, 500, 0);
        this.startingDensity = InitialSeedDensities.bushStartingDensity; // per 100pixels^2
        this.maxSize = 30;
        this.maxBiomass = 200;
        this.frameCoolDown = 10;
        this.startSize = 2;

        // organism constants
        this.metabolismRate = 5f; // adjust to FPS and cool down
        this.decayRate = -200; // adjust to FPS and cool down
        this.growAtHealth = 50;
        this.maxGrowthRate = 2.0f; // adjust to FPS and cool down
        this.reproduceAtSize = 0.9f * maxSize;
        this.reproduceAtHealth = 100;
        this.reproductionPenalty = 0.3f;
        this.maxOffsprings = 3;
        this.startHealth = 50;

        // plant constants
        this.dispersalRange = 40;
        this.maxShadeRange = 1.1f * this.maxSize / 2;
        this.shadePenalty = -10f; // adjust to FPS and cool down
        this.sproutTime = 10f; // adjust to FPS and cool down
    }
}
