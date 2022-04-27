package things.PlantConstants;

import constants.BlankConstants;
import constants.InitialSeedDensities;
import engine.visuals.ImageStack;
import engine.World;

public final class TreeConstants extends BlankConstants {
    public TreeConstants(World world) {
        super(world);

        // thing constants
        this.name = "Tree";
        this.type = "Plant";
        this.mainImage = new ImageStack("tree.png",
                0, 10, 1, 1000, 20);
        this.youngImage = new ImageStack("tree_seed.png",
                0, 3, 1, 300, 20);
        this.deadImage = new ImageStack("dead_tree.png",
                0, 3, 10, 1000, 0);
        this.startingDensity = InitialSeedDensities.treeStartingDensity; // per 100pixels^2
        this.maxSize = 50;
        this.maxBiomass = 500;
        this.frameCoolDown = 10;
        this.startSize = 3;

        // organism constants
        this.metabolismRate = 15.0f; // adjust to FPS and cool down
        this.decayRate = -100; // adjust to FPS and cool down
        this.growAtHealth = 80;
        this.maxGrowthRate = 2.0f; // adjust to FPS and cool down
        this.reproduceAtSize = 0.8f * maxSize;
        this.reproduceAtHealth = 90;
        this.reproductionPenalty = 0.3f;
        this.maxOffsprings = 3;
        this.startHealth = 50;

        // plant constants
        this.dispersalRange = 40;
        this.maxShadeRange = 1.2f * this.maxSize / 2;
        this.shadePenalty = -30f; // adjust to FPS and cool down
        this.sproutTime = 1f; // adjust to FPS and cool down

        this.updateRates();
    }
}
