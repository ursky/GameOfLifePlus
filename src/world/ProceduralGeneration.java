package world;
import constants.UiConstants;
import things.Archive;
import things.Thing;

import java.util.ArrayList;

public class ProceduralGeneration {
    final int nBins = UiConstants.nProceduralBins;
    boolean[][] wasRendered = new boolean[nBins][nBins];
    boolean[][] isRendered = new boolean[nBins][nBins];
    World world;
    public float[] currentCoordinates;
    public int[] currentBins;
    Archive[][] archivedThings = new Archive[nBins][nBins];
    public int safetyScanRange = 5;

    public int convertCoordinateToBin(float coordinate, float dimension) {
        return (int) (nBins * coordinate / dimension);
    }

    public float convertBinToCoordinate(int bin, float dimension) {
        return bin * dimension / nBins;
    }

    private void updateRenderedRange() {
        this.currentCoordinates = new float[] {
                this.world.playerPositionX - this.world.engine.loadRange,
                this.world.playerPositionY - this.world.engine.loadRange,
                this.world.playerPositionX + this.world.engine.loadRange,
                this.world.playerPositionY + this.world.engine.loadRange
        };
        this.currentBins = new int[] {
                convertCoordinateToBin(this.currentCoordinates[0], UiConstants.fullDimX),
                convertCoordinateToBin(this.currentCoordinates[1], UiConstants.fullDimY),
                convertCoordinateToBin(this.currentCoordinates[2], UiConstants.fullDimX),
                convertCoordinateToBin(this.currentCoordinates[3], UiConstants.fullDimY)
        };
    }

    private void checkRenderedBins() {
        int scanRange =  this.safetyScanRange + (int) (this.safetyScanRange * this.world.engine.zoomLevel);
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

    private boolean binIsRendered(int i, int j) {
        if (i < 0 || j < 0 || i >= this.nBins || j >= this.nBins) {
            return false;
        }
        if (i < this.currentBins[0] || i >= this.currentBins[2] || j < this.currentBins[1] || j >= this.currentBins[3])
        {
            this.isRendered[i][j] = false;
            return false;
        }
        return true;
    }

    private int getNRenderedBins() {
        int nRendered = 0;
        for (int i=0; i<this.nBins; i++) {
            for (int j = 0; j < this.nBins; j++) {
                if (this.isRendered[i][j]) {
                    nRendered++;
                }
            }
        }
        return nRendered;
    }

    private void initNewBin(int i, int j) {
        float minToInitX = this.convertBinToCoordinate(i, UiConstants.fullDimX);
        float maxToInitX = minToInitX + UiConstants.fullDimX / (float) nBins;
        float minToInitY = this.convertBinToCoordinate(j, UiConstants.fullDimY);
        float maxToInitY = minToInitY + UiConstants.fullDimY / (float) nBins;
        this.world.initThings.initThingsInBin(minToInitX, minToInitY, maxToInitX, maxToInitY);
    }

    public void updateBins() {
        this.updateRenderedRange();
        this.checkRenderedBins();
    }

    public ProceduralGeneration(World world) {
        this.world = world;
        for (int i=0; i<this.nBins; i++) {
            for (int j=0; j<this.nBins; j++) {
                this.isRendered[i][j] = false;
                this.wasRendered[i][j] = false;
                this.archivedThings[i][j] = new Archive();
            }
        }
    }
}
