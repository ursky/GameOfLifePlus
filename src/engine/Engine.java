package engine;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import constants.UiConstants;
import engine.visuals.PaintingGroupThread;
import engine.visuals.UserIO;
import engine.world.ProceduralGeneration;

public class Engine extends JPanel implements ActionListener {
    public World world = new World(this);
    public ProceduralGeneration procedural;
    public Timer timer;
    private Graphics2D g2D;
    public UserIO userIO;
    public float currentFPS = UiConstants.targetFPS;
    private long currentTime = System.currentTimeMillis();
    private int framesSinceLastFPS = 0;
    private long timeOfLastFPS = System.currentTimeMillis();
    private long timeOfLastUpdate = System.nanoTime();
    public int frameCounter = 0;
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

    public void throttleFPS() {
        long frameLength = System.currentTimeMillis() - currentTime;
        if (this.userIO.fastForward()) {
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

    public void paintImage(BufferedImage image, int xPos, int yPos, int size) {
        this.g2D.drawImage(image, xPos, yPos, size, size, null);
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
        this.userIO.keyboardCheck();
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
        this.procedural = new ProceduralGeneration(world);
        this.userIO = new UserIO(this);
        this.timer = new Timer(0, this);
        this.timer.start();
    }
}