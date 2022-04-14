package world;

import constants.BushConstants;
import constants.UiConstants;
import constants.TreeConstants;
import things.Organism;
import things.Thing;
import things.Tree;
import things.Bush;
import quadsearch.Region;
import quadsearch.QuadTree;

import java.util.ArrayList;

public class World {
    public float playerPositionX = UiConstants.startPositionX;
    public float playerPositionY = UiConstants.startPositionY;
    public ArrayList<Thing> things = new ArrayList<>();
    public QuadTree thingCoordinates;
    Region livingArea = new Region(0, 0, UiConstants.fieldDimX, UiConstants.fieldDimY);

    World() {
        System.out.println("Creating the world...");
        initializeTrees();
        initializeBushes();
    }

    public void initializeTrees() {
        for (int i = 0; i< TreeConstants.startingCount; i++) {
            float randX = (float) Math.random() * UiConstants.fieldDimX;
            float randY = (float) Math.random() * UiConstants.fieldDimY;
            float size = (float) Math.random() * TreeConstants.maxSize;
            Tree tree = new Tree(randX, randY, size, this);
            things.add(tree);
        }
    }

    public void initializeBushes() {
        for (int i = 0; i< BushConstants.startingCount; i++) {
            float randX = (float) Math.random() * UiConstants.fieldDimX;
            float randY = (float) Math.random() * UiConstants.fieldDimY;
            float size = (float) Math.random() * BushConstants.maxSize;
            Bush bush = new Bush(randX, randY, size, this);
            things.add(bush);
        }
    }

    public void updateCoordinates() {
        thingCoordinates = new QuadTree(livingArea);
        for (int i = 0; i < things.size(); i++) {
            Thing thing = things.get(i);
            if (isCloseEnoughToUpdate(thing)) {
                quadsearch.Point point = new quadsearch.Point(i, thing.xPosition, thing.yPosition);
                thingCoordinates.addPoint(point);
            }
        }
    }

    public void updateThings() {
        // TODO: i make a new large array every time...
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
        return thing.calcDistanceTo(playerPositionX, playerPositionY) <= UiConstants.interactionRange;
    }

    public void updateWorld() {
        updateCoordinates();
        updateThings();
    }
}
