package things;
import world.World;

import java.util.ArrayList;


public class Plant extends Organism {
    public float dispersalRange;
    public float shadeRange;
    public float shadePenalty;
    public float maxSproutTime;
    public int sproutEta = 0;

    public ArrayList<Plant> spreadSeeds() {
        ArrayList<Plant> seedlings = new ArrayList<>();
        if (this.size >= this.reproduceAtSize && this.healthPercent >= this.reproduceAtHealth) {
            for (int i=0; i<this.maxOffsprings; i++) {
                makeSeed(seedlings);
            }
            this.healthPercent *= this.reproductionPenalty;
        }
        return seedlings;
    }

    private void makeSeed(ArrayList<Plant> seedlings) {
        float seedX = randFloat(this.xPosition - this.dispersalRange, this.xPosition + this.dispersalRange);
        float seedY = randFloat(this.yPosition - this.dispersalRange, this.yPosition + this.dispersalRange);
        if (isInBounds(seedX, seedY)
                && calcDistance(this.xPosition, this.yPosition, seedX, seedY) <= this.dispersalRange) {
            Plant seedling = makeClone();
            seedling.size = 1;
            seedling.sproutEta = (int) (Math.random() * this.maxSproutTime * this.world.currentFPS);
            seedling.healthPercent = this.growAtHealth;
            seedling.xPosition = seedX;
            seedling.yPosition = seedY;
            seedlings.add(seedling);
        }
    }

    public Plant makeClone() {
        Plant clone = makeBlank();
        clone.itemImage = this.itemImage;
        clone.maxSize = this.maxSize;
        clone.minSizeToShow = this.minSizeToShow;
        clone.maxGrowthRate = this.maxGrowthRate;
        clone.growAtHealth = this.growAtHealth;
        clone.metabolismRate = this.metabolismRate;
        clone.healthPercent = this.healthPercent;
        clone.reproduceAtSize = this.reproduceAtSize;
        clone.reproduceAtHealth = this.reproduceAtHealth;
        clone.maxOffsprings = this.maxOffsprings;
        clone.reproductionPenalty = this.reproductionPenalty;
        clone.dispersalRange = this.dispersalRange;
        clone.shadeRange = this.shadeRange;
        clone.shadePenalty = this.shadePenalty;
        clone.maxSproutTime = this.maxSproutTime;
        return clone;
    }

    public Plant makeBlank() {
        return new Plant(this.xPosition, this.yPosition, this.size, this.world);
    }

    public void shadeOthers() {
        if (this.size >= this.maxSize * 0.1) {
            for (Thing otherTree : this.getThingsInRange(this.shadeRange)) {
                if (otherTree instanceof Plant && this.size > otherTree.size) {
                    ((Plant) otherTree).healthPercent += this.shadePenalty / this.world.currentFPS;
                }
            }
        }
    }

    @Override
    public ArrayList<Organism> live() {
        ArrayList<Organism> updatedCreatures = new ArrayList<>();
        if (this.healthPercent <= 0) {
            return updatedCreatures;
        }
        if (this.sproutEta <= 0) {
            ArrayList<Plant> offsprings = this.spreadSeeds();
            updatedCreatures.addAll(offsprings);
            this.grow();
            this.shadeOthers();
        }
        this.sproutEta--;
        updatedCreatures.add(this);
        return updatedCreatures;
    }

    public Plant(float xPosition, float yPosition, float size, World world) {
        super(xPosition, yPosition, size, world);
    }
}
