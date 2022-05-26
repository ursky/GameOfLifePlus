package game.world.things.Classes;
import game.constants.UiConstants;
import game.visuals.ImageStack;
import game.quadsearch.Point;
import game.quadsearch.Region;
import game.utilities.Random;
import game.world.World;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Describe the things that a living creature/thing can do
 */
public class Thing {
    public World world;
    public CreatureConstants constants;
    public float size;
    // this is a fraction that describes how far this thing is from full size
    public float relativeSize;
    // position coordinates
    public Point coordinate;
    // the bin describes the grouping on the plane that this thing belongs to
    public int xBin;
    public int yBin;
    // image to represent this thing on screen
    public BufferedImage itemImage;
    public float currentRotation = 0;
    public float currentOpacity = 255;
    // then the thing is a seed it basically goes into stasis
    public boolean isSeed = false;
    public float reproductionCoolDown;
    public int coolDownFrames = 1;
    public int coolDown = 0;
    public float healthPercent;
    public float biomass;
    public int framesInExistence = 0;
    public float maxSpeed = 0;

    /**
     * Decrement cool down
     */
    public void updateCoolDowns() {
        this.coolDownFrames = this.constants.frameCoolDown;
        this.coolDown = this.coolDownFrames - 1;
    }

    /**
     * Check if this thing is currently on screen
     * @return: is it on screen?
     */
    public boolean isInView() {
        float halfSize = this.size / 2;
        return (this.coordinate.getX() + halfSize > this.world.game.userIO.positionsInView[0]
                && this.coordinate.getX() - halfSize < this.world.game.userIO.positionsInView[1]
                && this.coordinate.getY() + halfSize > this.world.game.userIO.positionsInView[2]
                && this.coordinate.getY() - halfSize < this.world.game.userIO.positionsInView[3]);
    }

    /**
     * Check if a given set of coordinates is valid for this game field
     * @param point: coordinate to check
     * @return: are the coordinates valid?
     */
    public boolean isInBounds(Point point) {
        return (point.getX() >= 0
                && point.getY() >= 0
                && point.getX() < UiConstants.fullDimX
                && point.getY() < UiConstants.fullDimY);
    }

    /**
     * Calculate distance between this thing and another thing
     * @param otherThing: thing to calculate distance to
     * @return: distance
     */
    public float calculateDistanceTo(Thing otherThing) {
        return this.coordinate.getDistanceTo(otherThing.coordinate);
    }

    /**
     * Calculate distance of this thing to a point
     * @param otherPoint: point to consider
     * @return: distance
     */
    public float calculateDistanceTo(Point otherPoint) {
        return this.coordinate.getDistanceTo(otherPoint);
    }

    /**
     * Return all things in a given radius around this thing
     * @param radius: search radius
     * @return: things in range, excluding self
     */
    public ArrayList<Thing> getThingsInRange(float radius) {
        ArrayList<Thing> creaturesInRange = new ArrayList<>();
        Region searchArea = new Region(
                this.coordinate.getX() - radius,
                this.coordinate.getY() - radius,
                this.coordinate.getX() + radius,
                this.coordinate.getY() + radius);
        // note that we need to first retrieve the quad tree thread group this thing belongs to
        List<Point> pointsInRange = world.quadTreeSearchGroups.getQuadTree(this).search(searchArea, null);
        for (Point point: pointsInRange) {
            Thing thing = world.things.get(point.getIndex());
            float distance = this.calculateDistanceTo(thing);
            if (distance > 0 && distance <= radius + thing.size / 2) {
                creaturesInRange.add(thing);
            }
        }
        return creaturesInRange;
    }

    /**
     * Check if this thing is in view or close enough to the FOV to be updated this frame
     * @return: should we update this this frame?
     */
    public boolean isRendered() {
        return this.world.game.procedural.isRendered[this.xBin][this.yBin];
    }

    /**
     * Calculate which quad tree search group this thing belongs to this frame
     * @return: thread group
     */
    public int getThreadSlice() {
        float renderedLeftX = this.world.game.procedural.currentCoordinates.getX1();
        float positionInRendered = this.coordinate.getX() - renderedLeftX + this.world.game.tracker.threadBuffer;
        float threadWidth = this.world.game.procedural.currentCoordinates.getWidth() / UiConstants.threadCount;

        if (positionInRendered >= this.world.game.procedural.currentCoordinates.getWidth()) {
            positionInRendered -= this.world.game.procedural.currentCoordinates.getWidth();
        }
        int thread = (int) (positionInRendered / threadWidth);
        if (thread < 0) {
            return 0;
        }
        else if (thread >= UiConstants.threadCount) {
            return UiConstants.threadCount - 1;
        }
        else {
            return thread;
        }
    }

    /**
     * Update the thing image based on the current rotation and opacity
     * @param imageStack: image that best represents this thing right now
     */
    public void initImage(ImageStack imageStack) {
        this.itemImage = imageStack.getImage(this.currentRotation, this.currentOpacity);
    }

    /**
     * Update the world field bin this thing is in right now
     */
    public void updateBin() {
        this.xBin = (int) (this.coordinate.getX() / this.world.game.procedural.binWidthX);
        this.yBin = (int) (this.coordinate.getY() / this.world.game.procedural.binWidthY);
    }

    /**
     * Create offspring, but make it a metamorphosis precursor to this creature
     * @return: new thing
     */
    public Thing makeLarvae() {
        Thing clone = makeBlank();
        clone.currentRotation = this.currentRotation;
        clone.currentOpacity = this.currentOpacity;
        clone.constants = this.constants.metamorphosisFrom;
        return clone;
    }

