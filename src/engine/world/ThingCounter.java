package engine.world;

import constants.BlankConstants;
import engine.World;
import things.Classes.Thing;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ThingCounter {
    World world;
    HashMap<String, ArrayList<Integer>> classCounters;
    public ArrayList<ArrayList<Float>> classFractions;
    public ArrayList<Color> colors;
    int currentIndex = -1;
    boolean logCounts = false;

    public void initializeCounts() {
        // save fraction values
        if (this.currentIndex >= 0) {
            this.saveFractions();
        }
        // then append new values
        this.currentIndex ++;
        for (BlankConstants constants: this.world.initThings.orderedBlankConstants) {
            this.classCounters.get(constants.name).add(0);
        }
    }

    private void saveFractions() {
        ArrayList<Float> values = this.getCurrentValues();
        float totalCount = 0;
        for (float value: values) {
            totalCount += value;
        }
        for (int i=0; i<values.size(); i++) {
            float value = values.get(i);
            float fraction = value / totalCount;
            this.classFractions.get(i).add(fraction);
        }
    }

    private ArrayList<Float> getCurrentValues() {
        ArrayList<Float> values = new ArrayList<>();
        for (BlankConstants constants: this.world.initThings.orderedBlankConstants) {
            float value = this.classCounters.get(constants.name).get(this.currentIndex);
            if (this.logCounts) {
                value = (float) (Math.log(value) / Math.log(2));
            }
            values.add(value);
        }
        return values;
    }

    public void countThing(Thing thing) {
        ArrayList<Integer> pastCounts = this.classCounters.get(thing.constants.name);
        int count = pastCounts.get(this.currentIndex);
        count ++;
        pastCounts.set(this.currentIndex, count);
    }

    public String generateCountsMessage() {
        String message = "";
        for (BlankConstants constants: this.world.initThings.orderedBlankConstants) {
            message += constants.name + ":\t" + this.classCounters.get(constants.name).get(this.currentIndex) + "\n";
        }
        return message;
    }

    public ThingCounter(World world) {
        this.world = world;
        this.classCounters = new HashMap<>();
        this.classFractions = new ArrayList<>();
        this.colors = new ArrayList<>();
        for (BlankConstants constants: this.world.initThings.orderedBlankConstants) {
            this.classCounters.put(constants.name, new ArrayList<>());
            this.classFractions.add(new ArrayList<>());
            this.colors.add(constants.color);
        }
    }
}