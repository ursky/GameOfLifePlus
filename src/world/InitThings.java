package world;

import constants.BushConstants;
import constants.ThingConstants;
import constants.TreeConstants;
import things.Animal;
import things.Plant;
import things.Thing;
import utilities.Random;

import java.util.ArrayList;

public class InitThings {
    public World world;
    public ArrayList<ThingConstants> orderedThingConstants = new ArrayList<>();

    private void initializeThings(float minX, float minY, float maxX, float maxY, ThingConstants constants) {
        int count = seedDensityToCount(constants.startingDensity, minX, minY, maxX, maxY);
        for (int i = 0; i<count; i++) {
            float randX = Random.randFloat(minX, maxX);
            float randY = Random.randFloat(minY, maxY);
            float size = Random.randFloat(constants.maxSize / 2, constants.maxSize);
            float health = Random.randFloat(20, 100);
            createThing(randX, randY, size, health, constants);
        }
    }

    private void createThing(float randX, float randY, float size, float health, ThingConstants constants) {
        if (constants.type.equals("Plant")) {
            Plant thing = new Plant(randX, randY, size, this.world, constants);
            thing.healthPercent = health;
            this.world.things.add(thing);
        } else if (constants.type.equals("Animal")) {
            Animal thing = new Animal(randX, randY, size, this.world, constants);
            thing.healthPercent = health;
            this.world.things.add(thing);
        } else {
            Thing thing = new Thing(randX, randY, size, this.world, constants);
            thing.healthPercent = health;
            this.world.things.add(thing);
        }
    }

    public void initThingsInBin(float minX, float minY, float maxX, float maxY) {
        for (ThingConstants thingConstants: this.orderedThingConstants) {
            this.initializeThings(minX, minY, maxX, maxY, thingConstants);
        }
    }

    private int seedDensityToCount(float densityPer100, float minX, float minY, float maxX, float maxY) {
        float area = (maxX - minX) * (maxY - minY);
        return (int) (densityPer100 * area / 10000);
    }

    public void updateConstants() {
        for (ThingConstants constants: this.orderedThingConstants) {
            constants.update();
        }
    }

    public InitThings(World world) {
        this.world = world;

        ThingConstants bushConstants = new BushConstants(this.world);
        this.orderedThingConstants.add(bushConstants);

        ThingConstants treeConstants = new TreeConstants(this.world);
        this.orderedThingConstants.add(treeConstants);
    }
}
