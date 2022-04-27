package things;
import constants.BlankConstants;
import world.World;

public class Plant extends Thing {
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

    @Override
    public Plant makeBlank() {
        return new Plant(this.xPosition, this.yPosition, this.size, this.world, this.constants);
    }

    @Override
    public void live() {
        this.metabolize();
        if (this.healthPercent <= 0) {return; }
        this.reproduce();
        this.shadeOthers();
        this.updateCoolDowns();
    }

    public Plant(float xPosition, float yPosition, float size, World world, BlankConstants constants) {
        super(xPosition, yPosition, size, world, constants);
    }
}
