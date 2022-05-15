package things.Classes;
import constants.BlankConstants;
import constants.UiConstants;
import engine.utilities.Utils;
import engine.World;

import java.util.ArrayList;
import java.util.Objects;


public class Animal extends Thing {
    float xVelocity = 0;
    float yVelocity = 0;
    float xAcceleration = Utils.randFloat(-this.constants._maxAcceleration, this.constants._maxAcceleration);
    float yAcceleration = Utils.randFloat(-this.constants._maxAcceleration, this.constants._maxAcceleration);
    float wobble = 0;
    int flapFrame = 0;
    boolean wobbleRight = true;
    Thing thingOfInterest;
    float distanceToInterest;
    ArrayList<Thing> disinterestedInThings = new ArrayList<>();
    boolean aimLess = true;

    public void move() {
        if (this.healthPercent < 0) { return; }
        this.xVelocity += this.xAcceleration;
        this.yVelocity += this.yAcceleration;
        this.checkSpeedBounds();
        this.xPosition += this.xVelocity;
        this.yPosition += this.yVelocity;
        this.updateBin();
        this.updateRotation();
    }

    private void updateRotation() {
        // calculate current rotation from the velocity vectors and update this.currentRotation
        this.currentRotation = (float)(Math.atan(this.xVelocity / -this.yVelocity) * 180 / 3.1415926f);
        if (-this.yVelocity <= 0) {
            this.currentRotation += 180;
        }
        if (this.xVelocity < 0 && -this.yVelocity > 0) {
            this.currentRotation += 360;
        }
        this.wobble();
        if (this.constants.animate) {
            this.animateFrames();
        }
        else {
            // update the image being used
            this.itemImage = this.constants.mainImage.getImage(this.currentRotation, this.currentOpacity);
        }
    }

    private void animateFrames() {
        this.flapFrame++;
        if (this.flapFrame >= this.constants.animationStack.orderedImageStacks.size()) {
            this.flapFrame = 0;
        }
        this.itemImage = this.constants.animationStack.orderedImageStacks.get(this.flapFrame).getImage(
                this.currentRotation, this.currentOpacity);
    }

    private void wobble() {
        // eggs should not wobble
        if (this.isSeed) { return; }
        if (this.wobbleRight) {
            this.wobble += this.constants._wobbleSpeed;
        }
        else {
            this.wobble -= this.constants._wobbleSpeed;
        }
        if (this.wobble < -this.constants.wobbleMaxDegree) {
            this.wobbleRight = !this.wobbleRight;
            this.wobble = -this.constants.wobbleMaxDegree;
        }
        if (this.wobble > this.constants.wobbleMaxDegree) {
            this.wobbleRight = !this.wobbleRight;
            this.wobble = this.constants.wobbleMaxDegree;
        }
        this.currentRotation += this.wobble;
    }

    private void checkSpeedBounds() {
        this.maxSpeed = this.constants._maxSpeed * this.relativeSize;
        this.maxSpeed = Math.max(this.maxSpeed, this.constants._maxSpeed * 2 / this.constants.maxSize);
        float vectorMax = Math.max(Math.abs(this.xVelocity), Math.abs(this.yVelocity));
        this.xVelocity *= this.maxSpeed / vectorMax;
        this.yVelocity *= this.maxSpeed / vectorMax;
    }

    private void checkAccelerationBounds() {
        float vectorMax = Math.max(Math.abs(this.xAcceleration), Math.abs(this.yAcceleration));
        this.xAcceleration *= this.constants._maxAcceleration / vectorMax;
        this.yAcceleration *= this.constants._maxAcceleration / vectorMax;
    }

    private void updateIntent() {
        if (this.healthPercent < 100 && this.foundThingOfInterest()) {
            if (this.isPredator(this.thingOfInterest)) {
                this.goAwayFromInterest();
            }
            else {
                this.goTowardsInterest();
            }
        }
        else {
            this.wander();
        }
    }

    private void wander() {
        // step 1: introduce small % randomness to the acceleration vectors
        float range = this.constants._maxAcceleration * this.constants._wanderRandomness;
        this.xAcceleration += Utils.randFloat(this.xAcceleration - range, this.xAcceleration + range);
        this.yAcceleration += Utils.randFloat(this.yAcceleration - range, this.yAcceleration + range);
        // step 2: double down (to ensure max speed)
        this.xAcceleration *= 2;
        this.yAcceleration *= 2;
        this.checkAccelerationBounds();
    }

    private void goTowardsInterest() {
        this.xAcceleration = this.thingOfInterest.xPosition - this.xPosition - this.xVelocity;
        this.yAcceleration = this.thingOfInterest.yPosition - this.yPosition - this.yVelocity;
        this.checkAccelerationBounds();
    }

    private void goAwayFromInterest() {
        this.xAcceleration = -(this.thingOfInterest.xPosition - this.xPosition) - this.xVelocity;
        this.yAcceleration = -(this.thingOfInterest.yPosition - this.yPosition) - this.yVelocity;
        this.checkAccelerationBounds();
    }

