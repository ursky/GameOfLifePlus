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
    public float _metabolismRate, _maxGrowthRate, _shadePenalty, _sproutTime, _decayRate, _reproductionCoolDown;
    public float _maxSpeed, _maxAcceleration, _wanderRandomness, _eatingRate, _wobbleSpeed, _hatchRate;

    public void updateRates() {
        float speedUp = 1;
        if (this.world.engine.userIO.fastForward()) {
            speedUp = UiConstants.fastForward;
        }
        this._metabolismRate = speedUp * this.metabolismRate / this.world.engine.tracker.currentFPS;
        this._maxGrowthRate = speedUp * this.maxGrowthRate / this.world.engine.tracker.currentFPS;
        this._shadePenalty = speedUp * this.shadePenalty / this.world.engine.tracker.currentFPS;
        this._sproutTime = speedUp * this.sproutTime / this.world.engine.tracker.currentFPS;
        this._decayRate = speedUp * this.decayRate / this.world.engine.tracker.currentFPS;

        this._maxSpeed = speedUp * this.maxSpeed / this.world.engine.tracker.currentFPS;
        this._wobbleSpeed = speedUp * this.wobbleSpeed / this.world.engine.tracker.currentFPS;
        this._maxAcceleration = speedUp * speedUp * this.maxAcceleration / (this.world.engine.tracker.currentFPS
                * this.world.engine.tracker.currentFPS);
        this._wanderRandomness = speedUp * this.wanderRandomness / this.world.engine.tracker.currentFPS;
        this._eatingRate = speedUp * this.eatingRate / this.world.engine.tracker.currentFPS;
        this._reproductionCoolDown = speedUp / this.world.engine.tracker.currentFPS;
        this._hatchRate = this.sproutTime * this.world.engine.tracker.currentFPS / speedUp;
    }

    public BlankConstants(World world) {
        this.world = world;
    }
}
