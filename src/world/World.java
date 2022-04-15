package world;

import constants.BushConstants;
import constants.UiConstants;
import constants.TreeConstants;
import things.*;
import quadsearch.Region;
import quadsearch.QuadTree;
import utilities.UpdateThingsThread;
import utilities.Random;

import java.util.ArrayList;

public class World {
    public Engine engine;
    public float playerPositionX = UiConstants.startPositionX;
    public float playerPositionY = UiConstants.startPositionY;
    public ArrayList<Thing> things = new ArrayList<>();
    public ArrayList<Thing> newThings = new ArrayList<>();
    public QuadTree thingCoordinates;
    public Region livingArea = new Region(0, 0, UiConstants.fullDimX, UiConstants.fullDimY);

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

    public void calcCoordinates() {
        thingCoordinates = new QuadTree(livingArea);
        for (int i = 0; i < things.size(); i++) {
            Thing thing = things.get(i);
            if (isCloseEnoughToUpdate(thing)) {
                quadsearch.Point point = new quadsearch.Point(i, thing.xPosition, thing.yPosition);
                thingCoordinates.addPoint(point);
            }
        }
    }

    public void updateThingsMultithreading() {
        if (this.engine.frameCounter < 10) { return; }
        ArrayList<UpdateThingsThread> threads = new ArrayList<>();
        int[][] positions = breakIntoChunks(this.things);
        for (int i=0; i<UiConstants.threads; i++) {
            UpdateThingsThread thread = new UpdateThingsThread(this.things, positions[i][0], positions[i][1]);
            threads.add(thread);
        }
        for (UpdateThingsThread thread: threads) { thread.start(); }
        for (UpdateThingsThread thread: threads) { thread.join(); }
    }

    private int[][] breakIntoChunks(ArrayList<Thing> arrayList) {
        int increment = arrayList.size() / UiConstants.threads;
        int[][] positions = new int[UiConstants.threads][2];
        for (int i=0; i<UiConstants.threads; i++) {
            int start = i * increment;
            int end = (i + 1) * increment;
            if (end > arrayList.size()) {
                end = arrayList.size();
            }
            positions[i][0] = start;
            positions[i][1] = end;
        }
        return positions;
    }

    private void removeDeadOrOffscreen() {
        ArrayList<Thing> livingThings = new ArrayList<>();
        for (Thing thing: this.things) {
            if (thing.healthPercent > 0) {
                int binX = this.engine.procedural.convertCoordinateToBin(thing.xPosition, UiConstants.fullDimX);
                int binY = this.engine.procedural.convertCoordinateToBin(thing.yPosition, UiConstants.fullDimY);
                if (this.engine.procedural.isRendered[binX][binY]) {
                    livingThings.add(thing);
                }
                else {
                    this.engine.procedural.archivedThings[binX][binY].add(thing);
                }
            }
        }
        this.things.clear();
        this.things = livingThings;
    }

    public boolean isCloseEnoughToUpdate(Thing thing) {
        return thing.calcDistanceTo(playerPositionX, playerPositionY) <= this.engine.loadRange;
    }

    public void updateWorld() {
        this.engine.timeUpdate("\ndisplay");

        this.calcCoordinates();
        this.engine.timeUpdate("calc coordinates");

        this.updateThingsMultithreading();
        this.engine.timeUpdate("update");

        this.things.addAll(this.newThings);
        this.newThings.clear();
        this.engine.timeUpdate("add new things");

        this.removeDeadOrOffscreen();
        this.engine.timeUpdate("remove dead");
    }

    World(Engine engine) {
        this.engine = engine;
    }
}
