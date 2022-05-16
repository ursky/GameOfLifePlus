package things.AnimalConstants;

import things.Classes.CreatureConstants;
import things.Classes.InitialSeedDensities;
import engine.visuals.ImageStack;
import engine.World;

import java.awt.*;

public final class BeetleConstants extends CreatureConstants {
    public BeetleConstants(World world) {
        super(world);

        // thing constants
        this.name = "Beetle";
        this.type = "Animal";
        this.color = Color.red;
        this.mainImage = new ImageStack("ladybug.png",
                -5, 90, 1, 200, 20);
        this.youngImage = new ImageStack("egg_spotted.png",
                0, 3, 1, 200, 15);
        this.deadImage = new ImageStack("splat.png",
                0, 3, 10, 200, 0);
        this.startingDensity = InitialSeedDensities.beetleStartingDensity; // per 100pixels^2
        this.maxSize = 10;
        this.maxBiomass = 50; // adjust to size
        this.startSize = 3;
        this.frameCoolDown = 1;

        // growth constants
        this.metabolismRate = -4; // adjust to FPS and cool down
        this.decayRate = -400; // adjust to FPS and cool down
        this.growAtHealth = 100;
        this.maxGrowthRate = 3; // adjust to FPS and cool down

        // reproduction constants
        this.maxOffsprings = 1;
        this.reproduceAtSize = maxSize;
        this.reproduceAtHealth = 1000; // this is intentionally impossible - reproduce only when at tree
        this.reproductionPenalty = 0.9f;
        this.dispersalRange = 0.1f;
        this.startHealth = 10;
        this.sproutTime = 5f; // adjust to FPS and cool down
        this.asAdultOnlyLayEggs = true;

        // movement constants
        this.maxSpeed = 10f; // adjust to FPS but not cool down
        this.maxAcceleration = 60f; // adjust to FPS^2 but not cool down
        this.wobbleMaxDegree = 8;
        this.wobbleSpeed = 180; // adjust to FPS but not cool down
        this.wanderRandomness = 5f; // adjust to FPS but not cool down

        // foraging constants
        this.foodNames.add("Tree");
        this.visionRange = 30;
        this.minFoodSize = 5;
        this.eatsSeeds = false;
        this.eatingRate = 50; // biomass per second; adjust to FPS and cool down
        this.foodConversion = 0.8f; // biomass to health
    }
}
