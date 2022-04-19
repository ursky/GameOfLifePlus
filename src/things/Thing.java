package things;
import constants.BlankConstants;
import constants.UiConstants;
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
    public BufferedImage itemImage;
    public float currentRotation = 0;
    public float currentOpacity = 255;
    public boolean isSeed = false;
    public float healthPercent = 100;
    public int coolDownFrames = 1;
    public int coolDown = 0;

    public void updateCoolDowns() {
        if (this.world.engine.inView(this)) {
            this.coolDownFrames = this.constants.onScreenCoolDown;
        }
        else {
            this.coolDownFrames = this.constants.offScreenCoolDown;
        }
        this.coolDown = this.coolDownFrames - 1;
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
        return (this.xPosition >= this.world.playerPositionX - this.world.engine.loadRange
                && this.xPosition < this.world.playerPositionX + this.world.engine.loadRange
                && this.yPosition >= this.world.playerPositionY - this.world.engine.loadRange
                && this.yPosition < this.world.playerPositionY + this.world.engine.loadRange);
    }

    public int getThreadSlice() {
        float positionInRendered = this.xPosition - this.world.engine.renderedLeftX + this.world.engine.threadBuffer;
        if (positionInRendered >= 2 * this.world.engine.loadRange) {
            positionInRendered -= 2 * this.world.engine.loadRange;
        }
        return (int) (positionInRendered / this.world.engine.threadWidth);
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

    public void initImage() {
        this.itemImage = this.constants.mainImage.getImage(this.currentRotation, this.currentOpacity);
    }

    public Thing(float xPosition, float yPosition, float size, World world, BlankConstants constants) {
        this.constants = constants;
        this.initImage();
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.world = world;
        this.size = size;
    }
}