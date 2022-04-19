package things;
import constants.BlankConstants;
import utilities.Random;
import world.World;

public class Plant extends Organism {
    public void spreadSeeds() {
        if (this.healthPercent >= this.constants.reproduceAtHealth && this.size >= this.constants.reproduceAtSize) {
            for (int i=0; i<this.constants.maxOffsprings; i++) {
                makeSeed();
            }
            this.healthPercent *= this.constants.reproductionPenalty;
        }
    }

    private void makeSeed() {
        float seedX = Random.randFloat(this.xPosition - this.constants.dispersalRange,
                this.xPosition + this.constants.dispersalRange);
        float seedY = Random.randFloat(this.yPosition - this.constants.dispersalRange,
                this.yPosition + this.constants.dispersalRange);
        if (isInBounds(seedX, seedY)
                && calcDistance(this.xPosition, this.yPosition, seedX, seedY) <= this.constants.dispersalRange) {
            Thing seedling = makeClone();
            seedling.size = seedling.constants.startSize;
            seedling.coolDown = (int) (Math.random() * this.constants.sproutTime * this.coolDownFrames
                    * this.world.engine.currentFPS);
            seedling.healthPercent = seedling.constants.startHealth;
            seedling.xPosition = seedX;
            seedling.yPosition = seedY;
            seedling.isSeed = true;
            seedling.currentRotation = Random.randFloat(0, 360);
            seedling.initImage(seedling.constants.youngImage);
            this.world.newThings.add(seedling);
        }
    }

    public void shadeOthers() {
        float shadeRange = this.constants.maxShadeRange * this.size / this.constants.maxSize;
        if (shadeRange > 1) {
            for (Thing otherTree : this.getThingsInRange(shadeRange)) {
                if (otherTree instanceof Plant && this.size >= otherTree.size) {
                    otherTree.healthPercent += this.constants._shadePenalty * this.coolDownFrames;
                }
            }
        }
    }

    @Override
    public Plant makeBlank() {
        return new Plant(this.xPosition, this.yPosition, this.size, this.world, this.constants);
    }

    @Override
    public void live() {
        this.spreadSeeds();
        this.grow();
        this.shadeOthers();
        this.updateCoolDowns();
    }

    public Plant(float xPosition, float yPosition, float size, World world, BlankConstants constants) {
        super(xPosition, yPosition, size, world, constants);
    }
}
