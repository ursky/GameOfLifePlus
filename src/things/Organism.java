package things;
import world.World;

import java.util.ArrayList;

public class Organism extends Thing {
    public int maxOffsprings;
    public float maxSize;
    public float maxGrowthRate;
    public float reproduceAtSize;
    public float reproduceAtHealth;
    public float reproductionPenalty;
    public float growAtHealth;
    public float metabolismRate;
    public float healthPercent = 100;

    public void grow() {
        this.healthPercent += this.metabolismRate / this.world.engine.currentFPS;
        if (this.healthPercent > 100) {
            this.healthPercent = 100;
        }
        if (this.size < this.maxSize && this.healthPercent >= this.growAtHealth) {
            this.size += Math.random() * this.maxGrowthRate / this.world.engine.currentFPS;
        }
    }

    public ArrayList<Organism> live() {
        ArrayList<Organism> updatedCreatures = new ArrayList<>();
        if (this.healthPercent > 0) {
            updatedCreatures.add(this);
            this.grow();
        }
        return updatedCreatures;
    }

    public ArrayList<Organism> reproduce() {
        // this is just a placeholder method
        return new ArrayList<>();
    }

    public Organism(float xPosition, float yPosition, float size, World world) {
        super(xPosition, yPosition, size, world);
    }
}