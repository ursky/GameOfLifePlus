package game.world;
import game.userIO.UiConstants;
import game.World;
import game.things.Classes.ThingArchive;
import game.things.Classes.Thing;

import java.util.ArrayList;

public class ProceduralGeneration {
    public World world;
    public final int nBins = UiConstants.nProceduralBins;
    public final float binWidthX = UiConstants.fullDimX / (float)nBins;
    public final float binWidthY = UiConstants.fullDimY / (float)nBins;
    public boolean[][] wasRendered = new boolean[nBins][nBins];
    public boolean[][] isRendered = new boolean[nBins][nBins];
    public int[][][] cloneOfBin = new int[nBins][nBins][2];
    public float[] currentCoordinates;
    public int[] currentBins;
    public float loadRangeWidth;
    public float loadRangeHeight;
    public ThingArchive[][] archivedThings = new ThingArchive[nBins][nBins];
    public int safetyScanRange = 10;

    private void updateRenderedRange() {
        this.currentCoordinates = new float[] {
                this.world.engine.userIO.playerPositionX - this.world.engine.userIO.loadRange,
                this.world.engine.userIO.playerPositionY - this.world.engine.userIO.loadRange,
                this.world.engine.userIO.playerPositionX + this.world.engine.userIO.loadRange,
                this.world.engine.userIO.playerPositionY + this.world.engine.userIO.loadRange
        };
        this.currentBins = new int[] {
                (int)(this.currentCoordinates[0] / this.binWidthX),
                (int)(this.currentCoordinates[1] / this.binWidthY),
                (int)(this.currentCoordinates[2] / this.binWidthX),
                (int)(this.currentCoordinates[3] / this.binWidthY)
        };
        this.currentCoordinates = new float[] {
                this.currentBins[0] * this.binWidthX,
                this.currentBins[1] * this.binWidthY,
                this.currentBins[2] * this.binWidthX + this.binWidthX,
                this.currentBins[3] * this.binWidthY + this.binWidthY
        };
        this.loadRangeWidth = this.currentCoordinates[2] - this.currentCoordinates[0];
        this.loadRangeHeight = this.currentCoordinates[3] - this.currentCoordinates[1];
    }

    private void checkRenderedBins() {
        int scanRange = this.safetyScanRange + (int) (this.safetyScanRange * this.world.engine.userIO.zoomLevel);
        for (int i=this.currentBins[0]-scanRange; i<=this.currentBins[2]+scanRange; i++) {
            for (int j=this.currentBins[1]-scanRange; j<=this.currentBins[3]+scanRange; j++) {
                if (this.binIsRendered(i, j)) {
                    this.updateRenderedBin(i, j);
                }
            }
        }
    }

    private void updateRenderedBin(int i, int j) {
        if (!this.wasRendered[i][j]) {
            this.initNewBin(i, j);
            this.wasRendered[i][j] = true;
        }

        if (!this.archivedThings[i][j].empty) {
            ArrayList<Thing> archivedThings = this.archivedThings[i][j].get();
            this.world.things.addAll(archivedThings);
            this.archivedThings[i][j].clear();
        }
        this.isRendered[i][j] = true;
    }

    private boolean binIsRendered(int x, int y) {
        if (x < 0 || y < 0 || x >= this.nBins || y >= this.nBins) {
            return false;
        }
        if (x < this.currentBins[0] || x > this.currentBins[2] || y < this.currentBins[1] || y > this.currentBins[3])
        {
            this.isRendered[x][y] = false;
            return false;
        }
        else {
            this.isRendered[x][y] = true;
            return true;
        }
    }

    private void initNewBin(int i, int j) {
        if (this.world.engine.tracker.frameCounter < UiConstants.fastPreRenderFrames) {
            return;
        }
        float minToInitX = i * this.binWidthX;
        float maxToInitX = minToInitX + this.binWidthX;
        float minToInitY = j * this.binWidthY;
        float maxToInitY = minToInitY + this.binWidthY;
        ArrayList<Thing> clonedThings = this.world.initThings.copyThings(
                minToInitX, minToInitY, maxToInitX, maxToInitY);
        this.world.things.addAll(clonedThings);
        this.world.initThings.initAnimals(minToInitX, minToInitY, maxToInitX, maxToInitY);
        this.world.initThings.initPlants(minToInitX, minToInitY, maxToInitX, maxToInitY);
    }

    private void checkInitThings() {
        if (this.world.engine.tracker.frameCounter == 1) {
            System.out.println("Initializing plants");
            this.world.initThings.initPlants(this.currentCoordinates[0], this.currentCoordinates[1],
                    this.currentCoordinates[2], this.currentCoordinates[3]);
        }
        if (this.world.engine.tracker.frameCounter == UiConstants.fastPreRenderFrames / 2) {
            // halfway though preload add animals
            System.out.println("Initializing animals");
            this.world.initThings.initAnimals(this.currentCoordinates[0], this.currentCoordinates[1],
                    this.currentCoordinates[2], this.currentCoordinates[3]);
        }
    }

    public void updateBins() {
        this.updateRenderedRange();
        this.checkRenderedBins();
        this.checkInitThings();
    }

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