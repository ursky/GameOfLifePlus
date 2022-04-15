package things;
import constants.ThingConstants;
import world.World;


public class Organism extends Thing {
    public void grow() {
        this.healthPercent += this.constants._metabolismRate * this.coolDownFrames;
        if (this.healthPercent > 100) {
            this.healthPercent = 100;
        }
        if (this.size < this.constants.maxSize && this.healthPercent >= this.constants.growAtHealth) {
            this.size += Math.random() * this.constants._maxGrowthRate * this.coolDownFrames;
        }
        if (this.size > this.constants.maxSize) {
            this.size = this.constants.maxSize;
        }
    }

    public void live() {
        if (this.healthPercent > 0) {
            this.grow();
            this.updateCoolDowns();
        }
    }

    public void reproduce() {
        // this is just a placeholder method
    }

    public Organism(float xPosition, float yPosition, float size, World world, ThingConstants constants) {
        super(xPosition, yPosition, size, world, constants);
    }
}