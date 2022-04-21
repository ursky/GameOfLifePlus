package constants;

import engine.ImageStack;
import world.World;

public final class BeetleConstants extends BlankConstants {
    public BeetleConstants(World world) {
        super(world);

        // thing constants
        this.name = "Beetle";
        this.type = "Animal";
        this.mainImage = new ImageStack("ladybug.png",
                -5, 90, 1, 400, 20);
        this.youngImage = new ImageStack("egg.png",
                0, 3, 1, 200, 15);
        this.deadImage = new ImageStack("splat.png",
                0, 3, 10, 400, 0);
        this.startingDensity = 0.1f; // per 100pixels^2
        this.maxSize = 10;
        this.maxBiomass = 50; // adjust to size
        this.startSize = 3;
        this.offScreenCoolDown = 1;
        this.onScreenCoolDown = 1;

        // growth constants
        this.metabolismRate = -5; // adjust to FPS and cool down
        this.decayRate = -200; // adjust to FPS and cool down
        this.growAtHealth = 100;
        this.maxGrowthRate = 10; // adjust to FPS and cool down

        // reproduction constants
        this.maxOffsprings = 1;
        this.reproduceAtSize = maxSize;
        this.reproduceAtHealth = 1000; // this is intentionally impossible - reproduce only when at tree
        this.reproductionPenalty = 0.8f;
        this.dispersalRange = 3;
        this.startHealth = 50;
        this.sproutTime = 10f; // adjust to FPS and cool down

        // movement constants
        this.maxSpeed = 10f; // adjust to FPS but not cool down
        this.maxAcceleration = 1f; // adjust to FPS
        this.wobbleMaxDegree = 8;
        this.wobbleSpeed = 3;
        this.wanderRandomness = 0.1f; // adjust to FPS

        // foraging constants
        this.foodName = "Tree";
        this.visionRange = 50;
        this.minFoodSize = 1;
        this.eatsSeeds = false;
        this.eatingRate = 30; // biomass per second; adjust to FPS
        this.foodConversion = 0.8f; // biomass to health
    }
}
