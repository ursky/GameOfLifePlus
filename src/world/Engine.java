package world;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import constants.UiConstants;
import things.Thing;
import utilities.Keyboard;

public class Engine extends JPanel implements ActionListener {
    public World world = new World(this);
    public Procedural procedural = new Procedural(world);
    public Timer timer;
    public int currentFPS = UiConstants.targetFPS;
    private long currentTime = System.currentTimeMillis();
    private int framesSinceLastFPS = 0;
    private long timeOfLastFPS = System.currentTimeMillis();
    private long timeOfLastUpdate = System.nanoTime();
    public int frameCounter = 0;
    public boolean doPrintUpdates = false;

    private float zoomLevel = UiConstants.startZoom;
    public float zoomSpeed = UiConstants.zoomSpeed;
    private float povDimX = UiConstants.panelWidth / this.zoomLevel;
    private float povDimY = UiConstants.panelHeight / this.zoomLevel;
    public float loadRange = UiConstants.loadRangeMultiplier * Math.max(this.povDimX / 2, this.povDimY / 2);
    private float scrollSpeed = UiConstants.scrollSpeed / this.zoomLevel;

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
        framesSinceLastFPS ++;
        long timeSinceLastFPS = System.currentTimeMillis() - timeOfLastFPS;
        if (timeSinceLastFPS > 100) {
            currentFPS = 1000 * framesSinceLastFPS / (int) timeSinceLastFPS;
            currentFPS = Math.min(currentFPS, UiConstants.targetFPS);
            framesSinceLastFPS = 0;
            timeOfLastFPS = System.currentTimeMillis();
        }
        g.setColor(Color.white);
        String strFPS = String.valueOf(currentFPS);
        g.drawString("FPS: " + strFPS + "; Trees: " + world.things.size(), 0, 12);
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
        String[] paintOrder = new String[]{"Bush", "Tree"};
        for (String className: paintOrder) {
            scanThingsToPaint(className, g2D);
        }
        updateFPS(g);
    }

    private void scanThingsToPaint(String className, Graphics2D g2D) {
        for (Thing thing : world.things) {
            String thingClass = thing.getClass().getSimpleName();
            if (thing.size >= thing.minSizeToShow && thingClass.equals(className)) {
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
        }
        if (Keyboard.isKeyPressed(KeyEvent.VK_S)) {
            this.world.playerPositionY += this.scrollSpeed / this.currentFPS;
        }
        if (Keyboard.isKeyPressed(KeyEvent.VK_A)) {
            this.world.playerPositionX -= this.scrollSpeed / this.currentFPS;
        }
        if (Keyboard.isKeyPressed(KeyEvent.VK_D)) {
            this.world.playerPositionX += this.scrollSpeed / this.currentFPS;
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
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        procedural.updateBins();
        world.updateWorld();
        repaint();
        keyboardCheck();
        this.frameCounter++;
    }

    Engine() {
        this.setPreferredSize(new Dimension((int)UiConstants.panelWidth, (int)UiConstants.panelHeight));
        this.setBackground(Color.black);
        timer = new Timer(0, this);
        timer.start();
    }
}