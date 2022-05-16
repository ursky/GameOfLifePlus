package engine;

import engine.userIO.UiConstants;
import engine.quadsearch.SearchAreas;
import engine.utilities.Utils;
import engine.world.InitThings;
import engine.quadsearch.QuadTreeThread;
import engine.world.ThingCounter;
import engine.world.UpdateThingsThread;
import things.Classes.Animal;
import things.Classes.Thing;

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
        if (this.engine.tracker.frameCounter < 30) { return; }
        ArrayList<UpdateThingsThread> threads = new ArrayList<>();
        int[][] positions = Utils.breakIntoChunks(this.things.size());
        for (int i = 0; i<UiConstants.threadCount; i++) {
            UpdateThingsThread thread = new UpdateThingsThread(this.things, positions[i][0], positions[i][1]);
            threads.add(thread);
        }
        for (UpdateThingsThread thread: threads) { thread.start(); }
        for (UpdateThingsThread thread: threads) { thread.join(); }
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
        this.engine.tracker.printStepNanoseconds("Update things");

        this.things.addAll(this.newThings);
        this.newThings.clear();
        this.engine.tracker.printStepNanoseconds("Add new things");

        this.removeDeadOrOffscreen();
        this.engine.tracker.printStepNanoseconds("Remove dead");
    }

    public World(Engine engine) {
        this.initThings = new InitThings(this);
        this.engine = engine;
        this.counter = new ThingCounter(this);
    }
}
