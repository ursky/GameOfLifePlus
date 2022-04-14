package things;

import constants.BushConstants;
import world.World;

import javax.swing.*;

public class Bush extends Plant {
    @Override
    public Plant makeBlank() {
        return new Bush(this.xPosition, this.yPosition, this.size, this.world);
    }

    public Bush(float xPosition, float yPosition, float size, World world) {
        super(xPosition, yPosition, size, world);
        this.itemImage = new ImageIcon(BushConstants.imagePath).getImage();

        this.maxSize = BushConstants.maxSize;
        this.minSizeToShow = BushConstants.minSizeToShow;
        this.maxGrowthRate = BushConstants.maxGrowthRate;
        this.metabolismRate = BushConstants.metabolismRate;
        this.reproduceAtSize = BushConstants.reproduceAtSize;
        this.reproduceAtHealth = BushConstants.reproduceAtHealth;
        this.maxOffsprings = BushConstants.maxOffsprings;
        this.reproductionPenalty = BushConstants.reproductionPenalty;
        this.dispersalRange = BushConstants.dispersalRange;
        this.shadeRange = BushConstants.shadeRange;
        this.shadePenalty = BushConstants.shadePenalty;
        this.growAtHealth = BushConstants.growAtHealth;
    }
}
