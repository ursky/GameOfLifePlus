package game.quadsearch;
import java.util.List;
import java.util.ArrayList;

/**
 * Make a quad tree search data structure for finding things in a range on a 2D field in O(log n) time
 */
public class QuadTree {
    private static final int MAX_POINTS = 3;
    private final Region area;
    private final List<Point> points = new ArrayList<>();
    private final List<QuadTree> quadTrees = new ArrayList<>();

    /**
     * Recursively find all the points that fall in a given range by breaking down the range in smaller sections
     * @param searchRegion: range to search in
     * @param matches: points found in this range to add to
     * @return: all matches found in this range
     */
    public List<Point> search(Region searchRegion, List<Point> matches) {
        if (matches == null) {
            matches = new ArrayList<>();
        }
        if (!this.area.doesOverlap(searchRegion)) {
            return matches;
        } else {
            for (Point point : points) {
                if (searchRegion.containsPoint(point)) {
                    matches.add(point);
                }
            }
            if (this.quadTrees.size() > 0) {
                for (int i = 0; i < 4; i++) {
                    quadTrees.get(i).search(searchRegion, matches);
                }
            }
        }
        return matches;
    }

    /**
     * Add point from the 2D field into the quad tree data structure. If the quadrant is getting crowded, break down
     * the area into smaller quadrants.
     * @param point: point to save
     * @return: was this point added?
     */
    public boolean addPoint(Point point) {
        if (this.area.containsPoint(point)) {
            if (this.points.size() < MAX_POINTS) {
                this.points.add(point);
                return true;
            } else {
                if (this.quadTrees.size() == 0) {
                    createQuadrants();
                }
                return addPointToOneQuadrant(point);
            }
        }
        return false;
    }

    /**
     * Add a point to a given quadrant in the quad tree data strucure
     * @param point: point to add
     * @return: was the point added?
     */
    private boolean addPointToOneQuadrant(Point point) {
        boolean isPointAdded;
        for (int i = 0; i < 4; i++) {
            isPointAdded = this.quadTrees.get(i).addPoint(point);
            if (isPointAdded)
                return true;
        }
        return false;
    }

    /**
     * Initialize new quadrant in quad tree data structure
     */
    private void createQuadrants() {
        Region region;
        for (int i = 0; i < 4; i++) {
            region = this.area.getQuadrant(i);
            quadTrees.add(new QuadTree(region));
        }
    }

    /**
     * Initialize quad tree search are
     * @param area: region to index
     */
    public QuadTree(Region area) {
        this.area = area;
    }
}