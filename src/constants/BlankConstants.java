package constants;

import engine.ImageStack;
import world.World;

import javax.swing.*;
import java.awt.*;

public class BlankConstants {
    public World world;

    // thing constants
    public String name, type;
    public ImageStack mainImage, youngImage, deadImage;
    public int maxOffsprings, onScreenCoolDown, offScreenCoolDown;
    public float startingDensity, maxSize, metabolismRate, growAtHealth, decayRate,
            maxGrowthRate, reproduceAtSize, reproduceAtHealth, reproductionPenalty, startSize;
    public float dispersalRange, maxShadeRange, shadePenalty, sproutTime, startHealth;

    // animal variables
    public float maxSpeed, maxAcceleration;

    // variables
    public float _metabolismRate, _maxGrowthRate, _shadePenalty, _sproutTime, _decayRate;

    public void update() {
        this._metabolismRate = this.metabolismRate / this.world.engine.currentFPS;
        this._maxGrowthRate = this.maxGrowthRate / this.world.engine.currentFPS;
        this._shadePenalty = this.shadePenalty / this.world.engine.currentFPS;
        this._sproutTime = this.sproutTime / this.world.engine.currentFPS;
        this._decayRate = this.decayRate / this.world.engine.currentFPS;
    }

    public BlankConstants(World world) {
        this.world = world;
    }
}
