package world;
import constants.UiConstants;

import java.util.Arrays;

public class Procedural {
    final int nBins = UiConstants.nProceduralBins;
    boolean[][] isRendered = new boolean[nBins][nBins];
    World world;
    float minToRenderX, minToRenderY, maxToRenderX, maxToRenderY;
    int minBinToRenderX, minBinToRenderY, maxBinToRenderX, maxBinToRenderY;

    public int convertCoordinateToBin(float coordinate, float dimension) {
        return (int) (nBins * coordinate / dimension);
    }

    public float convertBinToCoordinate(int bin, float dimension) {
        return bin * dimension / nBins;
    }

    private void updateRenderedRange() {
        this.minToRenderX = this.world.playerPositionX - UiConstants.loadRange;
        this.minToRenderY = this.world.playerPositionY - UiConstants.loadRange;
        this.maxToRenderX = this.world.playerPositionX + UiConstants.loadRange;
        this.maxToRenderY = this.world.playerPositionY + UiConstants.loadRange;
        this.minBinToRenderX = convertCoordinateToBin(this.minToRenderX, UiConstants.fullDimX);
        this.minBinToRenderY = convertCoordinateToBin(this.minToRenderY, UiConstants.fullDimY);
        this.maxBinToRenderX = convertCoordinateToBin(this.maxToRenderX, UiConstants.fullDimX);
        this.maxBinToRenderY = convertCoordinateToBin(this.maxToRenderY, UiConstants.fullDimY);
    }

    private void checkRenderedBins() {
        for (int i=this.minBinToRenderX; i<=this.maxBinToRenderX; i++) {
            for (int j=this.minBinToRenderY; j<=this.maxBinToRenderY; j++) {
                if (i < 0 || j < 0 || i >= this.nBins || j >= this.nBins) {
                    continue;
                }
                if (!this.isRendered[i][j]) {
                    this.initNewBin(i, j);
                    this.isRendered[i][j] = true;
                }
            }
        }
    }

    private void initNewBin(int i, int j) {
        float minToInitX = this.convertBinToCoordinate(i, UiConstants.fullDimX);
        float maxToInitX = minToInitX + UiConstants.fullDimX / (float) nBins;
        float minToInitY = this.convertBinToCoordinate(j, UiConstants.fullDimY);
        float maxToInitY = minToInitY + UiConstants.fullDimY / (float) nBins;
        this.world.initializeInRange(minToInitX, minToInitY, maxToInitX, maxToInitY);
    }

    public void updateBins() {
        updateRenderedRange();
        checkRenderedBins();
    }

    public Procedural(World world) {
        for (boolean[] booleans : this.isRendered) Arrays.fill(booleans, false);
        this.world = world;
    }
}
