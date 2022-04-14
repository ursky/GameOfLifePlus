package things;
import constants.UiConstants;
import world.World;

import java.util.ArrayList;


public class Animal extends Organism {
    float maxSpeed, maxAcceleration;
    float xVelocity = 0;
    float yVelocity = 0;
    float xAcceleration = 0;
    float yAcceleration = 0;

    public void move() {
        if (maxSpeed == 0) {
            return;
        }
        updateIntent();
        this.xVelocity += this.xAcceleration;
        this.yVelocity += this.yAcceleration;
        checkSpeedBounds();
        this.xPosition += this.xVelocity;
        this.yPosition += this.yVelocity;
    }

    private void checkSpeedBounds() {
        if (this.xVelocity > this.maxSpeed) {
            this.xVelocity = this.maxSpeed;
        }
        if (this.yVelocity > this.maxSpeed) {
            this.yVelocity = this.maxSpeed;
        }
        if (this.xPosition + this.xVelocity < 0
                || this.xPosition + this.xVelocity + this.size >= UiConstants.fullDimX) {
            this.xVelocity = 0;
        }
        if (this.yPosition + this.yVelocity < 0
                || this.yPosition + this.yVelocity + this.size >= UiConstants.fullDimY) {
            this.yVelocity = 0;
        }
    }

    public void updateIntent() {
        this.xAcceleration = randFloat(-this.maxAcceleration, this.maxAcceleration);
        this.yAcceleration = randFloat(-this.maxAcceleration, this.maxAcceleration);
    }

    @Override
    public ArrayList<Organism> live() {
        ArrayList<Organism> updatedCreatures = new ArrayList<>();
        if (this.healthPercent > 0) {
            updatedCreatures.add(this);
            this.grow();
            ArrayList<Organism> offsprings = reproduce();
            updatedCreatures.addAll(offsprings);
            this.move();
        }
        return updatedCreatures;
    }

    public Animal(float xPosition, float yPosition, float maxSpeed, float maxAcceleration, float size, World world) {
        super(xPosition, yPosition, size, world);
        this.maxSpeed = maxSpeed;
        this.maxAcceleration = maxAcceleration;
    }
}