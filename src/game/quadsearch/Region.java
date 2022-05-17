package game.quadsearch;

/**
 * Define a single area on a 2D plane
 */
public class Region {
    private final float x1;
    private final float y1;
    private final float x2;
    private final float y2;

    /**
     * Is this point in this region?
     * @param point: point on 2D plane to check
     * @return: is this point in this region
     */
    public boolean containsPoint(Point point) {
        return point.getX() >= this.x1
                && point.getX() < this.x2
                && point.getY() >= this.y1
                && point.getY() < this.y2;
    }

    /**
     * Does another region overlap this region?
     * @param testRegion: another region
     * @return: do they overlap?
     */
    public boolean doesOverlap(Region testRegion) {
        if (testRegion.getX2() < this.getX1()) {
            return false;
        }
        if (testRegion.getX1() > this.getX2()) {
            return false;
        }
        if (testRegion.getY1() > this.getY2()) {
            return false;
        }
        return !(testRegion.getY2() < this.getY1());
    }

    /**
     * Make a new region for a given quad tree quadrant
     * @param quadrantIndex: quad tree search index
     * @return: region new region representing the quadrant
     */
    public Region getQuadrant(int quadrantIndex) {
        float quadrantWidth = (this.x2 - this.x1) / 2;
        float quadrantHeight = (this.y2 - this.y1) / 2;

        // 0=SW, 1=NW, 2=NE, 3=SE
        switch (quadrantIndex) {
            case 0:
                return new Region(x1, y1, x1 + quadrantWidth, y1 + quadrantHeight);
            case 1:
                return new Region(x1, y1 + quadrantHeight, x1 + quadrantWidth, y2);
            case 2:
                return new Region(x1 + quadrantWidth, y1 + quadrantHeight, x2, y2);
            case 3:
                return new Region(x1 + quadrantWidth, y1, x2, y1 + quadrantHeight);
        }
        return null;
    }

    /**
     * Initialize bounds of region
     * @param x1: bounds on 2D plane
     * @param y1: bounds on 2D plane
     * @param x2: bounds on 2D plane
     * @param y2: bounds on 2D plane
     */
    public Region(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    /**
     * Bounds getter
     * @return: region bound
     */
    public float getX1() {
        return this.x1;
    }

    /**
     * Bounds getter
     * @return: region bound
     */
    public float getX2() {
        return this.x2;
    }

    /**
     * Bounds getter
     * @return: region bound
     */
    public float getY1() {
        return this.y1;
    }

    /**
     * Bounds getter
     * @return: region bound
     */
    public float getY2() {
        return this.y2;
    }
}