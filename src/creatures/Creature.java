package creatures;
import constants.Constants;
import quadsearch.Point;
import quadsearch.QuadTree;
import quadsearch.Region;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Creature {
    public float size;
    public float maxSize, growthRate;
    public Image creatureImage;

    public float xPosition, yPosition;
    float maxSpeed, maxAcceleration;
    float xVelocity = 0;
    float yVelocity = 0;
    float xAcceleration = 0;
    float yAcceleration = 0;

    public static float randFloat(float min, float max) {
        Random rand = new Random();
        return rand.nextFloat() * (max - min) + min;
    }

    public void move() {
        if (maxSpeed == 0) {
            return;
        }
        updateIntent();
        this.xVelocity += this.xAcceleration;
        this.yVelocity += this.yAcceleration;
        checkBounds();
        this.xPosition += this.xVelocity;
        this.yPosition += this.yVelocity;
    }

    public void grow() {
        if (this.size < this.maxSize) {
            this.size += this.growthRate;
        }
    }

    private void checkBounds() {
        if (this.xVelocity > this.maxSpeed) {
            this.xVelocity = this.maxSpeed;
        }

        if (this.yVelocity > this.maxSpeed) {
            this.yVelocity = this.maxSpeed;
        }
        if (this.xPosition + this.xVelocity < 0
                || this.xPosition + this.xVelocity + this.size >= Constants.panelDimX) {
            this.xVelocity = 0;
        }
        if (this.yPosition + this.yVelocity < 0
                || this.yPosition + this.yVelocity + this.size >= Constants.panelDimY) {
            this.yVelocity = 0;
        }
    }

    public void updateIntent() {
        this.xAcceleration = randFloat(-this.maxAcceleration, this.maxAcceleration);
        this.yAcceleration = randFloat(-this.maxAcceleration, this.maxAcceleration);
    }

    public ArrayList<Creature> live() {
        ArrayList<Creature> updatedCreatures = new ArrayList<>();
        if (this.size > 0) {
            updatedCreatures.add(this);
            move();
            grow();

            if (this instanceof Tree) {
                ArrayList<Tree> offsprings = ((Tree) this).spreadSeeds();
                updatedCreatures.addAll(offsprings);

            }
            else {
                ArrayList<Creature> offsprings = reproduce();
                updatedCreatures.addAll(offsprings);
            }
        }
        return updatedCreatures;
    }

    public ArrayList<Creature> reproduce() {
        // this is just a placeholder method
        return new ArrayList<>();
    }

    public void interact(ArrayList<Creature> creatures, QuadTree creatureCoordinates) {
        if (this instanceof Tree) {
            ((Tree) this).shadeOthers(creatures, creatureCoordinates);
        }
    }

    public List<Point> getCreaturesInRange(float radius, QuadTree creatureCoordinates) {
        Region searchArea = new Region(this.xPosition - radius,
                this.yPosition - radius,
                this.xPosition + radius,
                this.yPosition + radius);
        return creatureCoordinates.search(searchArea, null);
    }

    public Creature(float xPosition, float yPosition, float maxSpeed, float maxAcceleration, float size) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.maxSpeed = maxSpeed;
        this.maxAcceleration = maxAcceleration;
        this.size = size;
    }
}