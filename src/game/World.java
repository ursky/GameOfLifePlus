package game;

import game.constants.UiConstants;
import game.quadsearch.SearchAreas;
import game.world.InitThings;
import game.quadsearch.QuadTreeThread;
import game.world.ThingCounter;
import game.world.UpdateThingsThread;
import game.world.things.Classes.Animal;
import game.world.things.Classes.Thing;

import java.util.ArrayList;

public class World {
    public Engine engine;
    public ThingCounter counter;
    public ArrayList<Thing> things = new ArrayList<>();
    public ArrayList<Thing> newThings = new ArrayList<>();
    public SearchAreas quadTreeSearchGroups;
    public InitThings initThings;

    public void calcDistancesMultithreading() {
        this.quadTreeSearchGroups = new SearchAreas(UiConstants.threadCount);
        ArrayList<QuadTreeThread> threads = new ArrayList<>();
        for (int i = 0; i<UiConstants.threadCount; i++) {
            QuadTreeThread thread = new QuadTreeThread(this.things, i);
            threads.add(thread);
        }
        for (QuadTreeThread thread: threads) { thread.start(); }
        for (QuadTreeThread thread: threads) { thread.join(); }
    }

    public void updateThingsMultithreading() {
        if (this.engine.tracker.frameCounter < 1) { return; }
        ArrayList<UpdateThingsThread> threads = new ArrayList<>();
        int[][] positions = this.breakIntoChunks(this.things.size());
        for (int i = 0; i<UiConstants.threadCount; i++) {
            UpdateThingsThread thread = new UpdateThingsThread(this.things, positions[i][0], positions[i][1]);
            threads.add(thread);
        }
        for (UpdateThingsThread thread: threads) { thread.start(); }
        for (UpdateThingsThread thread: threads) { thread.join(); }
    }

    /**
     * Given the length of a data array and the number of CPUs, find where to break up the data to make uniform chunks
     * @param size: length of data
     * @return: positions to break data at
     */
    public int[][] breakIntoChunks(int size) {
        int increment = size / UiConstants.threadCount + 1;
        int[][] positions = new int[UiConstants.threadCount][2];
        for (int i = 0; i<UiConstants.threadCount; i++) {
            int start = i * increment;
            int end = (i + 1) * increment;
            if (start >= size) {
                break;
            }
            if (end > size) {
                end = size;
            }
            positions[i][0] = start;
            positions[i][1] = end;
        }
        return positions;
    }

    private void removeDeadOrOffscreen() {
        ArrayList<Thing> thingsInRange = new ArrayList<>();
        if (this.engine.tracker.timeToUpdateCounts()) {
            this.counter.initializeCounts();
        }
        for (Thing thing: this.things) {
            if (thing != null && thing.size > 0 && thing.currentOpacity > 0) {
                if (this.engine.tracker.timeToUpdateCounts()) {
                    this.counter.countThing(thing);
                }
                if (thing.isRendered()) {
                    thingsInRange.add(thing);
                }
                else {
                    // if animal moves out of range, it should be deleted to avoid piling up on border
                    if (!(thing instanceof Animal) || thing.isSeed || this.engine.userIO.movingCamera) {
                        this.engine.procedural.archivedThings[thing.xBin][thing.yBin].add(thing);
                    }
                }
            }
        }
        this.things.clear();
        this.things = thingsInRange;
    }

    public void updateWorld() {
        this.calcDistancesMultithreading();
        this.engine.tracker.printStepNanoseconds("QuadTree");

        this.updateThingsMultithreading();
        this.engine.tracker.printStepNanoseconds("Update engine.things");

        this.things.addAll(this.newThings);
        this.newThings.clear();
        this.engine.tracker.printStepNanoseconds("Add new engine.things");

        this.removeDeadOrOffscreen();
        this.engine.tracker.printStepNanoseconds("Remove dead");
    }

    public World(Engine engine) {
        this.initThings = new InitThings(this);
        this.engine = engine;
        this.counter = new ThingCounter(this);
    }
}
