package engine;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import constants.UiConstants;
import engine.utilities.Keyboard;
import engine.visuals.PaintingGroupThread;
import engine.world.ProceduralGeneration;
import engine.world.UpdateThingsThread;

public class Engine extends JPanel implements ActionListener {
    public World world = new World(this);
    public ProceduralGeneration procedural = new ProceduralGeneration(world);
    public Timer timer;
    private Graphics2D g2D;
    public float currentFPS = UiConstants.targetFPS;
    private long currentTime = System.currentTimeMillis();
    private int framesSinceLastFPS = 0;
    private long timeOfLastFPS = System.currentTimeMillis();
    private long timeOfLastUpdate = System.nanoTime();
    public int frameCounter = 0;
    public boolean movingCamera = false;
    public float zoomLevel = UiConstants.startZoom;
    public float zoomSpeed = UiConstants.zoomSpeed;
    public float povDimX = UiConstants.panelWidth / this.zoomLevel;
    public float povDimY = UiConstants.panelHeight / this.zoomLevel;
    public float playerPositionX = UiConstants.startPositionX;
    public float playerPositionY = UiConstants.startPositionY;
    public float[] positionsInView = {
            this.playerPositionX - this.povDimX / 2,
            this.playerPositionX + this.povDimX / 2,
            this.playerPositionY - this.povDimY / 2,
            this.playerPositionY + this.povDimY / 2};
    public float loadRange = UiConstants.loadRangeMultiplier * Math.max(this.povDimX / 2, this.povDimY / 2);
    private float scrollSpeed = UiConstants.scrollSpeed / this.zoomLevel;
    public float threadBuffer = 0;

    public void printUpdate(String stepName) {
        long currentTime = System.nanoTime();
        long timeDelta = currentTime - this.timeOfLastUpdate;
        this.timeOfLastUpdate = currentTime;
        if (this.frameCounter % 100 == 0) {
            System.out.println(stepName + ": " + (float) timeDelta / 1000 + " ns");
        }
    }

    public void updateFPS(Graphics g) {
        throttleFPS();
        this.framesSinceLastFPS ++;
        long timeSinceLastFPS = System.currentTimeMillis() - timeOfLastFPS;
        if (timeSinceLastFPS > 100) {
            this.currentFPS = 1000f * this.framesSinceLastFPS / (int) timeSinceLastFPS;
            this.currentFPS = Math.min(this.currentFPS, UiConstants.targetFPS);
            this.currentFPS = Math.max(this.currentFPS, 1);
            this.framesSinceLastFPS = 0;
            this.timeOfLastFPS = System.currentTimeMillis();
        }
        g.setColor(Color.white);
        String strFPS = String.valueOf((int)currentFPS);
        g.drawString("FPS: " + strFPS + "; #Things: " + world.things.size(), 0, 12);
    }

    public boolean fastForward(){
        return this.frameCounter < UiConstants.fastPreRenderFrames || Keyboard.isKeyPressed(KeyEvent.VK_SPACE);
    }

