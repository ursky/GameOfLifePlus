import constants.Constants;
import constants.LifeConstants;
import creatures.Creature;
import creatures.Tree;
import quadsearch.Region;
import quadsearch.QuadTree;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class World {
    ArrayList<Creature> creatures = new ArrayList<>();
    QuadTree creatureCoordinates;
    Region livingArea = new Region(0, 0, Constants.panelDimX, Constants.panelDimY);

    World() {
        System.out.println("Creating the world...");
        initializeTrees();
    }

    public void initializeTrees() {
        for (int i=0; i<LifeConstants.treesStartingCount; i++) {
            float randX = (float) Math.random() * Constants.panelDimX;
            float randY = (float) Math.random() * Constants.panelDimY;
            double randSize = Math.random() * LifeConstants.treeMaxSize;
            Tree tree = new Tree(randX, randY, (int) randSize);
            creatures.add(tree);
        }
    }

    public void updateCreatureCoordinates() {
        creatureCoordinates = new QuadTree(livingArea);
        for (int i = 0; i < creatures.size(); i++) {
            quadsearch.Point point = new quadsearch.Point(i, creatures.get(i).xPosition, creatures.get(i).yPosition);
            creatureCoordinates.addPoint(point);
        }
    }

    public void updateCreatures() {
        ArrayList<Creature> newCreatures = new ArrayList<>();
        for (int i = 0; i < creatures.size(); i++) {
            Creature creature = creatures.get(i);
            ArrayList<Creature> updatedCreatures = creature.live();
            newCreatures.addAll(updatedCreatures);
            creature.interact(this.creatures, this.creatureCoordinates);
        }
        creatures = newCreatures;
    }

    public void updateWorld() {
        updateCreatureCoordinates();
        updateCreatures();
    }
}
