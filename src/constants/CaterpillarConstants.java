package constants;

import engine.AnimationStack;
import engine.ImageStack;
import world.World;

import java.util.ArrayList;
import java.util.Arrays;

public final class CaterpillarConstants extends BlankConstants {
    public CaterpillarConstants(World world) {
        super(world);

        // image and animation constants
        this.mainImage = new ImageStack("caterpillar_1.png",
                0, 90, 1, 300, 10);
        this.youngImage = new ImageStack("egg_spotted.png",
                0, 3, 1, 50, 15);
        this.deadImage = new ImageStack("splat.png",
                0, 3, 10, 300, 0);
        ArrayList<String> animationImages = new ArrayList<>(Arrays.asList(
                "caterpillar_1.png", "caterpillar_1.png", "caterpillar_1.png", "caterpillar_1.png",
                "caterpillar_2.png", "caterpillar_2.png", "caterpillar_2.png", "caterpillar_2.png",
                "caterpillar_3.png", "caterpillar_3.png", "caterpillar_3.png", "caterpillar_3.png",
                "caterpillar_2.png", "caterpillar_2.png", "caterpillar_2.png", "caterpillar_2.png",
                "caterpillar_1.png", "caterpillar_1.png", "caterpillar_1.png", "caterpillar_1.png"));
        this.animationStack = new AnimationStack(animationImages, -95, 90, 1,
                100, 0);

        // thing constants
        this.name = "Caterpillar";
        this.type = "Animal";
        this.startingDensity = 0; // per 100pixels^2
        this.maxSize = 6;
        this.maxBiomass = 50; // adjust to size
        this.startSize = 2;
        this.frameCoolDown = 1;

        // growth constants
        this.metabolismRate = -10; // adjust to FPS and cool down
        this.decayRate = -400; // adjust to FPS and cool down
        this.growAtHealth = 100;
        this.maxGrowthRate = 8f; // adjust to FPS and cool down

        // reproduction constants
        this.metamorphosisIsLarvae = true;
        this.startHealth = 25;
        this.reproduceAtSize = 1000; // intentionally impossible
        this.reproduceAtHealth = 1000; // intentionally impossible
        this.sproutTime = 5; // adjust to FPS

        // movement constants
        this.animate = true;
        this.maxSpeed = 4; // adjust to FPS but not cool down
        this.maxAcceleration = 6f; // adjust to FPS^2 but not cool down
        this.wobbleMaxDegree = 0;
        this.wobbleSpeed = 0; // adjust to FPS but not cool down
        this.wanderRandomness = 0.3f; // adjust to FPS but not cool down

        // foraging constants
        this.foodName = "Bush";
        this.visionRange = 20;
        this.minFoodSize = 1;
        this.eatsSeeds = false;
        this.eatingRate = 50; // biomass per second; adjust to FPS and cool down
        this.foodConversion = 0.9f; // biomass to health
    }
}
