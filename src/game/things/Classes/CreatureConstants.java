package game.things.Classes;

import game.userIO.UiConstants;
import game.visuals.AnimationStack;
import game.visuals.ImageStack;
import game.World;

import java.awt.*;
import java.util.ArrayList;

public class CreatureConstants {
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
    public CreatureConstants metamorphosisFrom, metamorphosisTo;

    // variables
    public float _metabolismRate, _maxGrowthRate, _shadePenalty, _sproutTime, _decayRate, _reproductionCoolDown;
    public float _maxSpeed, _maxAcceleration, _wanderRandomness, _eatingRate, _wobbleSpeed, _hatchRate;

    public void updateRates() {
        float speedUp = 1 / this.world.engine.tracker.currentFPS;
        if (this.world.engine.userIO.fastForward()) {
            speedUp = Math.min(UiConstants.fastForward * speedUp, UiConstants.maxRateFactor);
        }
        this._metabolismRate = speedUp * this.metabolismRate;
        this._maxGrowthRate = speedUp * this.maxGrowthRate;
        this._shadePenalty = speedUp * this.shadePenalty;
        this._sproutTime = speedUp * this.sproutTime;
        this._decayRate = speedUp * this.decayRate;

        this._maxSpeed = speedUp * this.maxSpeed;
        this._wobbleSpeed = speedUp * this.wobbleSpeed;
        this._maxAcceleration = speedUp * speedUp * this.maxAcceleration;
        this._wanderRandomness = speedUp * this.wanderRandomness;
        this._eatingRate = speedUp * this.eatingRate;
        this._reproductionCoolDown = speedUp;
        this._hatchRate = this.sproutTime / speedUp;
    }

    public CreatureConstants(World world) {
        this.world = world;
    }
}
