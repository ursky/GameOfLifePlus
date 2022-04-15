package world;

import things.Thing;

import java.util.ArrayList;

public class QuadTreeThread implements Runnable {
    Thread t;
    public int thread;
    public ArrayList<Thing> things;

    public QuadTreeThread(ArrayList<Thing> things, int thread) {
        this.things = things;
        this.thread = thread;
        this.t = new Thread(this);
    }

    public void start() {
        this.t.start();
    }

    public void join() {
        try {
            this.t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        for (int i=0; i<this.things.size(); i++) {
            Thing thing = things.get(i);
            if (thing.getThreadSlice() == this.thread) {
                quadsearch.Point point = new quadsearch.Point(i, thing.xPosition, thing.yPosition);
                thing.world.searchAreas.getQuadTree(thing).addPoint(point);
            }
        }
    }
}