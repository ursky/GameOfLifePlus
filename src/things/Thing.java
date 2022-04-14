package things;
import constants.UiConstants;
import quadsearch.Point;
import quadsearch.Region;
import world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Thing {
    public World world;
    public float size;
    public float minSizeToShow = 1;
    public float xPosition;
    public float yPosition;
    public Image itemImage;

    public static float randFloat(float min, float max) {
        Random rand = new Random();
        return rand.nextFloat() * (max - min) + min;
    }

    public boolean isInBounds(float xPos, float yPos) {
        return (xPos >= 0 && yPos >= 0 && xPos < UiConstants.fullDimX && yPos < UiConstants.fullDimY);
    }

    public boolean isInFOV(float xPos, float yPos) {
        return (xPos >= 0 && yPos >= 0 && xPos < UiConstants.povDimX && yPos < UiConstants.povDimY);
    }

    public float calcDistance(float x1, float y1, float x2, float y2) {
        float x_dif = x2 - x1;
        float y_dif = y2 - y1;
        float product = x_dif * x_dif + y_dif * y_dif;
        return (float) Math.sqrt(product);
    }

    public float calcDistanceTo(float x, float y) {
        return calcDistance(this.xPosition, this.yPosition, x, y);
    }

    public ArrayList<Thing> getThingsInRange(float radius) {
        ArrayList<Thing> creaturesInRange = new ArrayList<>();
        Region searchArea = new Region(this.xPosition - radius,
                this.yPosition - radius,
                this.xPosition + radius,
                this.yPosition + radius);
        List<Point> pointsInRange = world.thingCoordinates.search(searchArea, null);
        for (Point point: pointsInRange) {
            Thing thing = world.things.get(point.index);
            float distance = calcDistance(this.xPosition, this.yPosition, thing.xPosition, thing.yPosition);
            if (distance > 0 && distance <= radius + thing.size / 2) {
                creaturesInRange.add(thing);
            }
        }
        return creaturesInRange;
    }

    public Thing(float xPosition, float yPosition, float size, World world) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.world = world;
        this.size = size;
    }
}