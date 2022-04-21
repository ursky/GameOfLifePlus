package constants;

import engine.ImageStack;
import world.World;

public class BlankConstants {
    public World world;

    // thing constants
    public String name, type;
    public ImageStack mainImage, youngImage, deadImage;
    public int maxOffsprings, frameCoolDown;
    public float startingDensity, maxSize, maxBiomass, metabolismRate, growAtHealth, decayRate,
            maxGrowthRate, reproduceAtSize, reproduceAtHealth, reproductionPenalty, reproductionCoolDown, startSize;
    public float dispersalRange, maxShadeRange, shadePenalty, sproutTime, startHealth;

    // animal variables
    public float maxSpeed, maxAcceleration, wobbleMaxDegree, wobbleSpeed, wanderRandomness;
    public float visionRange, minFoodSize, eatingRate, foodConversion;
    public String foodName;
    public boolean eatsSeeds, asAdultOnlyLayEggs;

    // variables
    public float _metabolismRate, _maxGrowthRate, _shadePenalty, _sproutTime, _decayRate;
    public float _maxSpeed, _maxAcceleration, _wanderRandomness, _eatingRate, _wobbleSpeed;

    public void updateRates() {
        this._metabolismRate = this.metabolismRate / this.world.engine.currentFPS;
        this._maxGrowthRate = this.maxGrowthRate / this.world.engine.currentFPS;
        this._shadePenalty = this.shadePenalty / this.world.engine.currentFPS;
        this._sproutTime = this.sproutTime / this.world.engine.currentFPS;
        this._decayRate = this.decayRate / this.world.engine.currentFPS;

        this._maxSpeed = this.maxSpeed / this.world.engine.currentFPS;
        this._wobbleSpeed = this.wobbleSpeed / this.world.engine.currentFPS;
        this._maxAcceleration = this.maxAcceleration / (this.world.engine.currentFPS * this.world.engine.currentFPS);
        this._wanderRandomness = this.wanderRandomness / this.world.engine.currentFPS;
        this._eatingRate = this.eatingRate / this.world.engine.currentFPS;
    }

    public BlankConstants(World world) {
        this.world = world;
    }
}
