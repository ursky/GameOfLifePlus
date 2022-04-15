package constants;

import world.World;

import java.awt.*;

public class ThingConstants {
    public World world;

    // thing constants
    public String name, type, imagePath;
    public Image image;
    public int startingDensity, maxOffsprings;
    public float maxSize, minSizeToShow, maxCoolDownFrames, metabolismRate, growAtHealth, maxGrowthRate,
            reproduceAtSize, reproduceAtHealth, reproductionPenalty;
    public float dispersalRange, maxShadeRange, shadePenalty, sproutTime;

    // animal variables
    public float maxSpeed, maxAcceleration;

    // variables
    public float _metabolismRate, _maxGrowthRate, _shadePenalty, _sproutTime;

    public void update() {
        this._metabolismRate = this.metabolismRate / this.world.engine.currentFPS;
        this._maxGrowthRate = this.maxGrowthRate / this.world.engine.currentFPS;
        this._shadePenalty = this.shadePenalty / this.world.engine.currentFPS;
        this._sproutTime = this.sproutTime / this.world.engine.currentFPS;
    }

    public ThingConstants(World world) {
        this.world = world;
    }
}
