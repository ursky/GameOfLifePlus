package world;

import constants.UiConstants;
import engine.Engine;
import quadsearch.SearchAreas;
import things.*;

import java.util.ArrayList;

public class World {
    public Engine engine;
    public float playerPositionX = UiConstants.startPositionX;
    public float playerPositionY = UiConstants.startPositionY;
    public ArrayList<Thing> things = new ArrayList<>();
    public ArrayList<Thing> newThings = new ArrayList<>();
    public SearchAreas searchAreas;
    public InitThings initThings;

    public void calcDistancesMultithreading() {
        this.searchAreas = new SearchAreas(UiConstants.threadCount);
        ArrayList<QuadTreeThread> threads = new ArrayList<>();
        for (int i = 0; i<UiConstants.threadCount; i++) {
            QuadTreeThread thread = new QuadTreeThread(this.things, i);
            threads.add(thread);
        }
        for (QuadTreeThread thread: threads) { thread.start(); }
        for (QuadTreeThread thread: threads) { thread.join(); }
    }

    public void updateThingsMultithreading() {
        if (this.engine.frameCounter < 30) { return; }
        ArrayList<UpdateThingsThread> threads = new ArrayList<>();
        int[][] positions = breakIntoChunks(this.things);
        for (int i = 0; i<UiConstants.threadCount; i++) {
            UpdateThingsThread thread = new UpdateThingsThread(this.things, positions[i][0], positions[i][1]);
            threads.add(thread);
        }
        for (UpdateThingsThread thread: threads) { thread.start(); }
        for (UpdateThingsThread thread: threads) { thread.join(); }
    }

    private int[][] breakIntoChunks(ArrayList<Thing> arrayList) {
        int increment = arrayList.size() / UiConstants.threadCount;
        int[][] positions = new int[UiConstants.threadCount][2];
        for (int i = 0; i<UiConstants.threadCount; i++) {
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
        ArrayList<Thing> thingsInRange = new ArrayList<>();
        for (Thing thing: this.things) {
            if (thing != null && thing.size > 0 && thing.currentOpacity > 0) {
                if (thing.isRendered()) {
                    thingsInRange.add(thing);
                }
                else {
                    // if animal moves out of range, it should be deleted to avoid piling up on border
                    if (!(thing instanceof Animal) || thing.isSeed || this.engine.movingCamera) {
                        this.engine.procedural.archivedThings[thing.xBin][thing.yBin].add(thing);
                    }
                }
            }
        }
        this.things.clear();
        this.things = thingsInRange;
    }

    public void updateWorld() {
        this.engine.timeUpdate("\nPaint");

        this.calcDistancesMultithreading();
        this.engine.timeUpdate("QuadTree");

        this.updateThingsMultithreading();
        this.engine.timeUpdate("Update things");

        this.things.addAll(this.newThings);
        this.newThings.clear();
        this.engine.timeUpdate("Add new things");

        this.removeDeadOrOffscreen();
        this.engine.timeUpdate("Remove dead");
    }

    public World(Engine engine) {
        this.engine = engine;
        this.initThings = new InitThings(this);
    }
}
