package game.world.things.AnimalConstants;

import game.world.things.Classes.CreatureConstants;
import game.constants.InitialSeedDensities;
import game.world.World;
import game.visuals.ImageStack;

import java.awt.*;

/**
 * Constants for the turtle creature
 */
public final class TurtleConstants extends CreatureConstants {
    public TurtleConstants(World world) {
        super(world);

        // thing constants
        this.name = "Turtle";
        this.type = "Animal";
        this.color = Color.green;

        this.mainImage = new ImageStack("turtle.png",
                0, 90, 1, 200, 0);
        this.youngImage = new ImageStack("egg.png",
                0, 10, 1, 200, 0);
        this.deadImage = new ImageStack("dead_turtle.png",
                0, 30, 10, 100, 0);
        this.startingDensity = InitialSeedDensities.turtleStartingDensity; // per 100pixels^2
        this.maxSize = 15;
        this.maxBiomass = 100; // adjust to size
        this.startSize = 7;
        this.frameCoolDown = 1;

        // growth constants
        this.metabolismRate = -2.5f; // adjust to FPS and cool down
        this.decayRate = -200; // adjust to FPS and cool down
        this.growAtHealth = 100;
        this.maxGrowthRate = 3; // adjust to FPS and cool down

        // reproduction constants
        this.maxOffsprings = 1;
        this.reproduceAtSize = maxSize;
        this.reproduceAtHealth = 100;
        this.reproductionPenalty = 0.3f;
        this.dispersalRange = 0.1f;
        this.startHealth = 60;
        this.sproutTime = 20f; // adjust to FPS and cool down

        // movement constants
        this.maxSpeed = 18f; // adjust to FPS but not cool down
        this.maxAcceleration = 100f; // adjust to FPS^2 but not cool down
        this.wobbleMaxDegree = 8;
        this.wobbleSpeed = 220; // adjust to FPS but not cool down
        this.wanderRandomness = 10f; // adjust to FPS but not cool down

        // foraging constants
        this.foodNames.add("Beetle");
        this.foodNames.add("Caterpillar");
        this.foodNames.add("Butterfly");
        this.visionRange = 100;
        this.minFoodSize = 5;
        this.eatingRate = 25; // biomass per second; adjust to FPS and cool down
        this.foodConversion = 0.8f; // biomass to health
    }
}
