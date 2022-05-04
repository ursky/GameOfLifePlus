package engine.world;
import constants.UiConstants;
import engine.World;
import engine.utilities.Utils;
import things.Classes.ThingArchive;
import things.Classes.Thing;

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
    public int[] initialBins;
    public float loadRangeWidth;
    public float loadRangeHeight;
    public ThingArchive[][] archivedThings = new ThingArchive[nBins][nBins];
    public int safetyScanRange = 1;

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
        if (this.world.engine.frameCounter % 100 == 0) {
            this.saveInitialBins();
        }
    }

    private void saveInitialBins() {
        this.initialBins = new int[] {
                this.currentBins[0],
                this.currentBins[1],
                this.currentBins[2],
                this.currentBins[3]
        };
    }

    private void checkRenderedBins() {
        int scanRange = this.safetyScanRange + (int) (this.safetyScanRange * this.world.engine.userIO.zoomLevel);
        for (int i=this.currentBins[0]-scanRange; i<=this.currentBins[2]+scanRange; i++) {
            for (int j=this.currentBins[1]-scanRange; j<=this.currentBins[3]+scanRange; j++) {
                if (this.binRenderedUpdate(i, j)) {
                    this.updateRenderedBin(i, j);
                }
            }
        }
    }

    private void updateRenderedBin(int i, int j) {
        if (!this.wasRendered[i][j]) {
            this.cloneToNewBin(i, j);
            this.wasRendered[i][j] = true;
        }

        if (!this.archivedThings[i][j].empty) {
            ArrayList<Thing> archivedThings = this.archivedThings[i][j].get();
            this.world.things.addAll(archivedThings);
            this.archivedThings[i][j].clear();
        }
        this.isRendered[i][j] = true;
    }

    private void cloneToNewBin(int binX, int binY) {
        if (this.world.engine.frameCounter == 0) {
            return;
        }

        int[] sourceBin = chooseSourceBin(binX, binY);
        float[] sourceCoordinates = new float[] {
                Math.abs(sourceBin[0]) * this.binWidthX,
                Math.abs(sourceBin[0]) * this.binWidthX + this.binWidthX,
                Math.abs(sourceBin[1]) * this.binWidthY,
                Math.abs(sourceBin[1]) * this.binWidthY + this.binWidthY
        };
        ArrayList<Thing> thingsToScan;

        if (this.binIsRendered(sourceBin[0], sourceBin[1])) {
            System.out.println("scanning world");
            thingsToScan = this.world.things;
        }
        else {
            System.out.println("scanning archive");
            thingsToScan = this.archivedThings[Math.abs(sourceBin[0])][Math.abs(sourceBin[1])].get();
        }

        ArrayList<Thing> copiedThings = new ArrayList<>();
        for (Thing thing : thingsToScan) {
            if (Utils.inBounds(thing.xPosition, sourceCoordinates[0], sourceCoordinates[1])
                    && Utils.inBounds(thing.yPosition, sourceCoordinates[2], sourceCoordinates[3])) {
                float[] newPositions = transposeCoordinate(thing, binX, binY, sourceBin, sourceCoordinates);
                Thing newThing = this.copyThingTo(thing, newPositions[0], newPositions[1]);
                copiedThings.add(newThing);
            }
        }
        this.world.things.addAll(copiedThings);
    }

    private boolean binIsRendered(int binX, int binY) {
        binX = Math.abs(binX);
        binY = Math.abs(binY);
        return binX >= this.currentBins[0] && binX <= this.currentBins[2]
                && binY >= this.currentBins[1] && binY <= this.currentBins[3];
    }

    private boolean binRenderedUpdate(int x, int y) {
        if (x < 0 || y < 0 || x >= this.nBins || y >= this.nBins) {
            return false;
        }
        if (this.binIsRendered(x, y)) {
            this.isRendered[x][y] = true;
            return true;
        }
        else {
            this.isRendered[x][y] = false;
            return false;
        }
    }

    private int[] chooseSourceBin(int binX, int binY) {
        int sourceX = transformCopyRange(binX, this.initialBins[0], this.initialBins[2]);
        int sourceY = transformCopyRange(binY, this.initialBins[1], this.initialBins[3]);
        return new int[]{ sourceX, sourceY };
    }

    private int transformCopyRange(int binPosition, int initRangeSt, int initRangeFi) {
        int sourceBin;
        if (binPosition > initRangeFi) {
            sourceBin = initRangeFi;
            for (int i = initRangeFi + 1; i <= binPosition; i++) {
                sourceBin++;
                if (sourceBin > 0 && sourceBin > initRangeFi) {
                    sourceBin = -initRangeFi;
                }
                if (sourceBin < 0 && -sourceBin < initRangeSt) {
                    sourceBin = initRangeSt;
                }
            }
        }
        else if (binPosition < initRangeSt) {
            sourceBin = initRangeSt;
            for (int i = initRangeSt - 1; i >= binPosition; i--) {
                sourceBin--;
                if (sourceBin > 0 && sourceBin < initRangeSt) {
                    sourceBin = -initRangeSt;
                }
                if (sourceBin < 0 && -sourceBin > initRangeFi) {
                    sourceBin = initRangeFi;
                }
            }
        }
        else {
            sourceBin = binPosition;
        }
        System.out.println(initRangeSt+" "+initRangeFi+" "+binPosition+" "+sourceBin);
        return sourceBin;
    }

    private float[] transposeCoordinate(Thing thing, int binX, int binY, int[] sourceBin, float[] sourceCoordinates) {
        float newXPos, newYPos;
        if (sourceBin[0] > 0) {
            newXPos = binX * this.binWidthX + (thing.xPosition - sourceCoordinates[0]);
        }
        else {
            newXPos = binX * this.binWidthX + this.binWidthX - (thing.xPosition - sourceCoordinates[0]);
        }
        if (sourceBin[1] > 0) {
            newYPos = binY * this.binWidthY + (thing.yPosition - sourceCoordinates[2]);
        }
        else {
            newYPos = binY * this.binWidthY + this.binWidthY - (thing.yPosition - sourceCoordinates[2]);
        }
        return new float[]{ newXPos, newYPos };
    }

    private Thing copyThingTo(Thing thing, float newXPos, float newYPos) {
        Thing newThing = thing.makeClone();
        newThing.xPosition = newXPos;
        newThing.yPosition = newYPos;
        newThing.updateBin();
        return newThing;
    }

    private void checkInitThings() {
        if (this.world.engine.frameCounter == 0) {
            System.out.println("Initializing plants");
            this.world.initThings.initPlants(this.currentCoordinates[0], this.currentCoordinates[1],
                    this.currentCoordinates[2], this.currentCoordinates[3]);
        }
        if (this.world.engine.frameCounter == UiConstants.fastPreRenderFrames / 2) {
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
