package game.world;
import game.constants.UiConstants;
import game.quadsearch.Region;
import game.world.things.Classes.ThingArchive;
import game.world.things.Classes.Thing;
import game.quadsearch.Region;

import java.util.ArrayList;

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

    private void checkRenderedBins() {
        int scanRange = this.safetyScanRange + (int) (this.safetyScanRange * this.world.game.userIO.zoomLevel);
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
            ArrayList<Thing> archivedThings = this.archivedThings[i][j].things;
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
