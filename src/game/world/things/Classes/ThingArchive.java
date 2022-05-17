package game.world.things.Classes;

import java.util.ArrayList;

/**
 * An organized collection of creatures/thing
 */
public class ThingArchive {
    public ArrayList<Thing> things;
    public boolean empty;

    /**
     * Add thing to archive
     * @param thing: thing to add
     */
    public void add(Thing thing) {
        this.things.add(thing);
        this.empty = false;
    }

    /**
     * Clear things in this archive
     */
    public void clear() {
        this.things.clear();
        this.empty = true;
    }

    /**
     * Initialize thing archive
     */
    public ThingArchive() {
        this.things = new ArrayList<>();
        this.empty = true;
    }
}