package game.quadsearch;

import game.constants.UiConstants;
import game.world.things.Classes.Thing;

import java.util.ArrayList;

/**
 * Organized groupings of different regions of the map designated to their respective QuadTree search threads.
 * This is done to be able to construct separate quad trees for each thread, allowing the neighbor search
 * to support multithreading. The game 2d field is split vertically into N thread regions.
 * The division lines moves/shifts to the right a little every frame.
 */
public class SearchAreas {
    public ArrayList<QuadTree> quadTrees = new ArrayList<>();
    public Region livingArea = new Region(0, 0, UiConstants.fullDimX, UiConstants.fullDimY);
    public int threads;

    /**
     * Find which thread a creature belongs to this frame and save the information
     * @param thing: creature to check
     * @return: index of the thread it belongs to
     */
    public QuadTree getQuadTree(Thing thing) {
        int threadN = thing.getThreadSlice();
        return this.quadTrees.get(threadN);
    }

    /**
     * Initialize the search areas that indicate which thread index each creature is in
     * @param threads: number of threads
     */
    public SearchAreas(int threads) {
        this.threads = threads;
        for (int i=0; i<threads; i++) {
            quadTrees.add(new QuadTree(livingArea));
        }
    }
}
