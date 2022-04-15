package engine;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import constants.ThingConstants;
import constants.UiConstants;
import things.Thing;
import utilities.Keyboard;
import world.ProceduralGeneration;
import world.World;

public class Engine extends JPanel implements ActionListener {
    public World world = new World(this);
    public ProceduralGeneration procedural = new ProceduralGeneration(world);
    public Timer timer;
    public int currentFPS = UiConstants.targetFPS;
    private long currentTime = System.currentTimeMillis();
    private int framesSinceLastFPS = 0;
    private long timeOfLastFPS = System.currentTimeMillis();
    private long timeOfLastUpdate = System.nanoTime();
    public int frameCounter = 0;
    public float zoomLevel = UiConstants.startZoom;
    public float zoomSpeed = UiConstants.zoomSpeed;
    private float povDimX = UiConstants.panelWidth / this.zoomLevel;
    private float povDimY = UiConstants.panelHeight / this.zoomLevel;
    public float loadRange = UiConstants.loadRangeMultiplier * Math.max(this.povDimX / 2, this.povDimY / 2);
    private float scrollSpeed = UiConstants.scrollSpeed / this.zoomLevel;
    public int threadCount = UiConstants.threadCount;
    public float threadBuffer = 0;
    public float threadWidth = this.loadRange * 2 / this.threadCount;
    public float renderedLeftX = this.world.playerPositionX - this.loadRange;

    public void timeUpdate(String stepName) {
        long currentTime = System.nanoTime();
        long timeDelta = currentTime - this.timeOfLastUpdate;
        this.timeOfLastUpdate = currentTime;
        if (this.frameCounter % 100 == 0) {
            System.out.println(stepName + ": " + (float) timeDelta / 1000 + " ms");
        }
    }

    public void updateFPS(Graphics g) {
        throttleFPS();
        this.framesSinceLastFPS ++;
        long timeSinceLastFPS = System.currentTimeMillis() - timeOfLastFPS;
        if (timeSinceLastFPS > 100) {
            this.currentFPS = 1000 * this.framesSinceLastFPS / (int) timeSinceLastFPS;
            this.currentFPS = Math.min(this.currentFPS, UiConstants.targetFPS);
            this.currentFPS = Math.max(this.currentFPS, 1);
            this.framesSinceLastFPS = 0;
            this.timeOfLastFPS = System.currentTimeMillis();
        }
        g.setColor(Color.white);
        String strFPS = String.valueOf(currentFPS);
        g.drawString("FPS: " + strFPS + "; #Things: " + world.things.size(), 0, 12);
    }

    public void throttleFPS() {
        long frameLength = System.currentTimeMillis() - currentTime;
        if (frameLength < UiConstants.targetFrameTime) {
            try {
                Thread.sleep(UiConstants.targetFrameTime - frameLength);
            }
            catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        currentTime = System.currentTimeMillis();
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        for (ThingConstants constants: this.world.initThings.orderedThingConstants) {
            scanThingsToPaint(constants.name, g2D);
        }
        updateFPS(g);
    }

    private void scanThingsToPaint(String className, Graphics2D g2D) {
        for (Thing thing : world.things) {
            String thingName = thing.constants.name;
            if (thing.size >= thing.constants.minSizeToShow && thingName.equals(className)) {
                paintThing(thing, g2D);
            }
        }
    }

    private void paintThing(Thing thing, Graphics2D g2D) {
        float xPos = transformCoordinate(thing.xPosition, thing.size, world.playerPositionX, this.povDimX);
        float yPos = transformCoordinate(thing.yPosition, thing.size, world.playerPositionY, this.povDimY);
        float size = thing.size * this.zoomLevel;
        if (inView(xPos, yPos, size)) {
            g2D.drawImage(thing.itemImage, (int) (xPos * this.zoomLevel),
                    (int) (yPos * this.zoomLevel), (int) size, (int) size, null);
        }
    }

    private boolean inView(float xPos, float yPos, float size) {
        return xPos + size / 2 >= 0 && yPos + size / 2 >= 0
                && xPos - size / 2 < this.povDimX
                && yPos - size / 2 < this.povDimY;
    }

    private float transformCoordinate(float position, float size, float fovPosition, float dimension) {
        position = position - size / 2;
        position = position - fovPosition + dimension / 2;
        return position;
    }

    private void keyboardCheck() {
        if (Keyboard.isKeyPressed(KeyEvent.VK_W)) {
            this.world.playerPositionY -= this.scrollSpeed / this.currentFPS;
            reAdjustView();
        }
        if (Keyboard.isKeyPressed(KeyEvent.VK_S)) {
            this.world.playerPositionY += this.scrollSpeed / this.currentFPS;
            reAdjustView();
        }
        if (Keyboard.isKeyPressed(KeyEvent.VK_A)) {
            this.world.playerPositionX -= this.scrollSpeed / this.currentFPS;
            reAdjustView();
        }
        if (Keyboard.isKeyPressed(KeyEvent.VK_D)) {
            this.world.playerPositionX += this.scrollSpeed / this.currentFPS;
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
        if (this.zoomLevel < UiConstants.maxZoom) {
            this.zoomLevel = UiConstants.maxZoom;
        }
        if (this.zoomLevel > UiConstants.minZoom) {
            this.zoomLevel = UiConstants.minZoom;
        }
        this.scrollSpeed = UiConstants.scrollSpeed / this.zoomLevel;
        this.povDimX = UiConstants.panelWidth / this.zoomLevel;
        this.povDimY = UiConstants.panelHeight / this.zoomLevel;
        this.loadRange = UiConstants.loadRangeMultiplier * Math.max(this.povDimX / 2, this.povDimY / 2);
        this.loadRange = Math.max(this.loadRange, UiConstants.minLoadRange);
        this.threadWidth = this.loadRange * 2 / this.threadCount;
        this.renderedLeftX = this.world.playerPositionX - this.loadRange;
    }

    private void updateFrames() {
        this.frameCounter++;
        this.threadBuffer = this.world.engine.currentFPS % (this.loadRange * 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        procedural.updateBins();
        world.updateWorld();
        repaint();
        keyboardCheck();
        this.world.initThings.updateConstants();
        this.updateFrames();
    }

    Engine() {
        this.setPreferredSize(new Dimension((int)UiConstants.panelWidth, (int)UiConstants.panelHeight));
        this.setBackground(Color.black);
        timer = new Timer(0, this);
        timer.start();
    }
}