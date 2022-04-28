package things.AnimalConstants;

import constants.BlankConstants;
import constants.InitialSeedDensities;
import engine.visuals.AnimationStack;
import engine.visuals.ImageStack;
import engine.World;

import java.util.ArrayList;
import java.util.Arrays;

public final class ButterflyConstants extends BlankConstants {
    public ButterflyConstants(World world) {
        super(world);

        // image and animation constants
        this.mainImage = new ImageStack("butterfly_1.png",
                0, 90, 1, 300, 10);
        this.youngImage = new ImageStack("pupae.png",
                0, 3, 1, 200, 15);
        this.deadImage = new ImageStack("butterfly_8.png",
                0, 3, 10, 300, 0);
        ArrayList<String> animationImages = new ArrayList<>(Arrays.asList(
                "butterfly_1.png", "butterfly_1.png", "butterfly_1.png", "butterfly_2.png", "butterfly_2.png",
                "butterfly_3.png", "butterfly_4.png", "butterfly_5.png",
                "butterfly_6.png", "butterfly_7.png", "butterfly_8.png", "butterfly_7.png", "butterfly_6.png",
                "butterfly_5.png", "butterfly_4.png", "butterfly_3.png",
                "butterfly_2.png", "butterfly_2.png", "butterfly_1.png"));
        this.animationStack = new AnimationStack(animationImages, 0, 90, 1,
                300, 10);

        // thing constants
        this.name = "Butterfly";
        this.type = "Animal";
        this.startingDensity = InitialSeedDensities.butterflyStartingDensity; // per 100pixels^2
        this.maxSize = 10;
        this.maxBiomass = 30; // adjust to size
        this.startSize = 5;
        this.frameCoolDown = 1;

        // growth constants
        this.metabolismRate = -5; // adjust to FPS and cool down
        this.decayRate = -300; // adjust to FPS and cool down
        this.growAtHealth = 10;
        this.maxGrowthRate = 100; // adjust to FPS and cool down

        // reproduction constants
        this.metamorphosisIsAdult = true;
        this.maxOffsprings = 1;
        this.reproduceAtSize = maxSize;
        this.reproduceAtHealth = 10;
        this.reproductionPenalty = 0.9f; // die after reproducing
        this.dispersalRange = 1f;
        this.startHealth = 100;
        this.reproductionCoolDown = 10;
        this.sproutTime = 5f; // adjust to FPS and cool down
        this.asAdultOnlyLayEggs = true;

        // movement constants
        this.maxSpeed = 15f; // adjust to FPS but not cool down
        this.maxAcceleration = 30f; // adjust to FPS^2 but not cool down
        this.wobbleMaxDegree = 0;
        this.wanderRandomness = 20f; // adjust to FPS but not cool down
        this.flying = true;
        this.animate = true;

        // foraging constants
        this.foodName = "Bush";
        this.visionRange = 50;
        this.minFoodSize = 1;
        this.eatsSeeds = false;
        this.eatingRate = 0; // biomass per second; adjust to FPS and cool down
        this.foodConversion = 0f; // biomass to health
    }
}
