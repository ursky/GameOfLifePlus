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
        this.searchAreas = new SearchAreas(this.engine.threadCount);
        ArrayList<QuadTreeThread> threads = new ArrayList<>();
        for (int i = 0; i<this.engine.threadCount; i++) {
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
        for (int i = 0; i<this.engine.threadCount; i++) {
            UpdateThingsThread thread = new UpdateThingsThread(this.things, positions[i][0], positions[i][1]);
            threads.add(thread);
        }
        for (UpdateThingsThread thread: threads) { thread.start(); }
        for (UpdateThingsThread thread: threads) { thread.join(); }
    }

    private int[][] breakIntoChunks(ArrayList<Thing> arrayList) {
        int increment = arrayList.size() / this.engine.threadCount;
        int[][] positions = new int[this.engine.threadCount][2];
        for (int i = 0; i<this.engine.threadCount; i++) {
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
            if (thing.size > 0 && thing.currentOpacity > 0) {
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

    public void updateWorld() {
        this.engine.timeUpdate("\npMisc");

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
