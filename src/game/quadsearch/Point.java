package game.quadsearch;

/**
 * This class stores a single 2d point
 */
public class Point {
    public int index;
    public float x;
    public float y;

    /**
     * Store 2D point
     * @param index: ID of this point
     * @param x: position on 2D plane
     * @param y: position on 2D plane
     */
    public Point(int index, float x, float y) {
        this.index = index;
        this.x = x;
        this.y = y;
    }

    /**
     * ID/index getter. Note this is used as an index to find the creature at this position.
     * @return: index
     */
    public float getIndex() {
        return this.index;
    }

    /**
     * X getter
     * @return: x
     */
    public float getX() {
        return this.x;
    }

    /**
     * Y getter
     * @return: y
     */
    public float getY() {
        return this.y;
    }

    /**
     * X setter
     * @param x: new value
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Y setter
     * @param y: new value
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Change index of point
     * @param i: new index
     */
    public void setIndex(int i) {
        this.index = i;
    }

    /**
     * Calculate distance to another point
     * @param otherPoint: the other point
     * @return: distance
     */
    public float getDistanceTo(Point otherPoint) {
        float distX = this.getXDistanceTo(otherPoint);
        float distY = this.getYDistanceTo(otherPoint);
        return (float) Math.sqrt(distX * distX + distY * distY);
    }

    /**
     * Get X axis distance to another point; not it can be negative
     * @param otherPoint: the other point
     * @return: distance
     */
    public float getXDistanceTo(Point otherPoint) {
        return otherPoint.getX() - this.x;
    }

    /**
     * Get Y axis distance to another point; not it can be negative
     * @param otherPoint: the other point
     * @return: distance
     */
    public float getYDistanceTo(Point otherPoint) {
        return otherPoint.getY() - this.y;
    }

    /**
     * Make a string representing the point (for reporting)
     * @return: string with point data
     */
    public String toString() {
        String index = Integer.toString(this.index);
        String strX = Float.toString(this.x);
        String strY = Float.toString(this.y);
        return index + ": " + strX + "," + strY;
    }
}