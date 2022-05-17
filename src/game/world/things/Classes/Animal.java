package game.world.things.Classes;
import game.constants.UiConstants;
import game.quadsearch.Point;
import game.utilities.Random;
import game.world.World;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This class inherits from the generic Thing class and adds all the things an animal creature can do.
 * This includes movement, animation, hunting, etc.
 */
public class Animal extends Thing {
    float xVelocity = 0;
    float yVelocity = 0;
    float xAcceleration = Random.randFloat(-this.constants._maxAcceleration, this.constants._maxAcceleration);
    float yAcceleration = Random.randFloat(-this.constants._maxAcceleration, this.constants._maxAcceleration);
    float wobble = 0;
    int animationFrame = 0;
    boolean wobbleRight = true;
    Thing thingOfInterest;
    float distanceToInterest;
    ArrayList<Thing> disinterestedInThings = new ArrayList<>();
    boolean aimLess = true;

    /**
     * Update the speed and position of the creature based on the current acceleration direction
     */
    public void move() {
        if (this.healthPercent < 0) { return; }
        this.xVelocity += this.xAcceleration;
        this.yVelocity += this.yAcceleration;
        this.checkSpeedBounds();
        this.coordinate.setX(this.coordinate.getX() + this.xVelocity);
        this.coordinate.setY(this.coordinate.getY() + this.yVelocity);
        this.updateBin();
        this.updateRotation();
    }

    /**
     * Update where the creature is pointing based on where it is moving to
     */
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

    /**
     * Update the creature image based on what animation frame it is currently in
     */
    private void animateFrames() {
        this.animationFrame++;
        if (this.animationFrame >= this.constants.animationStack.orderedImageStacks.size()) {
            this.animationFrame = 0;
        }
        this.itemImage = this.constants.animationStack.orderedImageStacks.get(this.animationFrame).getImage(
                this.currentRotation, this.currentOpacity);
    }

    /**
     * This slightly changes the rotation of the animal left and right to mimic an animation.
     * This is a purely visual modification.
     */
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

    /**
     * Scale back the speed vectors of the creature to make sure they are in bounds of the maximum speed
     */
    private void checkSpeedBounds() {
        this.maxSpeed = this.constants._maxSpeed * this.relativeSize;
        this.maxSpeed = Math.max(this.maxSpeed, this.constants._maxSpeed * 2 / this.constants.maxSize);
        float vectorMax = Math.max(Math.abs(this.xVelocity), Math.abs(this.yVelocity));
        this.xVelocity *= this.maxSpeed / vectorMax;
        this.yVelocity *= this.maxSpeed / vectorMax;
    }

    /**
     * Scale back the acceleration vectors of the creature to make sure they are in bounds of the maximum acceleration
     */
    private void checkAccelerationBounds() {
        float vectorMax = Math.max(Math.abs(this.xAcceleration), Math.abs(this.yAcceleration));
        this.xAcceleration *= this.constants._maxAcceleration / vectorMax;
        this.yAcceleration *= this.constants._maxAcceleration / vectorMax;
    }

    /**
     * Animal decides where it would like to go
     */
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

    /**
     * Animal does not know where to go and wanders into a random direction
     */
    private void wander() {
        // step 1: introduce small % randomness to the acceleration vectors
        float range = this.constants._maxAcceleration * this.constants._wanderRandomness;
        this.xAcceleration += Random.randFloat(this.xAcceleration - range, this.xAcceleration + range);
        this.yAcceleration += Random.randFloat(this.yAcceleration - range, this.yAcceleration + range);
        // step 2: double down (to ensure max speed)
        this.xAcceleration *= 2;
        this.yAcceleration *= 2;
        this.checkAccelerationBounds();
    }

    /**
     * Adjust acceleration to move towards the point of interest (chase food)
     */
    private void goTowardsInterest() {
        this.xAcceleration = this.coordinate.getXDistanceTo(this.thingOfInterest.coordinate) - this.xVelocity;
        this.yAcceleration = this.coordinate.getYDistanceTo(this.thingOfInterest.coordinate) - this.yVelocity;
        this.checkAccelerationBounds();
    }

    /**
     * Adjust acceleration to move away from the point of interest (run away from predator)
     */
    private void goAwayFromInterest() {
        this.xAcceleration = -(this.coordinate.getXDistanceTo(this.thingOfInterest.coordinate)) - this.xVelocity;
        this.yAcceleration = -(this.coordinate.getYDistanceTo(this.thingOfInterest.coordinate)) - this.yVelocity;
        this.checkAccelerationBounds();
    }

    /**
     * Animal looks around for a point of interest
     * @return: did it find anything?
     */
    private boolean foundThingOfInterest() {
        // go after previous target
        if (!Objects.isNull(this.thingOfInterest) && this.thingOfInterest.healthPercent > 0
                && this.framesInExistence % 10 != 0) {
            this.distanceToInterest = this.calculateDistanceTo(this.thingOfInterest);
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
                    float distanceToThing = this.calculateDistanceTo(otherThing);
                    if (distanceToThing < this.distanceToInterest) {
                        this.thingOfInterest = otherThing;
                        this.distanceToInterest = distanceToThing;
                    }
                }
            }
            return this.distanceToInterest < UiConstants.fullDimX;
        }
    }

    /**
     * Check if another creature is this animal's food
     * @param potentialFood: another creature/thing
     * @return: is it edible?
     */
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

    /**
     * Check if another creature/ thing is interesting to this animal
     * @param otherThing: another creature/thing
     * @return: is it interesting?
     */
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

    /**
     * Check if another creature can eat this animal
     * @param otherThing: a potential predator
     * @return: is it a predator of this animal?
     */
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

    /**
     * Each a food source in eating range. This involves gradually reducing the food's biomass and converting it to
     * health of this animal.
     */
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

    /**
     * Special behavior of things capable of "evolving" - convert to a new creature type.
     */
    private void metamorphosis() {
        if (this.constants.metamorphosisIsLarvae && this.size >= this.constants.maxSize
                && this.healthPercent >= 100) {
            this.constants = this.constants.metamorphosisTo;
            this.isSeed = true;
            this.itemImage = this.constants.youngImage.getImage(this.currentRotation, this.currentOpacity);
            this.size = constants.startSize;
            this.biomass = constants.maxBiomass;
            this.coolDown = (int) (Math.random() * this.constants.sproutTime * this.coolDownFrames
                    * this.world.game.tracker.currentFPS);
        }
    }

    /**
     * Lay eggs on a food source and then ignore that food source forever
     */
    private void layEggsAndForget() {
        for (int i=0; i<this.constants.maxOffsprings; i++) {
            this.makeYoung();
        }
        this.disinterestedInThings.add(this.thingOfInterest);
        this.thingOfInterest = null;
    }

    /**
     * Live - do all the things an animal can do, such as eat, move, etc.
     */
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

    /**
     * Initialize animal
     * @param coordinate: position on 2d plane
     * @param size: current size
     * @param world: game world
     * @param constants: organism constants to use
     */
    public Animal(Point coordinate, float size, World world, CreatureConstants constants) {
        super(coordinate, size, world, constants);
    }
}