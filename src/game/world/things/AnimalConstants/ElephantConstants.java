package game.world.things.AnimalConstants;

import game.world.things.Classes.CreatureConstants;
import game.constants.InitialSeedDensities;
import game.visuals.ImageStack;
import game.world.World;

import java.awt.*;

/**
 * Constants for the elephant creature
 */
public final class ElephantConstants extends CreatureConstants {
    public ElephantConstants(World world) {
        super(world);

        // thing constants
        this.name = "Elephant";
        this.type = "Animal";
        this.color = Color.gray;

        this.mainImage = new ImageStack("elephant.png",
                0, 90, 1, 200, 0);
        this.youngImage = new ImageStack("elephant.png",
                0, 90, 1, 200, 0);
        this.deadImage = new ImageStack("dead_mouse.png",
                90, 30, 10, 100, 0);
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
        this.reproduceAtHealth = 100;
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
        this.foodNames.add("Grass");
        this.visionRange = 100;
        this.minFoodSize = 1;
        this.eatsSeeds = false;
        this.eatingRate = 100; // biomass per second; adjust to FPS and cool down
        this.foodConversion = 0.15f; // biomass to health
    }
}
