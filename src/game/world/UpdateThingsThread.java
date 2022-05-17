package game.world;

import game.things.Classes.Thing;

import java.util.ArrayList;

public class UpdateThingsThread implements Runnable {
    Thread t;
    public int start, end;
    public ArrayList<Thing> things;

    public UpdateThingsThread(ArrayList<Thing> things, int start, int end) {
        this.things = things;
        this.start = start;
        this.end = end;
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
        for (int i=this.start; i<this.end; i++) {
            Thing thing = this.things.get(i);
            if (thing.coolDown <= 0 && thing.isRendered()) {
                thing.live();
            } else { thing.coolDown--; }
        }
    }
}