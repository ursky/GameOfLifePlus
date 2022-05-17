package game.world;

import game.world.things.Classes.CreatureConstants;
import game.world.things.Classes.Thing;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class keeps track of how many of each creature there was and is. Primarily for the dashboard display.
 */
public class ThingCounter {
    World world;
    HashMap<String, ArrayList<Integer>> classCounters;
    public ArrayList<Integer> totalCounts, thingCounts;
    public ArrayList<ArrayList<Float>> classFractions;
    public ArrayList<Color> colors;
    int currentIndex = -1;

    /**
     * Count every creature type at this moment, and save the data
     */
    public void countThings() {
        // save fraction values
        if (this.currentIndex >= 0) {
            this.saveFractions();
            // then append new values
            int totalCount = 0;
            for (CreatureConstants constants: this.world.initThings.orderedCreatureConstants) {
                totalCount += this.classCounters.get(constants.name).get(this.currentIndex);
                this.classCounters.get(constants.name).add(0);
            }
            this.totalCounts.add(totalCount);
        }
        else {
            for (CreatureConstants constants: this.world.initThings.orderedCreatureConstants) {
                this.classCounters.get(constants.name).add(0);
            }
        }
        this.currentIndex ++;
    }

    /**
     * Save the relative abundances of different creatures
     */
    private void saveFractions() {
        this.getCurrentValues();
        float totalCount = 0;
        for (float value: this.thingCounts) {
            totalCount += value;
        }
        for (int i=0; i<this.thingCounts.size(); i++) {
            float value = this.thingCounts.get(i);
            float fraction = value / totalCount;
            this.classFractions.get(i).add(fraction);
        }
    }

    /**
     * Retrieve the current counts of all creatures
     */
    private void getCurrentValues() {
        this.thingCounts = new ArrayList<>();
        for (CreatureConstants constants: this.world.initThings.orderedCreatureConstants) {
            int value = this.classCounters.get(constants.name).get(this.currentIndex);
            this.thingCounts.add(value);
        }
    }

    /**
     * Check the name of a creature and save it in the count
     * @param thing: creature to count
     */
    public void countThing(Thing thing) {
        ArrayList<Integer> pastCounts = this.classCounters.get(thing.constants.name);
        int count = pastCounts.get(this.currentIndex);
        count ++;
        pastCounts.set(this.currentIndex, count);
    }

    /**
     * Generate a status message saying how many of every creature there is right now
     * @return: status string that can be printed
     */
    public String generateCountsMessage() {
        StringBuilder message = new StringBuilder();
        for (CreatureConstants constants: this.world.initThings.orderedCreatureConstants) {
            message.append(constants.name)
                    .append(":\t")
                    .append(this.classCounters.get(constants.name).get(this.currentIndex))
                    .append("\n");
        }
        return message.toString();
    }

    /**
     * Initialize counter
     * @param world: game world
     */
    public ThingCounter(World world) {
        this.world = world;
        this.classCounters = new HashMap<>();
        this.totalCounts = new ArrayList<>();
        this.thingCounts = new ArrayList<>();
        this.classFractions = new ArrayList<>();
        this.colors = new ArrayList<>();
        for (CreatureConstants constants: this.world.initThings.orderedCreatureConstants) {
            this.classCounters.put(constants.name, new ArrayList<>());
            this.classFractions.add(new ArrayList<>());
            this.colors.add(constants.color);
        }
    }
}