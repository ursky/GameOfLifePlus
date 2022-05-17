package game.userIO;

import game.constants.UiConstants;
import game.constants.DashboardConstants;
import game.Engine;

import java.awt.event.KeyEvent;

/**
 * This class broadly handles the player's interaction with the game world and is responsible for scrolling,
 * zooming in and out, and clicking keys of mouse.
 */
public class UserIO {
    public Engine engine;
    public boolean movingCamera = false;

    // current zoom and location positions
    public float zoomLevel = UiConstants.startZoom;
    public float zoomSpeed = UiConstants.zoomSpeed;
    public float povDimX = UiConstants.panelWidth / this.zoomLevel;
    public float povDimY = (UiConstants.panelHeight - DashboardConstants.dashboardHeight) / this.zoomLevel;
    public float playerPositionX = UiConstants.startPositionX;
    public float playerPositionY = UiConstants.startPositionY;

    // boundaries of the current FOV
    public float[] positionsInView = {
            this.playerPositionX - this.povDimX / 2,
            this.playerPositionX + this.povDimX / 2,
            this.playerPositionY - this.povDimY / 2,
            this.playerPositionY + this.povDimY / 2};

    // fow far away from the player should things be still updated?
    public float loadRange = UiConstants.loadRangeMultiplier * Math.max(this.povDimX / 2, this.povDimY / 2);
    private float scrollSpeed = UiConstants.scrollSpeed / this.zoomLevel;
    float fpsToAdjustTo;

    /**
     * Check if any relevant keyboard controls were pressed and do something if they were
     */
    public void keyboardCheck() {
        if (this.fastForward()) { return; }
        this.movingCamera = false;
        if (UiConstants.saveFrames) {
            // make the zooming slow and smooth for nicer GIFs
            fpsToAdjustTo = UiConstants.targetFPS * 2;
        }
        else {
            fpsToAdjustTo = this.engine.tracker.currentFPS;
        }
        if (Keyboard.isKeyPressed(KeyEvent.VK_W)) {
            this.playerPositionY -= this.scrollSpeed / fpsToAdjustTo;
            reAdjustView();
        }
        if (Keyboard.isKeyPressed(KeyEvent.VK_S)) {
            this.playerPositionY += this.scrollSpeed / fpsToAdjustTo;
            reAdjustView();
        }
        if (Keyboard.isKeyPressed(KeyEvent.VK_A)) {
            this.playerPositionX -= this.scrollSpeed / fpsToAdjustTo;
            reAdjustView();
        }
        if (Keyboard.isKeyPressed(KeyEvent.VK_D)) {
            this.playerPositionX += this.scrollSpeed / fpsToAdjustTo;
            reAdjustView();
        }
        if (Keyboard.isKeyPressed(KeyEvent.VK_EQUALS)) {
            this.zoomLevel += this.zoomLevel * this.zoomSpeed / fpsToAdjustTo;
            reAdjustView();
        }
        if (Keyboard.isKeyPressed(KeyEvent.VK_MINUS)) {
            this.zoomLevel -= this.zoomLevel * this.zoomSpeed / fpsToAdjustTo;
            reAdjustView();
        }
    }

    /**
     * Re-adjust camera view if player position or zoom changed
     */
    private void reAdjustView() {
        this.movingCamera = true;
        if (this.zoomLevel < UiConstants.minZoom) {
            this.zoomLevel = UiConstants.minZoom;
        }
        if (this.zoomLevel > UiConstants.maxZoom) {
            this.zoomLevel = UiConstants.maxZoom;
        }
        this.scrollSpeed = UiConstants.scrollSpeed / this.zoomLevel;
        this.povDimX = UiConstants.panelWidth / this.zoomLevel;
        this.povDimY = (UiConstants.panelHeight - DashboardConstants.dashboardHeight) / this.zoomLevel;
        this.loadRange = UiConstants.loadRangeMultiplier * Math.max(this.povDimX / 2, this.povDimY / 2);
        this.loadRange = Math.max(this.loadRange, UiConstants.minLoadRange);

        // finally update the FOV
        this.positionsInView[0] = this.playerPositionX - this.povDimX / 2;
        this.positionsInView[1] =  this.playerPositionX + this.povDimX / 2;
        this.positionsInView[2] =  this.playerPositionY - this.povDimY / 2;
        this.positionsInView[3] =  this.playerPositionY + this.povDimY / 2;
    }

    /**
     * Find if the game is in a fast-forward state (cannot interact while fast-forwarding!)
     * @return: are we fast-forwarding right now?
     */
    public boolean fastForward(){
        return this.engine.tracker.frameCounter < UiConstants.fastPreRenderFrames
                || Keyboard.isKeyPressed(KeyEvent.VK_SPACE);
    }

    /**
     * Convert coordinate on the view window to the true coordinate of the spot on the game board
     * @param x: coordinate on the viewing window (such as form from a mouse click)
     * @return: true coordinate (as seen by a creature)
     */
    public float reverseTransformX(int x) {
        return x / this.engine.userIO.zoomLevel + this.engine.userIO.playerPositionX
                - this.engine.userIO.povDimX / 2;
    }

    /**
     * Convert coordinate on the view window to the true coordinate of the spot on the game board
     * @param y: coordinate on the viewing window (such as form from a mouse click)
     * @return: true coordinate (as seen by a creature)
     */
    public float reverseTransformY(int y) {
        return y / this.engine.userIO.zoomLevel + this.engine.userIO.playerPositionY
                - this.engine.userIO.povDimY / 2;
    }

    /**
     * Initialize IO listener
     * @param engine: game engine
     */
    public UserIO(Engine engine) {
        this.engine = engine;
    }
}