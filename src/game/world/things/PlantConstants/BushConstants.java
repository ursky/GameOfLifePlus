package game.world.things.PlantConstants;

import game.world.things.Classes.CreatureConstants;
import game.constants.InitialSeedDensities;
import game.visuals.ImageStack;
import game.world.World;

import java.awt.*;

/**
 * Constants for the bush creature
 */
public final class BushConstants extends CreatureConstants {
    public BushConstants(World world) {
        super(world);

        // thing constants
        this.name = "Bush";
        this.type = "Plant";
        this.color = Color.cyan;

        this.mainImage = new ImageStack("bush.png",
                0, 10, 1, 300, 50);
        this.youngImage = new ImageStack("seed.png",
                0, 3, 1, 200, 15);
        this.deadImage = new ImageStack("bush.png",
                0, 3, 10, 300, 0);
        this.startingDensity = InitialSeedDensities.bushStartingDensity; // per 100pixels^2
        this.maxSize = 30;
        this.maxBiomass = 200;
        this.frameCoolDown = 10;
        this.startSize = 2;

        // organism constants
        this.metabolismRate = 5f; // adjust to FPS and cool down
        this.decayRate = -50; // adjust to FPS and cool down
        this.growAtHealth = 50;
        this.maxGrowthRate = 3.0f; // adjust to FPS and cool down
        this.reproduceAtSize = 0.9f * maxSize;
        this.reproduceAtHealth = 100;
        this.reproductionPenalty = 0.9f;
        this.reproductionCoolDown = 5;
        this.maxOffsprings = 3;
        this.startHealth = 50;

        // plant constants
        this.dispersalRange = 40;
        this.maxShadeRange = 1.1f * this.maxSize / 2;
        this.shadePenalty = -10f; // adjust to FPS and cool down
        this.sproutTime = 10f; // adjust to FPS and cool down
    }
}
