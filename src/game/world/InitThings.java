package game.world;

import game.World;
import game.userIO.UiConstants;
import game.things.Classes.Animal;
import game.things.AnimalConstants.*;
import game.things.Classes.CreatureConstants;
import game.things.Classes.Plant;
import game.things.PlantConstants.BushConstants;
import game.things.PlantConstants.GrassConstants;
import game.things.PlantConstants.TreeConstants;
import game.things.Classes.Thing;
import game.utilities.Utils;

import java.util.ArrayList;
import java.util.Objects;

public class InitThings {
    public World world;
    public ArrayList<CreatureConstants> orderedCreatureConstants = new ArrayList<>();

    private void initializeThings(float minX, float minY, float maxX, float maxY, CreatureConstants constants) {
        int count;
        if (this.world.engine.tracker.frameCounter > UiConstants.fastPreRenderFrames) {
            // if this is later in the simulation engine.things should be copied, so include only small amount of new engine.things
            count = seedDensityToCount(constants.startingDensity * UiConstants.postStartSpawnPenalty,
                    minX, minY, maxX, maxY);
        }
        else {
            count = seedDensityToCount(constants.startingDensity, minX, minY, maxX, maxY);
        }
        for (int i = 0; i<count; i++) {
            float randX = Utils.randFloat(minX, maxX);
            float randY = Utils.randFloat(minY, maxY);
            float size = Utils.randFloat(constants.maxSize / 2, constants.maxSize);
            this.createThing(randX, randY, size, constants);
        }
    }

    public void createThing(float randX, float randY, float size, CreatureConstants constants) {
        if (constants.type.equals("Plant")) {
            Plant thing = new Plant(randX, randY, size, this.world, constants);
            initThing(thing);
        } else if (constants.type.equals("Animal")) {
            Animal thing = new Animal(randX, randY, size, this.world, constants);
            initThing(thing);
        } else {
            Thing thing = new Thing(randX, randY, size, this.world, constants);
            initThing(thing);
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
        for (CreatureConstants creatureConstants : this.orderedCreatureConstants) {
            if (Objects.equals(creatureConstants.type, "Plant")) {
                this.initializeThings(minX, minY, maxX, maxY, creatureConstants);
            }
        }
    }

    public void initAnimals(float minX, float minY, float maxX, float maxY) {
        for (CreatureConstants creatureConstants : this.orderedCreatureConstants) {
            if (Objects.equals(creatureConstants.type, "Animal")) {
                this.initializeThings(minX, minY, maxX, maxY, creatureConstants);
            }
        }
    }

    public ArrayList<Thing> copyThings(float minX, float minY, float maxX, float maxY) {
        ArrayList<Thing> copiedThings = new ArrayList<>();
        if (UiConstants.blankCanvas) {
            return copiedThings;
        }
        float[] copyRange = selectRangeToCopy(maxX - minX, maxY - minY);
        for (Thing thing: this.world.things) {
            if (Utils.inBounds(thing.xPosition, copyRange[0], copyRange[1])
                    && Utils.inBounds(thing.yPosition, copyRange[2], copyRange[3])) {
                float newXPos = minX + (thing.xPosition - copyRange[0]);
                float newYPos = minY + (thing.yPosition - copyRange[2]);
                Thing newThing = copyThingTo(thing, newXPos, newYPos);
                copiedThings.add(newThing);
            }
        }
        return copiedThings;
    }

    private float[] selectRangeToCopy(float widthX, float widthY) {
        float copyStartX = Utils.randFloat(this.world.engine.userIO.positionsInView[0],
                this.world.engine.userIO.positionsInView[1] - widthX);
        float copyEndX = copyStartX + widthX;
        float copyStartY = Utils.randFloat(this.world.engine.userIO.positionsInView[2],
                this.world.engine.userIO.positionsInView[3] - widthY);
        float copyEndY = copyStartY + widthY;
        return new float[] {copyStartX, copyEndX, copyStartY, copyEndY};
    }

    private Thing copyThingTo(Thing thing, float newXPos, float newYPos) {
        Thing newThing = thing.makeClone();
        newThing.xPosition = newXPos;
        newThing.yPosition = newYPos;
        newThing.updateBin();
        return newThing;
    }

    private int seedDensityToCount(float densityPer1000, float minX, float minY, float maxX, float maxY) {
        if (UiConstants.blankCanvas) {
            return 0;
        }
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
        for (CreatureConstants constants: this.orderedCreatureConstants) {
            constants.updateRates();
        }
    }

    public float getBiggestSize() {
        float biggest = 0;
        for (CreatureConstants constants: this.orderedCreatureConstants) {
            if (constants.maxSize > biggest) {
                biggest = constants.maxSize;
            }
        }
        return biggest;
    }

    public InitThings(World world) {
        this.world = world;

        System.out.println("Initializing grass");
        CreatureConstants grassConstants = new GrassConstants(this.world);
        this.orderedCreatureConstants.add(grassConstants);

        System.out.println("Initializing bushes");
        CreatureConstants bushConstants = new BushConstants(this.world);
        this.orderedCreatureConstants.add(bushConstants);

        System.out.println("Initializing trees");
        CreatureConstants treeConstants = new TreeConstants(this.world);
        this.orderedCreatureConstants.add(treeConstants);

        System.out.println("Initializing beetles");
        CreatureConstants beetleConstants = new BeetleConstants(this.world);
        this.orderedCreatureConstants.add(beetleConstants);

        System.out.println("Initializing butterflies");
        CreatureConstants butterflyConstants = new ButterflyConstants(this.world);
        CreatureConstants caterpillarConstants = new CaterpillarConstants(this.world);
        butterflyConstants.metamorphosisFrom = caterpillarConstants;
        butterflyConstants.metamorphosisTo = butterflyConstants;
        caterpillarConstants.metamorphosisFrom = caterpillarConstants;
        caterpillarConstants.metamorphosisTo = butterflyConstants;
        this.orderedCreatureConstants.add(butterflyConstants);
        this.orderedCreatureConstants.add(caterpillarConstants);

        System.out.println("Initializing turtles");
        CreatureConstants insectivoreConstants = new TurtleConstants(this.world);
        this.orderedCreatureConstants.add(insectivoreConstants);

        System.out.println("Initializing elephants");
        CreatureConstants herbivoreConstants = new ElephantConstants(this.world);
        this.orderedCreatureConstants.add(herbivoreConstants);
    }
}
