package game.world;
import game.constants.UiConstants;
import game.quadsearch.Region;
import game.world.things.Classes.ThingArchive;
import game.world.things.Classes.Thing;

import java.util.ArrayList;

/**
 * This class handles which things are rendered at any given point in time based on the camera movement
 */
public class ProceduralGeneration {
    public World world;
    public final int nBins = UiConstants.nProceduralBins;
    public final float binWidthX = UiConstants.fullDimX / (float)nBins;
    public final float binWidthY = UiConstants.fullDimY / (float)nBins;
    public boolean[][] wasRendered = new boolean[nBins][nBins];
    public boolean[][] isRendered = new boolean[nBins][nBins];
    public int[][][] cloneOfBin = new int[nBins][nBins][2];
    public Region currentCoordinates = new Region(0, 0, 0, 0);
    public int[] currentBins;
    public ThingArchive[][] archivedThings = new ThingArchive[nBins][nBins];
    public int safetyScanRange = 10;

    /**
     * Update the rendered range and active bin range based on current camera position
     */
    private void updateRenderedRange() {
        this.currentCoordinates = new Region(
                this.world.game.userIO.playerPositionX - this.world.game.userIO.loadRange,
                this.world.game.userIO.playerPositionY - this.world.game.userIO.loadRange,
                this.world.game.userIO.playerPositionX + this.world.game.userIO.loadRange,
                this.world.game.userIO.playerPositionY + this.world.game.userIO.loadRange);
        this.currentBins = new int[] {
                (int)(this.currentCoordinates.getX1() / this.binWidthX),
                (int)(this.currentCoordinates.getY1() / this.binWidthY),
                (int)(this.currentCoordinates.getX2() / this.binWidthX),
                (int)(this.currentCoordinates.getY2() / this.binWidthY)
        };
        this.currentCoordinates = new Region(
                this.currentBins[0] * this.binWidthX,
                this.currentBins[1] * this.binWidthY,
                this.currentBins[2] * this.binWidthX + this.binWidthX,
                this.currentBins[3] * this.binWidthY + this.binWidthY
        );
    }

    /**
     * Go over the bins of the field and check if they are in view. If not, turn them off.
     */
    private void checkRenderedBins() {
        int scanRange = this.safetyScanRange + (int) (this.safetyScanRange * this.world.game.userIO.zoomLevel);
        for (int i=this.currentBins[0]-scanRange; i<=this.currentBins[2]+scanRange; i++) {
            for (int j=this.currentBins[1]-scanRange; j<=this.currentBins[3]+scanRange; j++) {
                if (this.binIsRendered(i, j)) {
                    // if the bin is in view it needs to be updated
                    this.updateRenderedBin(i, j);
                }
            }
        }
    }

    /**
     * Store in the class memory that this given bin is, in fact, active.
     * @param i: x-index of active bin
     * @param j: y-index of active bin
     */
    private void updateRenderedBin(int i, int j) {
        if (!this.wasRendered[i][j]) {
            this.initNewBin(i, j);
            this.wasRendered[i][j] = true;
        }

        if (!this.archivedThings[i][j].empty) {
            ArrayList<Thing> archivedThings = this.archivedThings[i][j].things;
            this.world.things.addAll(archivedThings);
            this.archivedThings[i][j].clear();
        }
        this.isRendered[i][j] = true;
    }

    /**
     * Check if a given bin should be active (is close to the player FOV)
     * @param i: x-index of active bin
     * @param j: y-index of active bin
     * @return: true if the bin should be active
     */
    private boolean binIsRendered(int i, int j) {
        if (i < 0 || j < 0 || i >= this.nBins || j >= this.nBins) {
            return false;
        }
        if (i < this.currentBins[0] || i > this.currentBins[2] || j < this.currentBins[1] || j > this.currentBins[3])
        {
            this.isRendered[i][j] = false;
            return false;
        }
        else {
            this.isRendered[i][j] = true;
            return true;
        }
    }

    /**
     * Initialize a bin that was turned on for the first time
     * @param i: x-index of active bin
     * @param j: y-index of active bin
     */
    private void initNewBin(int i, int j) {
        if (this.world.game.tracker.frameCounter < UiConstants.fastPreRenderFrames) {
            return;
        }
        Region region = new Region(
                i * this.binWidthX,
                j * this.binWidthY,
                i * this.binWidthX + this.binWidthX,
                j * this.binWidthY + this.binWidthY);
        ArrayList<Thing> clonedThings = this.world.initThings.copyThings(region);
        this.world.things.addAll(clonedThings);
        this.world.initThings.initAnimals(region);
        this.world.initThings.initPlants(region);
    }

    /**
     * Check if this is a good moment to first animals and plants to the game (happens in the firs loading period)
     */
    private void checkInitThings() {
        if (this.world.game.tracker.frameCounter == 1) {
            System.out.println("Initializing plants");
            this.world.initThings.initPlants(this.currentCoordinates);
        }
        if (this.world.game.tracker.frameCounter == UiConstants.fastPreRenderFrames / 2) {
            // halfway though preload add animals
            System.out.println("Initializing animals");
            this.world.initThings.initAnimals(this.currentCoordinates);
        }
    }

    /**
     * Run the bin update pipeline above
     */
    public void updateBins() {
        // update which bins are active or not
        this.updateRenderedRange();
        // turn on appropriate bins
        this.checkRenderedBins();
        // initialize creatures if this is just the start of the simulatiton
        this.checkInitThings();
    }

    /**
     * Create object
     * @param world: game world
     */
    public ProceduralGeneration(World world) {
        this.world = world;
        for (int i=0; i<this.nBins; i++) {
            for (int j=0; j<this.nBins; j++) {
                this.isRendered[i][j] = false;
                this.wasRendered[i][j] = false;
                this.archivedThings[i][j] = new ThingArchive();
                this.cloneOfBin[i][j][0] = 0;
                this.cloneOfBin[i][j][1] = 0;
            }
        }
    }
}