    private boolean foundThingOfInterest() {
        // go after previous target
        if (!Objects.isNull(this.thingOfInterest) && this.thingOfInterest.healthPercent > 0
                && this.framesInExistence % 10 != 0) {
            this.distanceToInterest = this.calcDistance(this.xPosition, this.yPosition,
                    this.thingOfInterest.xPosition, this.thingOfInterest.yPosition);
            return true;
        }
        // search for a new target
        else {
            this.distanceToInterest = UiConstants.fullDimX;
            float visionRange = this.constants.visionRange * Math.max(0.25f, this.relativeSize);
            for (Thing otherThing: this.getThingsInRange(visionRange)) {
                if (isPredator(otherThing)) {
                    // predator alert - stop everything and run
                    this.thingOfInterest = otherThing;
                    return true;
                }
                if (isInteresting(otherThing)) {
                    float distanceToThing = this.calcDistance(this.xPosition, this.yPosition,
                            otherThing.xPosition, otherThing.yPosition);
                    if (distanceToThing < this.distanceToInterest) {
                        this.thingOfInterest = otherThing;
                        this.distanceToInterest = distanceToThing;
                    }
                }
            }
            return this.distanceToInterest < UiConstants.fullDimX;
        }
    }

    private boolean isFood(Thing potentialFood) {
        for (String foodName: this.constants.foodNames) {
            if (Objects.equals(potentialFood.constants.name, foodName)) {
                return (potentialFood.size >= this.constants.minFoodSize * this.relativeSize
                        && potentialFood.isSeed == this.constants.eatsSeeds
                        && potentialFood.healthPercent > 0
                        && potentialFood.maxSpeed < this.maxSpeed
                        && !(potentialFood.constants.flying && !potentialFood.isSeed));
            }
        }
        return false;
    }

    private boolean isInteresting(Thing otherThing) {
        if (this.isFood(otherThing)) {
            for (Thing uninterestingThing: this.disinterestedInThings) {
                if (uninterestingThing == otherThing) {
                    return false;
                }
            }
            return true;
        }
        else return otherThing.isSeed && this.constants.eatsSeeds
                && Objects.equals(otherThing.constants.type, "Plant");
    }

    private boolean isPredator(Thing otherThing) {
        if (otherThing.size < this.size) {
            return false;
        }
        for (String foodItEats: otherThing.constants.foodNames) {
            if (Objects.equals(foodItEats, this.constants.name)){
                return true;
            }
        }
        return false;
    }

    private void eat() {
        if (this.distanceToInterest <= 1 + this.size / 2 && this.healthPercent < 100
                && !Objects.isNull(this.thingOfInterest) && this.thingOfInterest.biomass > 0
                && this.isFood(this.thingOfInterest)) {
            // special behavior: lay egg when it finds food
            if (this.constants.asAdultOnlyLayEggs && this.size >= this.constants.reproduceAtSize) {
                this.layEggsAndForget();
                return;
            }

            float eatenBiomass = Math.min(this.thingOfInterest.biomass + 1,
                    this.constants._eatingRate * this.coolDownFrames);

            this.thingOfInterest.biomass -= eatenBiomass;
            this.healthPercent += eatenBiomass * this.constants.foodConversion;

            // kill the food item if it's all gone
            if (this.thingOfInterest.biomass <= 0) {
                this.thingOfInterest.coolDown = 0;
                this.thingOfInterest.killSelf();
                this.aimLess = true;
            }
            // lose interest in target if full
            if (this.healthPercent > 100) {
                this.thingOfInterest = null;
                this.aimLess = true;
            }
        }
    }

    private void metamorphosis() {
        if (this.constants.metamorphosisIsLarvae && this.size >= this.constants.maxSize
                && this.healthPercent >= 100) {
            this.constants = this.constants.metamorphosisTo;
            this.isSeed = true;
            this.itemImage = this.constants.youngImage.getImage(this.currentRotation, this.currentOpacity);
            this.size = constants.startSize;
            this.biomass = constants.maxBiomass;
            this.coolDown = (int) (Math.random() * this.constants.sproutTime * this.coolDownFrames
                    * this.world.engine.tracker.currentFPS);
        }
    }

    private void layEggsAndForget() {
        for (int i=0; i<this.constants.maxOffsprings; i++) {
            this.makeYoung();
        }
        this.disinterestedInThings.add(this.thingOfInterest);
        this.thingOfInterest = null;
    }

    @Override
    public void live() {
        this.framesInExistence++;
        this.metabolize();
        if (this.healthPercent <= 0) {return; }
        this.updateIntent();
        this.move();
        this.eat();
        this.reproduce();
        this.metamorphosis();
    }

    public Animal(float xPosition, float yPosition, float size, World world, BlankConstants constants) {
        super(xPosition, yPosition, size, world, constants);
    }
}