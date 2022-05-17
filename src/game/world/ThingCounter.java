package game.world;

import game.world.things.Classes.CreatureConstants;
import game.world.things.Classes.Thing;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ThingCounter {
    World world;
    HashMap<String, ArrayList<Integer>> classCounters;
    public ArrayList<Integer> totalCounts, thingCounts;
    public ArrayList<ArrayList<Float>> classFractions;
    public ArrayList<Color> colors;
    int currentIndex = -1;

    public void initializeCounts() {
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

    private void getCurrentValues() {
        this.thingCounts = new ArrayList<>();
        for (CreatureConstants constants: this.world.initThings.orderedCreatureConstants) {
            int value = this.classCounters.get(constants.name).get(this.currentIndex);
            this.thingCounts.add(value);
        }
    }

    public void countThing(Thing thing) {
        ArrayList<Integer> pastCounts = this.classCounters.get(thing.constants.name);
        int count = pastCounts.get(this.currentIndex);
        count ++;
        pastCounts.set(this.currentIndex, count);
    }

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