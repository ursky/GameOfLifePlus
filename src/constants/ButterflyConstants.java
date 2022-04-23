package constants;

import engine.AnimationStack;
import engine.ImageStack;
import world.World;

import java.util.ArrayList;
import java.util.Arrays;

public final class ButterflyConstants extends BlankConstants {
    public ButterflyConstants(World world) {
        super(world);

        // image and animation constants
        this.mainImage = new ImageStack("butterfly_1.png",
                -5, 90, 1, 400, 10);
        this.youngImage = new ImageStack("egg.png",
                0, 3, 1, 200, 15);
        this.deadImage = new ImageStack("splat.png",
                0, 3, 10, 400, 0);
        ArrayList<String> animationImages = new ArrayList<>(Arrays.asList(
                "butterfly_1.png", "butterfly_1.png", "butterfly_1.png", "butterfly_2.png", "butterfly_2.png",
                "butterfly_3.png", "butterfly_4.png", "butterfly_5.png",
                "butterfly_6.png", "butterfly_7.png", "butterfly_8.png", "butterfly_9.png", "butterfly_10.png",
                "butterfly_11.png", "butterfly_12.png", "butterfly_13.png",
                "butterfly_14.png", "butterfly_14.png", "butterfly_15.png"));
        this.animationStack = new AnimationStack(animationImages, -5, 90, 1,
                400, 10);

        // thing constants
        this.name = "Butterfly";
        this.type = "Animal";
        this.startingDensity = InitialSeedDensities.butterflyStartingDensity; // per 100pixels^2
        this.maxSize = 10;
        this.maxBiomass = 30; // adjust to size
        this.startSize = 1;
        this.frameCoolDown = 1;

        // growth constants
        this.metabolismRate = -10; // adjust to FPS and cool down
        this.decayRate = -200; // adjust to FPS and cool down
        this.growAtHealth = 10;
        this.maxGrowthRate = 5; // adjust to FPS and cool down

        // reproduction constants
        this.maxOffsprings = 5;
        this.reproduceAtSize = maxSize;
        this.reproduceAtHealth = 100;
        this.reproductionPenalty = -1f; // die after reproducing
        this.dispersalRange = 1f;
        this.startHealth = 50;
        this.sproutTime = 10f; // adjust to FPS and cool down
        this.asAdultOnlyLayEggs = false;

        // movement constants
        this.maxSpeed = 15f; // adjust to FPS but not cool down
        this.maxAcceleration = 60f; // adjust to FPS^2 but not cool down
        this.wobbleMaxDegree = 0;
        this.wobbleSpeed = 180; // adjust to FPS but not cool down
        this.wanderRandomness = 0.3f; // adjust to FPS but not cool down
        this.flying = true;

        // foraging constants
        this.foodName = "Bush";
        this.visionRange = 100;
        this.minFoodSize = 10;
        this.eatsSeeds = false;
        this.eatingRate = 60; // biomass per second; adjust to FPS and cool down
        this.foodConversion = 0.5f; // biomass to health
    }
}
