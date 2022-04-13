package creatures;
import constants.Constants;
import constants.LifeConstants;
import quadsearch.Point;
import quadsearch.QuadTree;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Tree extends Creature {
    public Tree(float xPosition, float yPosition, float size) {
        super(xPosition, yPosition, 0, 0, size);
        this.maxSize = LifeConstants.treeMaxSize;
        this.growthRate = LifeConstants.treeGrowthRate;
        this.creatureImage = new ImageIcon(LifeConstants.treeImage).getImage();
    }

    public ArrayList<Tree> spreadSeeds() {
        ArrayList<Tree> seedlings = new ArrayList<>();
        if (this.size >= LifeConstants.treeMaturitySize) {
            for (int i=0; i<LifeConstants.treeMaxSeeds; i++) {
                makeSeed(seedlings);
            }
            this.size *= LifeConstants.treeReproductionPenalty;
        }
        return seedlings;
    }

    private void makeSeed(ArrayList<Tree> seedlings) {
        float seedX = randFloat(this.xPosition - LifeConstants.treeDispersalRange,
                xPosition + LifeConstants.treeDispersalRange);
        float seedY = randFloat(this.yPosition - LifeConstants.treeDispersalRange,
                yPosition + LifeConstants.treeDispersalRange);
        if (seedX >= 0 && seedX < Constants.panelDimX && seedY >= 0 && seedY < Constants.panelDimY) {
            Tree seedling = new Tree(seedX, seedY, 1);
            seedlings.add(seedling);
        }
    }

    public void shadeOthers(ArrayList<Creature> creatures, QuadTree creatureCoordinates) {
        for (Point point : this.getCreaturesInRange(LifeConstants.treeShadeRange / 2, creatureCoordinates)) {
            Creature otherTree = creatures.get(point.index);
            if (otherTree instanceof Tree && this.size > otherTree.size
                    && !(this.xPosition == otherTree.xPosition && this.yPosition == otherTree.yPosition)) {
                    otherTree.size += LifeConstants.treeShadePenalty;
                }
            }
        }
    }
