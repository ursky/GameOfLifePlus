package engine.quadsearch;

public class Point {
    public final int index;
    public float x;
    public float y;

    public Point(int index, float x, float y) {
        this.index = index;
        this.x = x;
        this.y = y;
    }

    public float getIndex() {
        return this.index;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public String toString() {
        String index = Integer.toString(this.index);
        String strX = Float.toString(this.x);
        String strY = Float.toString(this.y);
        return index + ": " + strX + "," + strY;
    }
}