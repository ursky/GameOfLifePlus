package things;
import constants.BlankConstants;
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
        if (this.isSeed) {
            this.itemImage = this.constants.mainImage.getImage(this.currentRotation, this.currentOpacity);
            this.size *= 2;
            this.isSeed = false;
        }
        if (this.size > this.constants.maxSize) {
            this.size = this.constants.maxSize;
        }
        if (this.healthPercent < 0) {
            this.healthPercent = -10000;
            this.itemImage = this.constants.deadImage.getImage(this.currentRotation, this.currentOpacity);
            this.currentOpacity += Math.random() * this.constants._decayRate * this.coolDownFrames;
        }
    }

    public void live() {
        this.grow();
        this.updateCoolDowns();
    }

    public void reproduce() {
        // this is just a placeholder method
    }

    public Organism(float xPosition, float yPosition, float size, World world, BlankConstants constants) {
        super(xPosition, yPosition, size, world, constants);
    }
}