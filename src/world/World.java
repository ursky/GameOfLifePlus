package world;

import constants.BushConstants;
import constants.UiConstants;
import constants.TreeConstants;
import things.*;
import quadsearch.Region;
import quadsearch.QuadTree;
import utilities.Random;

import java.util.ArrayList;

public class World {
    public float playerPositionX = UiConstants.startPositionX;
    public float playerPositionY = UiConstants.startPositionY;
    public ArrayList<Thing> things = new ArrayList<>();
    public QuadTree thingCoordinates;
    Region livingArea = new Region(0, 0, UiConstants.fullDimX, UiConstants.fullDimY);
    public float currentFPS;

    private void initializeTrees(float minX, float minY, float maxX, float maxY) {
        int count = seedDensityToCount(TreeConstants.startingDensity, minX, minY, maxX, maxY);
        for (int i = 0; i<=count; i++) {
            float randX = Random.randFloat(minX, maxX);
            float randY = Random.randFloat(minY, maxY);
            float size = Random.randFloat(TreeConstants.maxSize / 2, TreeConstants.maxSize);
            Tree tree = new Tree(randX, randY, size, this);
            tree.healthPercent = Random.randFloat(20, 100);
            things.add(tree);
        }
    }

    private void initializeBushes(float minX, float minY, float maxX, float maxY) {
        int count = seedDensityToCount(BushConstants.startingDensity, minX, minY, maxX, maxY);
        for (int i = 0; i<=count; i++) {
            float randX = Random.randFloat(minX, maxX);
            float randY = Random.randFloat(minY, maxY);
            float size = Random.randFloat(BushConstants.maxSize / 2, BushConstants.maxSize);
            Bush bush = new Bush(randX, randY, size, this);
            bush.healthPercent = Random.randFloat(20, 100);
            things.add(bush);
        }
    }

    public void initializeInRange(float minX, float minY, float maxX, float maxY) {
        this.initializeTrees(minX, minY, maxX, maxY);
        this.initializeBushes(minX, minY, maxX, maxY);
    }

    private int seedDensityToCount(float densityPer100, float minX, float minY, float maxX, float maxY) {
        float area = (maxX - minX) * (maxY - minY);
        return (int) (densityPer100 * area / 100000);
    }

    public void updateCoordinates() {
        thingCoordinates = new QuadTree(livingArea);
        for (int i = 0; i < things.size(); i++) {
            Thing thing = things.get(i);
            if (isCloseEnoughToUpdate(thing)) {
                if (thing instanceof Tree) {
                    if (((Tree) thing).sproutEta > 0) {
                        continue;
                    }
                }
                quadsearch.Point point = new quadsearch.Point(i, thing.xPosition, thing.yPosition);
                thingCoordinates.addPoint(point);
            }
        }
    }

    public void updateThings() {
        ArrayList<Thing> newThings = new ArrayList<>();
        for (Thing thing : things) {
            if (thing instanceof Organism && isCloseEnoughToUpdate(thing)) {
                ArrayList<Organism> updatedThings = ((Organism) thing).live();
                newThings.addAll(updatedThings);
            }
            else {
                newThings.add(thing);
            }
        }
        things = newThings;
    }

    private boolean isCloseEnoughToUpdate(Thing thing) {
        return thing.calcDistanceTo(playerPositionX, playerPositionY) <= UiConstants.loadRange;
    }

    public void updateWorld() {
        updateCoordinates();
        updateThings();
    }

    World() {
        System.out.println("Creating the world...");
    }
}
