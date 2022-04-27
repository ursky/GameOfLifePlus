package things.AnimalConstants;

import constants.BlankConstants;
import constants.InitialSeedDensities;
import engine.visuals.ImageStack;
import engine.World;

public final class ElephantConstants extends BlankConstants {
    public ElephantConstants(World world) {
        super(world);

        // thing constants
        this.name = "Elephant";
        this.type = "Animal";
        this.mainImage = new ImageStack("elephant.png",
                0, 90, 1, 400, 0);
        this.youngImage = new ImageStack("elephant.png",
                0, 90, 1, 400, 0);
        this.deadImage = new ImageStack("dead_mouse.png",
                90, 5, 10, 200, 0);
        this.startingDensity = InitialSeedDensities.seedEaterStartingDensity; // per 100pixels^2
        this.maxSize = 15;
        this.maxBiomass = 100; // adjust to size
        this.startSize = 5;
        this.frameCoolDown = 1;

        // growth constants
        this.metabolismRate = -3; // adjust to FPS and cool down
        this.decayRate = -200; // adjust to FPS and cool down
        this.growAtHealth = 100;
        this.maxGrowthRate = 3; // adjust to FPS and cool down

        // reproduction constants
        this.maxOffsprings = 1;
        this.reproduceAtSize = maxSize;
        this.reproduceAtHealth = 100; // this is intentionally impossible - reproduce only when at tree
        this.reproductionPenalty = 0.5f;
        this.dispersalRange = 0.1f;
        this.startHealth = 50;
        this.sproutTime = 0f; // adjust to FPS and cool down


        // movement constants
        this.maxSpeed = 20f; // adjust to FPS but not cool down
        this.maxAcceleration = 120f; // adjust to FPS^2 but not cool down
        this.wobbleMaxDegree = 8;
        this.wobbleSpeed = 220; // adjust to FPS but not cool down
        this.wanderRandomness = 20f; // adjust to FPS but not cool down

        // foraging constants
        this.foodName = "Grass";
        this.visionRange = 100;
        this.minFoodSize = 1;
        this.eatsSeeds = false;
        this.eatingRate = 100; // biomass per second; adjust to FPS and cool down
        this.foodConversion = 0.15f; // biomass to health
    }
}
