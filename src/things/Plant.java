package things;
import world.World;

public class Plant extends Organism {
    public float dispersalRange;
    public float shadeRange;
    public float shadePenalty;
    public float maxSproutTime;

    public void spreadSeeds() {
        if (this.size >= this.reproduceAtSize && this.healthPercent >= this.reproduceAtHealth) {
            for (int i=0; i<this.maxOffsprings; i++) {
                makeSeed();
            }
            this.healthPercent *= this.reproductionPenalty;
        }
    }

    private void makeSeed() {
        float seedX = randFloat(this.xPosition - this.dispersalRange, this.xPosition + this.dispersalRange);
        float seedY = randFloat(this.yPosition - this.dispersalRange, this.yPosition + this.dispersalRange);
        if (isInBounds(seedX, seedY)
                && calcDistance(this.xPosition, this.yPosition, seedX, seedY) <= this.dispersalRange) {
            Plant seedling = makeClone();
            seedling.size = 1;
            seedling.coolDown = (int) (Math.random() * this.maxSproutTime * this.coolDownFrames
                    * this.world.engine.currentFPS);
            seedling.healthPercent = this.growAtHealth;
            seedling.xPosition = seedX;
            seedling.yPosition = seedY;
            this.world.newThings.add(seedling);
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
        clone.coolDown = this.coolDown;
        clone.maxCoolDownFrames = this.maxCoolDownFrames;
        return clone;
    }

    public Plant makeBlank() {
        return new Plant(this.xPosition, this.yPosition, this.size, this.world);
    }

    public void shadeOthers() {
        if (this.size >= this.maxSize * 0.1) {
            for (Thing otherTree : this.getThingsInRange(this.shadeRange)) {
                if (otherTree instanceof Plant && this.size > otherTree.size) {
                    otherTree.healthPercent += this.shadePenalty * this.coolDownFrames / this.world.engine.currentFPS;
                }
            }
        }
    }

    @Override
    public void live() {
        if (this.healthPercent > 0) {
            this.spreadSeeds();
            this.grow();
            this.shadeOthers();
            this.updateCoolDowns();
        }
    }

    public Plant(float xPosition, float yPosition, float size, World world) {
        super(xPosition, yPosition, size, world);
    }
}
