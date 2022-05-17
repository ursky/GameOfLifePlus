package game.world.things.Classes;
import game.quadsearch.Point;
import game.world.World;

/**
 * This class inherits from the generic Thing class and adds things that only plants can do.
 */
public class Plant extends Thing {
    /**
     * Shade all other plants that are under this plant's canopy, reducing their health
     */
    public void shadeOthers() {
        float shadeRange = this.constants.maxShadeRange * this.relativeSize;
        if (shadeRange > 1) {
            for (Thing otherTree : this.getThingsInRange(shadeRange)) {
                if (otherTree instanceof Plant && this.size >= otherTree.size && !otherTree.isSeed) {
                    otherTree.healthPercent += this.constants._shadePenalty * this.coolDownFrames;
                }
            }
        }
    }

    /**
     * Make a new plant
     * @return: new plant object
     */
    @Override
    public Plant makeBlank() {
        return new Plant(this.coordinate, this.size, this.world, this.constants);
    }

    /**
     * Live and do all the things a plant does, such as spread seeds and shade other plants
     */
    @Override
    public void live() {
        this.metabolize();
        if (this.healthPercent <= 0) {return; }
        this.reproduce();
        this.shadeOthers();
        this.updateCoolDowns();
    }

    /**
     * Initialize plant creature
     * @param coordinate: position to 2d plane
     * @param size: current size
     * @param world: game world
     * @param constants: creature constants
     */
    public Plant(Point coordinate, float size, World world, CreatureConstants constants) {
        super(coordinate, size, world, constants);
    }
}
