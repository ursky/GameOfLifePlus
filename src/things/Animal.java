package things;
import constants.BlankConstants;
import constants.UiConstants;
import utilities.Random;
import world.World;


public class Animal extends Organism {
    float xVelocity = 0;
    float yVelocity = 0;
    float xAcceleration = 0;
    float yAcceleration = 0;

    public void move() {
        if (this.constants.maxSpeed == 0) {
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
        if (this.xVelocity > this.constants.maxSpeed) {
            this.xVelocity = this.constants.maxSpeed;
        }
        if (this.yVelocity > this.constants.maxSpeed) {
            this.yVelocity = this.constants.maxSpeed;
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
        this.xAcceleration = Random.randFloat(-this.constants.maxAcceleration, this.constants.maxAcceleration);
        this.yAcceleration = Random.randFloat(-this.constants.maxAcceleration, this.constants.maxAcceleration);
    }

    @Override
    public void live() {
        if (this.healthPercent > 0) {
            this.grow();
            this.reproduce();
            this.move();
        }
    }

    public Animal(float xPosition, float yPosition,
                  float size, World world, BlankConstants constants) {
        super(xPosition, yPosition, size, world, constants);
    }
}