package things;

import constants.TreeConstants;
import world.World;
import javax.swing.*;

public class Tree extends Plant {
    @Override
    public Plant makeBlank() {
        return new Tree(this.xPosition, this.yPosition, this.size, this.world);
    }

    public Tree(float xPosition, float yPosition, float size, World world) {
        super(xPosition, yPosition, size, world);
        this.itemImage = new ImageIcon(TreeConstants.imagePath).getImage();

        this.maxSize = TreeConstants.maxSize;
        this.minSizeToShow = TreeConstants.minSizeToShow;
        this.maxGrowthRate = TreeConstants.maxGrowthRate;
        this.metabolismRate = TreeConstants.metabolismRate;
        this.reproduceAtSize = TreeConstants.reproduceAtSize;
        this.reproduceAtHealth = TreeConstants.reproduceAtHealth;
        this.maxOffsprings = TreeConstants.maxOffsprings;
        this.reproductionPenalty = TreeConstants.reproductionPenalty;
        this.dispersalRange = TreeConstants.dispersalRange;
        this.shadeRange = TreeConstants.shadeRange;
        this.shadePenalty = TreeConstants.shadePenalty;
        this.growAtHealth = TreeConstants.growAtHealth;
    }
}
