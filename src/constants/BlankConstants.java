package constants;

import engine.visuals.AnimationStack;
import engine.visuals.ImageStack;
import engine.World;

import java.awt.*;
import java.util.ArrayList;

public class BlankConstants {
    public World world;

    // thing constants
    public String name, type;
    public Color color;
    public ImageStack mainImage, youngImage, deadImage;
    public int maxOffsprings, frameCoolDown;
    public float startingDensity, maxSize, maxBiomass, metabolismRate, growAtHealth, decayRate,
            maxGrowthRate, reproduceAtSize, reproduceAtHealth, reproductionPenalty, reproductionCoolDown, startSize;
    public float dispersalRange, maxShadeRange, shadePenalty, sproutTime, startHealth;

    // animal variables
    public float maxSpeed, maxAcceleration, wobbleMaxDegree, wobbleSpeed, wanderRandomness;
    public float visionRange, minFoodSize, eatingRate, foodConversion;
    public ArrayList<String> foodNames = new ArrayList<>();
    public boolean eatsSeeds, asAdultOnlyLayEggs, flying, animate, metamorphosisIsAdult, metamorphosisIsLarvae;
    public AnimationStack animationStack;
    public BlankConstants metamorphosisFrom, metamorphosisTo;

    // variables
    public float _metabolismRate, _maxGrowthRate, _shadePenalty, _sproutTime, _decayRate;
    public float _maxSpeed, _maxAcceleration, _wanderRandomness, _eatingRate, _wobbleSpeed;

    public void updateRates() {
        this._metabolismRate = this.metabolismRate / this.world.engine.tracker.currentFPS;
        this._maxGrowthRate = this.maxGrowthRate / this.world.engine.tracker.currentFPS;
        this._shadePenalty = this.shadePenalty / this.world.engine.tracker.currentFPS;
        this._sproutTime = this.sproutTime / this.world.engine.tracker.currentFPS;
        this._decayRate = this.decayRate / this.world.engine.tracker.currentFPS;

        this._maxSpeed = this.maxSpeed / this.world.engine.tracker.currentFPS;
        this._wobbleSpeed = this.wobbleSpeed / this.world.engine.tracker.currentFPS;
        this._maxAcceleration = this.maxAcceleration / (this.world.engine.tracker.currentFPS
                * this.world.engine.tracker.currentFPS);
        this._wanderRandomness = this.wanderRandomness / this.world.engine.tracker.currentFPS;
        this._eatingRate = this.eatingRate / this.world.engine.tracker.currentFPS;
    }

    public BlankConstants(World world) {
        this.world = world;
    }
}
