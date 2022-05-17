package game.world;

import game.constants.InitialSeedDensities;
import game.constants.UiConstants;
import game.quadsearch.Point;
import game.quadsearch.Region;
import game.world.things.Classes.Animal;
import game.world.things.AnimalConstants.*;
import game.world.things.Classes.CreatureConstants;
import game.world.things.Classes.Plant;
import game.world.things.PlantConstants.BushConstants;
import game.world.things.PlantConstants.GrassConstants;
import game.world.things.PlantConstants.TreeConstants;
import game.world.things.Classes.Thing;
import game.utilities.Random;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Handles creature creation, initialization, and storage
 */
public class InitThings {
    public World world;
    public ArrayList<CreatureConstants> orderedCreatureConstants = new ArrayList<>();

    /**
     * Randomly seed creatures of a given constants type in a region
     * @param region: region to populate
     * @param constants: creature constants to seed
     */
    private void initializeThings(Region region, CreatureConstants constants) {
        int count;
        if (this.world.game.tracker.frameCounter > UiConstants.fastPreRenderFrames) {
            // if this is later in the simulation engine.things should be copied, so include only small amount of new engine.things
            count = seedDensityToCount(
                    constants.startingDensity * InitialSeedDensities.postStartSpawnPenalty,
                    region);
        }
        else {
            count = seedDensityToCount(constants.startingDensity, region);
        }
        for (int i = 0; i<count; i++) {
            Point coordinate = new Point(0,
                    Random.randFloat(region.getX1(), region.getX2()),
                    Random.randFloat(region.getY1(), region.getY2()));
            float size = Random.randFloat(constants.maxSize / 2, constants.maxSize);
            this.createThing(coordinate, size, constants);
        }
    }

    public void createThing(Point coordinate, float size, CreatureConstants constants) {
        if (constants.type.equals("Plant")) {
            Plant thing = new Plant(coordinate, size, this.world, constants);
            initThing(thing);
        } else if (constants.type.equals("Animal")) {
            Animal thing = new Animal(coordinate, size, this.world, constants);
            initThing(thing);
        } else {
            Thing thing = new Thing(coordinate, size, this.world, constants);
            initThing(thing);
        }
    }

    private void initThing(Thing thing) {
        float health = Random.randFloat(20, 100);
        thing.currentRotation = Random.randFloat(0, 360);
        thing.currentOpacity = 255;
        thing.initImage(thing.constants.mainImage);
        thing.healthPercent = health;
        this.world.things.add(thing);
    }

    public void initPlants(Region region) {
        for (CreatureConstants creatureConstants : this.orderedCreatureConstants) {
            if (Objects.equals(creatureConstants.type, "Plant")) {
                this.initializeThings(region, creatureConstants);
            }
        }
    }

    public void initAnimals(Region region) {
        for (CreatureConstants creatureConstants : this.orderedCreatureConstants) {
            if (Objects.equals(creatureConstants.type, "Animal")) {
                this.initializeThings(region, creatureConstants);
            }
        }
    }

    public ArrayList<Thing> copyThings(Region region) {
        ArrayList<Thing> copiedThings = new ArrayList<>();
        if (UiConstants.blankCanvas) {
            return copiedThings;
        }
        Region copyRegion = selectRangeToCopy(region.getWidth(), region.getHeight());
        for (Thing thing: this.world.things) {
            if (copyRegion.containsPoint(thing.coordinate)) {
                Point newCoordinate = new Point(0,
                        region.getX1() + (thing.coordinate.getX() - copyRegion.getX1()),
                        region.getY1() + (thing.coordinate.getY() - copyRegion.getY1()));
                Thing newThing = copyThingTo(thing, newCoordinate);
                copiedThings.add(newThing);
            }
        }
        return copiedThings;
    }

    private Region selectRangeToCopy(float widthX, float widthY) {
        float copyStartX = Random.randFloat(this.world.game.userIO.positionsInView[0],
                this.world.game.userIO.positionsInView[1] - widthX);
        float copyEndX = copyStartX + widthX;
        float copyStartY = Random.randFloat(this.world.game.userIO.positionsInView[2],
                this.world.game.userIO.positionsInView[3] - widthY);
        float copyEndY = copyStartY + widthY;
        return new Region(copyStartX, copyStartY, copyEndX, copyEndY);
    }

    private Thing copyThingTo(Thing thing, Point newCoordinate) {
        Thing newThing = thing.makeClone();
        newThing.coordinate = newCoordinate;
        newThing.updateBin();
        return newThing;
    }

    private int seedDensityToCount(float densityPer1000, Region region) {
        if (UiConstants.blankCanvas) {
            return 0;
        }
        float area = region.getArea();
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
