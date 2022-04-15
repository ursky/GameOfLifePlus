package things;
import world.World;


public class Organism extends Thing {
    public int maxOffsprings;
    public float maxSize;
    public float maxGrowthRate;
    public float reproduceAtSize;
    public float reproduceAtHealth;
    public float reproductionPenalty;
    public float growAtHealth;
    public float metabolismRate;

    public void grow() {
        this.healthPercent += this.metabolismRate * this.coolDownFrames / this.world.engine.currentFPS;
        if (this.healthPercent > 100) {
            this.healthPercent = 100;
        }
        if (this.size < this.maxSize && this.healthPercent >= this.growAtHealth) {
            this.size += Math.random() * this.maxGrowthRate * this.coolDownFrames / this.world.engine.currentFPS;
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

    public Organism(float xPosition, float yPosition, float size, World world) {
        super(xPosition, yPosition, size, world);
    }
}