    /**
     * Make a new thing that is a replica of this thing
     * @return: new thing
     */
    public Thing makeClone() {
        Thing clone = makeBlank();
        clone.currentRotation = this.currentRotation;
        clone.currentOpacity = this.currentOpacity;
        clone.itemImage = this.constants.mainImage.getImage(this.currentRotation, this.currentOpacity);
        clone.constants = this.constants;
        clone.coolDown = this.coolDown;
        clone.healthPercent = this.healthPercent;
        clone.reproductionCoolDown = this.reproductionCoolDown;
        clone.biomass = this.biomass;
        clone.size = this.size;
        return clone;
    }

    /**
     * Make a blank thing with default attributes, but make sure it is either an animal or plant
     * @return: new thing
     */
    public Thing makeBlank() {
        if (Objects.equals(this.constants.type, "Animal")) {
            return new Animal(this.coordinate, this.size, this.world, this.constants);
        }
        else if (Objects.equals(this.constants.type, "Plant")) {
            return new Plant(this.coordinate, this.size, this.world, this.constants);
        }
        else {
            return new Thing(this.coordinate, this.size, this.world, this.constants);
        }
    }

    /**
     * Increase/decrease health based on metabolism speed, grow up, and update image accordingly
     */
    public void metabolize() {
        this.healthPercent += this.constants._metabolismRate * this.coolDownFrames;
        if (this.healthPercent < 0) {
            this.killSelf();
            return;
        }

        if (this.isSeed) {
            this.initImage(this.constants.mainImage);
            this.isSeed = false;
            return;
        }

        if (this.healthPercent >= this.constants.growAtHealth) {
            this.grow();
        }
        this.sizeCheck();
    }

    /**
     * This thing is dead now. Make sure it stays that way until garbage collector comes...
     */
    public void killSelf() {
        this.healthPercent = -10000;
        this.currentOpacity += Math.random() * this.constants._decayRate * this.coolDownFrames;
        this.initImage(this.constants.deadImage);
    }

    /**
     * Grow up and increase in size and biomass up to a point
     */
    public void grow() {
        float growth = (float)Math.random() * this.constants._maxGrowthRate * this.coolDownFrames;
        if (this.size < this.constants.maxSize) {
            this.size += growth;
        }
        if (this.biomass < this.constants.maxBiomass) {
            this.biomass += this.constants.maxBiomass * growth / this.constants.maxSize;
        }
    }

    /**
     * Make sure the thing size is not greater than allowed
     */
    public void sizeCheck() {
        this.relativeSize = (this.size + 1) / (this.constants.maxSize + 1);
        if (this.size > this.constants.maxSize) {
            this.size = this.constants.maxSize;
        }
    }

    /**
     * Reproduce and make tiny clones!
     */
    public void reproduce() {
        this.reproductionCoolDown -= constants._reproductionCoolDown;
        if (this.healthPercent >= this.constants.reproduceAtHealth
                && this.reproductionCoolDown < 0
                && this.size >= this.constants.reproduceAtSize) {
            for (int i=0; i<this.constants.maxOffsprings; i++) {
                makeYoung();
            }
            this.healthPercent *= this.constants.reproductionPenalty;
            this.reproductionCoolDown = this.constants.reproductionCoolDown;
        }
    }

    /**
     * Make an offspring - a single tiny clone of this thing, with a couple randomized attributes
     */
    public void makeYoung() {
        // randomize the exact spawn position
        Point seedCoordinate = new Point(0,
                Random.randFloat(this.coordinate.getX() - this.constants.dispersalRange,
                        this.coordinate.getX() + this.constants.dispersalRange),
                Random.randFloat(this.coordinate.getY() - this.constants.dispersalRange,
                        this.coordinate.getY() + this.constants.dispersalRange)
        );

        if (this.isInBounds(seedCoordinate)
                && this.calculateDistanceTo(seedCoordinate) <= this.constants.dispersalRange) {
            Thing seedling;
            if (this.constants.metamorphosisIsAdult) {
                seedling = makeLarvae();
            }
            else {
                seedling = makeClone();
            }
            seedling.size = seedling.constants.startSize;
            seedling.relativeSize = (1 + seedling.size) / (seedling.constants.maxSize + 1);
            seedling.biomass = seedling.constants.maxBiomass * seedling.relativeSize;
            seedling.healthPercent = seedling.constants.startHealth;
            // randomize the hatch time
            float maxHatchTime = seedling.constants._hatchRate * seedling.coolDownFrames;
            float minHatchTime = maxHatchTime / 2;
            seedling.coolDown = (int) Random.randFloat(minHatchTime, maxHatchTime);
            seedling.coordinate = seedCoordinate;
            seedling.updateBin();
            seedling.isSeed = true;
            seedling.currentRotation = Random.randFloat(0, 360);
            seedling.initImage(seedling.constants.youngImage);
            if (seedling.isRendered()) {
                this.world.newThings.add(seedling);
            }
        }
    }

    /**
     * Living is handled by the Plant/Animal class since they are quite different
     */
    public void live() {
        // placeholder
    }

    /**
     * Initialize thing object
     * @param coordinate: position on 2D plane
     * @param size: current size
     * @param world: game world
     * @param constants: constants of this thing
     */
    public Thing(Point coordinate, float size, World world, CreatureConstants constants) {
        this.constants = constants;
        this.initImage(this.constants.mainImage);
        this.coordinate = coordinate;
        this.world = world;
        this.size = size;
        this.biomass = this.constants.maxBiomass * this.relativeSize;
        this.updateBin();
    }
}