    public void throttleFPS() {
        long frameLength = System.currentTimeMillis() - currentTime;
        if (this.fastForward()) {
            this.currentFPS = UiConstants.fastPreRenderFPS;
        }
        else if (frameLength < UiConstants.targetFrameTime) {
            try {
                Thread.sleep((long) (UiConstants.targetFrameTime - frameLength));
            }
            catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        currentTime = System.currentTimeMillis();
    }

    public void paint(Graphics g) {
        super.paint(g);
        this.g2D = (Graphics2D) g;
        ArrayList<PaintingGroupThread> paintGroups = initializePaintGroups();
        this.printUpdate("Prepare painting");
        this.paintPaintGroup(paintGroups);
        this.updateFPS(g);
        this.printUpdate("Add paint objects");
    }

    public void paintImage(BufferedImage image, int xPos, int yPos, int size) {
        this.g2D.drawImage(image, xPos, yPos, size, size, null);
    }

    private ArrayList<PaintingGroupThread> initializePaintGroups() {
        ArrayList<PaintingGroupThread> paintThreads = new ArrayList<>();
        float minSize = 0;
        float maxSize = (int)this.world.initThings.getBiggestSize() + 1;
        float sizeIncrement = UiConstants.paintSizeIncrement;
        for (float size=minSize; size<maxSize; size+=sizeIncrement) {
            PaintingGroupThread paintThread = new PaintingGroupThread(this, size, size + sizeIncrement, false);
            paintThreads.add(paintThread);
        }
        PaintingGroupThread paintThread = new PaintingGroupThread(this, 0, 10000, true);
        paintThreads.add(paintThread);
        for (PaintingGroupThread thread: paintThreads) { thread.start(); }
        for (PaintingGroupThread thread: paintThreads) { thread.join(); }

        return paintThreads;
    }

    private void paintPaintGroup(ArrayList<PaintingGroupThread> paintGroups) {
        for (PaintingGroupThread paintGroup: paintGroups) {
            for (int i=0; i<paintGroup.images.size(); i++) {
                this.paintImage(
                        paintGroup.images.get(i),
                        paintGroup.xPositions.get(i),
                        paintGroup.yPositions.get(i),
                        paintGroup.sizes.get(i));
            }
        }
    }

    private void keyboardCheck() {
        if (this.fastForward()) { return; }
        this.movingCamera = false;
        if (Keyboard.isKeyPressed(KeyEvent.VK_W)) {
            this.playerPositionY -= this.scrollSpeed / this.currentFPS;
            reAdjustView();
        }
        if (Keyboard.isKeyPressed(KeyEvent.VK_S)) {
            this.playerPositionY += this.scrollSpeed / this.currentFPS;
            reAdjustView();
        }
        if (Keyboard.isKeyPressed(KeyEvent.VK_A)) {
            this.playerPositionX -= this.scrollSpeed / this.currentFPS;
            reAdjustView();
        }
        if (Keyboard.isKeyPressed(KeyEvent.VK_D)) {
            this.playerPositionX += this.scrollSpeed / this.currentFPS;
            reAdjustView();
        }
        if (Keyboard.isKeyPressed(KeyEvent.VK_EQUALS)) {
            this.zoomLevel += this.zoomLevel * this.zoomSpeed / this.currentFPS;
            reAdjustView();
        }
        if (Keyboard.isKeyPressed(KeyEvent.VK_MINUS)) {
            this.zoomLevel -= this.zoomLevel * this.zoomSpeed / this.currentFPS;
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
        this.povDimY = UiConstants.panelHeight / this.zoomLevel;
        this.loadRange = UiConstants.loadRangeMultiplier * Math.max(this.povDimX / 2, this.povDimY / 2);
        this.loadRange = Math.max(this.loadRange, UiConstants.minLoadRange);
        this.positionsInView[0] = this.playerPositionX - this.povDimX / 2;
        this.positionsInView[1] =  this.playerPositionX + this.povDimX / 2;
        this.positionsInView[2] =  this.playerPositionY - this.povDimY / 2;
        this.positionsInView[3] =  this.playerPositionY + this.povDimY / 2;
    }

    private void updateFrames() {
        this.frameCounter++;
        this.threadBuffer = this.world.engine.frameCounter % (this.world.engine.procedural.loadRangeWidth);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.printUpdate("Paint");
        this.world.updateWorld();

        this.repaint();
        this.keyboardCheck();
        this.printUpdate("Keyboard");

        this.procedural.updateBins();
        this.printUpdate("Update bins");

        this.world.initThings.updateConstants();
        this.updateFrames();
        this.printUpdate("\nUpdate frames");
    }

    Engine() {
        this.setPreferredSize(new Dimension((int)UiConstants.panelWidth, (int)UiConstants.panelHeight));
        this.setBackground(Color.black);
        timer = new Timer(0, this);
        timer.start();
    }
}