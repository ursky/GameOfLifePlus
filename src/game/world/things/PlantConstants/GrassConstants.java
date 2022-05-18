package game.world.things.PlantConstants;

import game.world.things.Classes.CreatureConstants;
import game.constants.InitialSeedDensities;
import game.visuals.ImageStack;
import game.world.World;

import java.awt.*;

/**
 * Constants for the grass creature
 */
public final class GrassConstants extends CreatureConstants {
    public GrassConstants(World world) {
        super(world);

        // thing constants
        this.name = "Grass";
        this.type = "Plant";
        this.color = Color.magenta;

        this.mainImage = new ImageStack("grass.png",
                0, 10, 1, 300, 0);
        this.youngImage = new ImageStack("seed.png",
                0, 3, 1, 100, 15);
        this.deadImage = new ImageStack("dead_grass.png",
                0, 3, 10, 200, 0);
        this.startingDensity = InitialSeedDensities.grassStartingDensity; // per 100pixels^2
        this.maxSize = 20;
        this.maxBiomass = 100;
        this.startSize = 1;
        this.frameCoolDown = 10;

        // organism constants
        this.metabolismRate = 8f; // adjust to FPS and cool down
        this.decayRate = -50; // adjust to FPS and cool down
        this.growAtHealth = 50;
        this.maxGrowthRate = 5; // adjust to FPS and cool down
        this.reproduceAtSize = maxSize;
        this.reproduceAtHealth = 90;
        this.reproductionPenalty = 0.1f;
        this.maxOffsprings = 3;
        this.startHealth = 5;

        // plant constants
        this.dispersalRange = 50;
        this.maxShadeRange = 1.3f * this.maxSize / 2;
        this.shadePenalty = -10f; // adjust to FPS and cool down
        this.sproutTime = 0f; // adjust to FPS and cool down
    }
}
