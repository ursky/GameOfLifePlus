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
        this.healthPercent += this.metabolismRate;
        if (this.healthPercent > 100) {
            this.healthPercent = 100;
        }
        if (this.size < this.maxSize && this.healthPercent >= this.growAtHealth) {
            this.size += Math.random() * this.maxGrowthRate;
        }
    }

    public ArrayList<Organism> live() {
        ArrayList<Organism> updatedCreatures = new ArrayList<>();
        if (this.healthPercent > 0) {
            updatedCreatures.add(this);
            this.grow();

            if (this instanceof Plant) {
                ArrayList<Plant> offsprings = ((Plant) this).spreadSeeds();
                updatedCreatures.addAll(offsprings);
                ((Plant) this).shadeOthers();

            }
            if (this instanceof Animal) {
                ((Animal) this).move();
                ArrayList<Organism> offsprings = reproduce();
                updatedCreatures.addAll(offsprings);
            }
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