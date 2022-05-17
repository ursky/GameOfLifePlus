package game.quadsearch;

import game.world.things.Classes.Thing;

import java.util.ArrayList;

/**
 * A thread class handling processing data in a subsection of the game field with the Quad tree algorithm.
 */
public class QuadTreeThread implements Runnable {
    Thread t;
    public int thread;
    public ArrayList<Thing> things;

    /**
     * Initialize thread
     * @param things: creatures to add to this quad tree search object
     * @param thread: index of the thread
     */
    public QuadTreeThread(ArrayList<Thing> things, int thread) {
        this.things = things;
        this.thread = thread;
        this.t = new Thread(this);
    }

    /**
     * Launch thread
     */
    public void start() {
        this.t.start();
    }

    /**
     * Wait for thread to stop
     */
    public void join() {
        try {
            this.t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Add creatures designated to this thread to the Quad tree data structure
     */
    public void run() {
        for (int i=0; i<this.things.size(); i++) {
            Thing thing = things.get(i);
            if (thing.isSeed) {continue;}
            if (thing.getThreadSlice() == this.thread) {
                game.quadsearch.Point point = new game.quadsearch.Point(i, thing.xPosition, thing.yPosition);
                thing.world.quadTreeSearchGroups.getQuadTree(thing).addPoint(point);
            }
        }
    }
}