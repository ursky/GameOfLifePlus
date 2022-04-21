package world;

import constants.*;
import things.Animal;
import things.Plant;
import things.Thing;
import utilities.Random;

import java.util.ArrayList;
import java.util.Objects;

public class InitThings {
    public World world;
    public ArrayList<BlankConstants> orderedBlankConstants = new ArrayList<>();

    private void initializeThings(float minX, float minY, float maxX, float maxY, BlankConstants constants) {
        int count = seedDensityToCount(constants.startingDensity, minX, minY, maxX, maxY);
        for (int i = 0; i<count; i++) {
            float randX = Random.randFloat(minX, maxX);
            float randY = Random.randFloat(minY, maxY);
            float size = Random.randFloat(constants.maxSize / 2, constants.maxSize);
            createThing(randX, randY, size, constants);
        }
    }

    private void createThing(float randX, float randY, float size, BlankConstants constants) {
        if (constants.type.equals("Plant")) {
            Plant thing = new Plant(randX, randY, size, this.world, constants);
            initThing(thing);
        } else if (constants.type.equals("Animal")) {
            Animal thing = new Animal(randX, randY, size, this.world, constants);
            initThing(thing);
        } else {
            Thing Thing = new Thing(randX, randY, size, this.world, constants);
            initThing(Thing);
        }
    }

    private void initThing(Thing thing) {
        float health = Random.randFloat(20, 100);
        thing.currentRotation = Random.randFloat(0, 360);
        thing.currentOpacity = 255;
        thing.initImage(thing.constants.mainImage);
        thing.healthPercent = health;
        this.world.things.add(thing);
    }

    public void initPlants(float minX, float minY, float maxX, float maxY) {
        for (BlankConstants blankConstants : this.orderedBlankConstants) {
            if (Objects.equals(blankConstants.type, "Plant")) {
                this.initializeThings(minX, minY, maxX, maxY, blankConstants);
            }
        }
    }

    public void initAnimals(float minX, float minY, float maxX, float maxY) {
        for (BlankConstants blankConstants : this.orderedBlankConstants) {
            if (Objects.equals(blankConstants.type, "Animal")) {
                this.initializeThings(minX, minY, maxX, maxY, blankConstants);
            }
        }
    }

    public ArrayList<Thing> copyThings(float minX, float minY, float maxX, float maxY) {
        ArrayList<Thing> copiedThings = new ArrayList<>();
        float[] copyRange = selectRangeToCopy(maxX - minX, maxY - minY);
        for (Thing thing: this.world.things) {
            if (thing.xPosition >= copyRange[0] && thing.xPosition < copyRange[1]
                    && thing.yPosition >= copyRange[2] && thing.yPosition < copyRange[3]) {
                float newXPos = minX + (thing.xPosition - copyRange[0]);
                float newYPos = minY + (thing.yPosition - copyRange[2]);
                Thing newThing = copyThingTo(thing, newXPos, newYPos);
                copiedThings.add(newThing);
            }
        }
        return copiedThings;
    }

    private float[] selectRangeToCopy(float widthX, float widthY) {
        float copyStartX = Random.randFloat(this.world.engine.positionsInView[0],
                this.world.engine.positionsInView[1] - widthX);
        float copyEndX = copyStartX + widthX;
        float copyStartY = Random.randFloat(this.world.engine.positionsInView[2],
                this.world.engine.positionsInView[3] - widthY);
        float copyEndY = copyStartY + widthY;
        return new float[] {copyStartX, copyEndX, copyStartY, copyEndY};
    }

    private Thing copyThingTo(Thing thing, float newXPos, float newYPos) {
        Thing newThing = thing.makeClone();
        newThing.xPosition = newXPos;
        newThing.yPosition = newYPos;
        newThing.updateBin();
        return newThing;
    }

    private int seedDensityToCount(float densityPer1000, float minX, float minY, float maxX, float maxY) {
        float area = (maxX - minX) * (maxY - minY);
        float expectedCount = densityPer1000 * area / 10000;
        int countInt = (int)expectedCount;
        float remainder = expectedCount - countInt;
        if (Math.random() < remainder) {
            countInt++;
        }
        return countInt;
    }

    public void updateConstants() {
        for (BlankConstants constants: this.orderedBlankConstants) {
            constants.updateRates();
        }
    }

    public float getBiggestSize() {
        float biggest = 0;
        for (BlankConstants constants: this.orderedBlankConstants) {
            if (constants.maxSize > biggest) {
                biggest = constants.maxSize;
            }
        }
        return biggest;
    }

    public InitThings(World world) {
        this.world = world;

        BlankConstants grassConstants = new GrassConstants(this.world);
        this.orderedBlankConstants.add(grassConstants);

        BlankConstants bushConstants = new BushConstants(this.world);
        this.orderedBlankConstants.add(bushConstants);

        BlankConstants treeConstants = new TreeConstants(this.world);
        this.orderedBlankConstants.add(treeConstants);

        BlankConstants beetleConstants = new BeetleConstants(this.world);
        this.orderedBlankConstants.add(beetleConstants);
    }
}
