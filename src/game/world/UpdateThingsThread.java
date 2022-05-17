package game.world;

import game.world.things.Classes.Thing;

import java.util.ArrayList;

/**
 * This class handles a single thread that goes over creatures in a selected range and updates all of them.
 */
public class UpdateThingsThread implements Runnable {
    Thread t;
    public int start, end;
    public ArrayList<Thing> things;

    /**
     * Initialize thread
     * @param things: list of creatures
     * @param start: index to start updating from
     * @param end: index to stop updating at
     */
    public UpdateThingsThread(ArrayList<Thing> things, int start, int end) {
        this.things = things;
        this.start = start;
        this.end = end;
        this.t = new Thread(this);
    }

    /**
     * Launch thread
     */
    public void start() {
        this.t.start();
    }

    /**
     * Wait for thread to finish
     */
    public void join() {
        try {
            this.t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Go over creatures in desired range and have them live out their lives this frame...
     */
    public void run() {
        for (int i=this.start; i<this.end; i++) {
            Thing thing = this.things.get(i);
            if (thing.coolDown <= 0 && thing.isRendered()) {
                thing.live();
            } else { thing.coolDown--; }
        }
    }
}