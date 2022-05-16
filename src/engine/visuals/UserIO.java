package engine.visuals;

import constants.BashboardConstants;
import constants.UiConstants;
import engine.Engine;
import engine.utilities.Keyboard;

import java.awt.event.KeyEvent;


public class UserIO {
    public Engine engine;
    public boolean movingCamera = false;
    public float zoomLevel = UiConstants.startZoom;
    public float zoomSpeed = UiConstants.zoomSpeed;
    public float povDimX = UiConstants.panelWidth / this.zoomLevel;
    public float povDimY = (UiConstants.panelHeight - BashboardConstants.dashboardHeight) / this.zoomLevel;
    public float playerPositionX = UiConstants.startPositionX;
    public float playerPositionY = UiConstants.startPositionY;
    public float[] positionsInView = {
            this.playerPositionX - this.povDimX / 2,
            this.playerPositionX + this.povDimX / 2,
            this.playerPositionY - this.povDimY / 2,
            this.playerPositionY + this.povDimY / 2};
    public float loadRange = UiConstants.loadRangeMultiplier * Math.max(this.povDimX / 2, this.povDimY / 2);
    private float scrollSpeed = UiConstants.scrollSpeed / this.zoomLevel;


    public void keyboardCheck() {
        if (this.fastForward()) { return; }
        this.movingCamera = false;
        if (Keyboard.isKeyPressed(KeyEvent.VK_W)) {
            this.playerPositionY -= this.scrollSpeed / this.engine.tracker.currentFPS;
            reAdjustView();
        }
        if (Keyboard.isKeyPressed(KeyEvent.VK_S)) {
            this.playerPositionY += this.scrollSpeed / this.engine.tracker.currentFPS;
            reAdjustView();
        }
        if (Keyboard.isKeyPressed(KeyEvent.VK_A)) {
            this.playerPositionX -= this.scrollSpeed / this.engine.tracker.currentFPS;
            reAdjustView();
        }
        if (Keyboard.isKeyPressed(KeyEvent.VK_D)) {
            this.playerPositionX += this.scrollSpeed / this.engine.tracker.currentFPS;
            reAdjustView();
        }
        if (Keyboard.isKeyPressed(KeyEvent.VK_EQUALS)) {
            this.zoomLevel += this.zoomLevel * this.zoomSpeed / this.engine.tracker.currentFPS;
            reAdjustView();
        }
        if (Keyboard.isKeyPressed(KeyEvent.VK_MINUS)) {
            this.zoomLevel -= this.zoomLevel * this.zoomSpeed / this.engine.tracker.currentFPS;
            reAdjustView();
        }
    }

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
        this.povDimY = (UiConstants.panelHeight - BashboardConstants.dashboardHeight) / this.zoomLevel;
        this.loadRange = UiConstants.loadRangeMultiplier * Math.max(this.povDimX / 2, this.povDimY / 2);
        this.loadRange = Math.max(this.loadRange, UiConstants.minLoadRange);
        this.positionsInView[0] = this.playerPositionX - this.povDimX / 2;
        this.positionsInView[1] =  this.playerPositionX + this.povDimX / 2;
        this.positionsInView[2] =  this.playerPositionY - this.povDimY / 2;
        this.positionsInView[3] =  this.playerPositionY + this.povDimY / 2;
    }

    public boolean fastForward(){
        return this.engine.tracker.frameCounter < UiConstants.fastPreRenderFrames
                || Keyboard.isKeyPressed(KeyEvent.VK_SPACE);
    }

    public UserIO(Engine engine) {
        this.engine = engine;
    }
}