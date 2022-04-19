package things;
import constants.BlankConstants;
import constants.UiConstants;
import engine.ImageStack;
import quadsearch.Point;
import quadsearch.Region;
import world.World;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class Thing {
    public World world;
    public BlankConstants constants;
    public float size;
    public float xPosition;
    public float yPosition;
    public int xBin;
    public int yBin;
    public BufferedImage itemImage;
    public float currentRotation = 0;
    public float currentOpacity = 255;
    public boolean isSeed = false;
    public float healthPercent = 100;
    public int coolDownFrames = 1;
    public int coolDown = 0;

    public void updateCoolDowns() {
        if (this.isInView()) {
            this.coolDownFrames = this.constants.onScreenCoolDown;
        }
        else {
            this.coolDownFrames = this.constants.offScreenCoolDown;
        }
        this.coolDown = this.coolDownFrames - 1;
    }

    public boolean isInView() {
        float halfSize = this.size / 2;
        return (this.xPosition + halfSize > this.world.engine.positionsInView[0]
                && this.xPosition - halfSize < this.world.engine.positionsInView[1]
                && this.yPosition + halfSize > this.world.engine.positionsInView[2]
                && this.yPosition - halfSize < this.world.engine.positionsInView[3]);
    }

    public boolean isInBounds(float xPos, float yPos) {
        return (xPos >= 0 && yPos >= 0 && xPos < UiConstants.fullDimX && yPos < UiConstants.fullDimY);
    }

    public float calcDistance(float x1, float y1, float x2, float y2) {
        float x_dif = x2 - x1;
        float y_dif = y2 - y1;
        float product = x_dif * x_dif + y_dif * y_dif;
        return (float) Math.sqrt(product);
    }

    public ArrayList<Thing> getThingsInRange(float radius) {
        ArrayList<Thing> creaturesInRange = new ArrayList<>();
        Region searchArea = new Region(
                this.xPosition - radius,
                this.yPosition - radius,
                this.xPosition + radius,
                this.yPosition + radius);
        List<Point> pointsInRange = world.searchAreas.getQuadTree(this).search(searchArea, null);
        for (Point point: pointsInRange) {
            Thing thing = world.things.get(point.index);
            float distance = calcDistance(this.xPosition, this.yPosition, thing.xPosition, thing.yPosition);
            if (distance > 0 && distance <= radius + thing.size / 2) {
                creaturesInRange.add(thing);
            }
        }
        return creaturesInRange;
    }

    public boolean isRendered() {
        return this.world.engine.procedural.isRendered[this.xBin][this.yBin];
    }

    public void printDetails() {
        System.out.println(
                "thing:(" + this.xPosition + ", " + this.yPosition + ") "
                + "thingBin:(" + this.xBin + ", " + this.yBin + ") "
                + this.world.engine.procedural.isRendered[this.xBin][this.yBin] + " "
                + "player:(" + this.world.playerPositionX + ", " + this.world.playerPositionY + ") "
                + "range:(" + this.world.engine.loadRange + ": "
                    + this.world.engine.procedural.currentCoordinates[0] + ", "
                    + this.world.engine.procedural.currentCoordinates[1] + ", "
                    + this.world.engine.procedural.currentCoordinates[2] + ", "
                    + this.world.engine.procedural.currentCoordinates[3] + ") "
                + "binRange:(" + this.world.engine.procedural.currentBins[0] + ", "
                    + this.world.engine.procedural.currentBins[1] + ", "
                    + this.world.engine.procedural.currentBins[2] + ", "
                    + this.world.engine.procedural.currentBins[3] + ")"
        );
    }

    public int getThreadSlice() {
        float renderedLeftX = this.world.engine.procedural.currentCoordinates[0];
        float positionInRendered = this.xPosition - renderedLeftX + this.world.engine.threadBuffer;
        float threadWidth = this.world.engine.procedural.loadRangeWidth / UiConstants.threadCount;

        if (positionInRendered >= this.world.engine.procedural.loadRangeWidth) {
            positionInRendered -= this.world.engine.procedural.loadRangeWidth;
        }
        return (int) (positionInRendered / threadWidth);
    }

    public Thing makeClone() {
        Thing clone = makeBlank();
        clone.currentRotation = this.currentRotation;
        clone.currentOpacity = this.currentOpacity;
        clone.itemImage = this.constants.mainImage.getImage(this.currentRotation, this.currentOpacity);
        clone.constants = this.constants;
        clone.healthPercent = this.healthPercent;
        clone.coolDown = this.coolDown;
        return clone;
    }

    public Thing makeBlank() {
        return new Thing(this.xPosition, this.yPosition, this.size, this.world, this.constants);
    }

    public void initImage(ImageStack imageStack) {
        this.itemImage = imageStack.getImage(this.currentRotation, this.currentOpacity);
    }

    public void updateBin() {
        this.xBin = (int) (this.xPosition / this.world.engine.procedural.binWidthX);
        this.yBin = (int) (this.yPosition / this.world.engine.procedural.binWidthY);
    }

    public Thing(float xPosition, float yPosition, float size, World world, BlankConstants constants) {
        this.constants = constants;
        this.initImage(this.constants.mainImage);
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.world = world;
        this.size = size;
        this.updateBin();
    }
}