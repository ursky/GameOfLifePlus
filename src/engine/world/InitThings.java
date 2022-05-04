package engine.world;

import constants.*;
import engine.World;
import things.Classes.Animal;
import things.AnimalConstants.*;
import things.Classes.Plant;
import things.PlantConstants.BushConstants;
import things.PlantConstants.GrassConstants;
import things.PlantConstants.TreeConstants;
import things.Classes.Thing;
import engine.utilities.Utils;

import java.util.ArrayList;
import java.util.Objects;

public class InitThings {
    public World world;
    public ArrayList<BlankConstants> orderedBlankConstants = new ArrayList<>();

    private void initializeThings(float minX, float minY, float maxX, float maxY, BlankConstants constants) {
        int count = seedDensityToCount(constants.startingDensity, minX, minY, maxX, maxY);
        for (int i = 0; i<count; i++) {
            float randX = Utils.randFloat(minX, maxX);
            float randY = Utils.randFloat(minY, maxY);
            float size = Utils.randFloat(constants.maxSize / 2, constants.maxSize);
            createThing(randX, randY, size, constants);
        }
    }

    private void createThing(float randX, float randY, float size, BlankConstants constants) {
        if (constants.type.equals("Plant")) {
            Plant thing = new Plant(randX, randY, size, this.world, constants);
            initThing(thing);
        } else if (constants.type.equals("Animal")) {
            Animal thing = new Animal(randX, randY, size, this.world, constants);
            initThing(thing);
        } else {
            Thing Thing = new Thing(randX, randY, size, this.world, constants);
            initThing(Thing);
        }
    }

    private void initThing(Thing thing) {
        float health = Utils.randFloat(20, 100);
        thing.currentRotation = Utils.randFloat(0, 360);
        thing.currentOpacity = 255;
        thing.initImage(thing.constants.mainImage);
        thing.healthPercent = health;
        this.world.things.add(thing);
    }

    public void initPlants(float minX, float minY, float maxX, float maxY) {
        for (BlankConstants blankConstants : this.orderedBlankConstants) {
            if (Objects.equals(blankConstants.type, "Plant")) {
                this.initializeThings(minX, minY, maxX, maxY, blankConstants);
            }
        }
    }

    public void initAnimals(float minX, float minY, float maxX, float maxY) {
        for (BlankConstants blankConstants : this.orderedBlankConstants) {
            if (Objects.equals(blankConstants.type, "Animal")) {
                this.initializeThings(minX, minY, maxX, maxY, blankConstants);
            }
        }
    }

    private int seedDensityToCount(float densityPer1000, float minX, float minY, float maxX, float maxY) {
        float area = (maxX - minX) * (maxY - minY);
        float expectedCount = densityPer1000 * area / 10000;
        int countInt = (int)expectedCount;
        float remainder = expectedCount - countInt;
        if (Math.random() < remainder) {
            countInt++;
        }
        return countInt;
    }

    public void updateConstants() {
        for (BlankConstants constants: this.orderedBlankConstants) {
            constants.updateRates();
        }
    }

    public float getBiggestSize() {
        float biggest = 0;
        for (BlankConstants constants: this.orderedBlankConstants) {
            if (constants.maxSize > biggest) {
                biggest = constants.maxSize;
            }
        }
        return biggest;
    }

    public InitThings(World world) {
        this.world = world;

        System.out.println("Initializing grass");
        BlankConstants grassConstants = new GrassConstants(this.world);
        this.orderedBlankConstants.add(grassConstants);

        System.out.println("Initializing bushes");
        BlankConstants bushConstants = new BushConstants(this.world);
        this.orderedBlankConstants.add(bushConstants);

        System.out.println("Initializing trees");
        BlankConstants treeConstants = new TreeConstants(this.world);
        this.orderedBlankConstants.add(treeConstants);

        System.out.println("Initializing beetles");
        BlankConstants beetleConstants = new BeetleConstants(this.world);
        this.orderedBlankConstants.add(beetleConstants);

        System.out.println("Initializing butterflies");
        BlankConstants butterflyConstants = new ButterflyConstants(this.world);
        BlankConstants caterpillarConstants = new CaterpillarConstants(this.world);
        butterflyConstants.metamorphosisFrom = caterpillarConstants;
        butterflyConstants.metamorphosisTo = butterflyConstants;
        caterpillarConstants.metamorphosisFrom = caterpillarConstants;
        caterpillarConstants.metamorphosisTo = butterflyConstants;
        this.orderedBlankConstants.add(butterflyConstants);
        this.orderedBlankConstants.add(caterpillarConstants);

        System.out.println("Initializing elephants");
        BlankConstants herbivoreConstants = new ElephantConstants(this.world);
        this.orderedBlankConstants.add(herbivoreConstants);
    }
}
