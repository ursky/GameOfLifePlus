package game.things.Classes;

import java.util.ArrayList;

public class ThingArchive {
    public ArrayList<Thing> things;
    public boolean empty;

    public void add(Thing thing) {
        this.things.add(thing);
        this.empty = false;
    }

    public ArrayList<Thing> get() {
        return this.things;
    }

    public void clear() {
        this.things.clear();
        this.empty = true;
    }

    public ThingArchive() {
        this.things = new ArrayList<>();
        this.empty = true;
    }
}