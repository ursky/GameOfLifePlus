package game.quadsearch;

import game.userIO.UiConstants;
import game.things.Classes.Thing;

import java.util.ArrayList;

public class SearchAreas {
    public ArrayList<QuadTree> quadTrees = new ArrayList<>();
    public Region livingArea = new Region(0, 0, UiConstants.fullDimX, UiConstants.fullDimY);
    public int threads;

    public QuadTree getQuadTree(Thing thing) {
        int threadN = thing.getThreadSlice();
        return this.quadTrees.get(threadN);
    }

    public SearchAreas(int threads) {
        this.threads = threads;
        for (int i=0; i<threads; i++) {
            quadTrees.add(new QuadTree(livingArea));
        }
    }
}
