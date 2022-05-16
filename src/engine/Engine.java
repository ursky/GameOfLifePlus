package engine;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import engine.userIO.UiConstants;
import engine.userIO.Mouse;
import engine.visuals.PaintingGroupThread;
import engine.userIO.UserIO;
import engine.world.ProceduralGeneration;
import engine.dashboard.Dashboard;

public class Engine extends JPanel implements ActionListener {
    public World world = new World(this);
    public ProceduralGeneration procedural;
    public Timer timer;
    public TimeTracker tracker;
    public Graphics2D g2D;
    public final Dashboard dashboard;
    public UserIO userIO;

    public void paintFPS(String message) {
        this.g2D.setColor(Color.white);
        this.g2D.drawString(message, 0, 12);
    }

    public void paint(Graphics g) {
        super.paint(g);
        this.g2D = (Graphics2D) g;
        ArrayList<PaintingGroupThread> paintGroups = initializePaintGroups();
        this.tracker.printStepNanoseconds("Prepare painting");
        this.paintPaintGroup(paintGroups);
        String fpsMessage = "FPS: " + String.valueOf((int) this.tracker.currentFPS);
        this.paintFPS(fpsMessage);
        this.tracker.printStepNanoseconds("\nAdd paint objects");
        this.dashboard.paint();
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

    @Override
    public void actionPerformed(ActionEvent e) {
        this.tracker.printStepNanoseconds("Paint");
        this.tracker.updateFPS();
        this.world.updateWorld();
        this.dashboard.update();

        this.repaint();
        this.userIO.keyboardCheck();
        this.tracker.printStepNanoseconds("Keyboard");

        this.procedural.updateBins();
        this.tracker.printStepNanoseconds("Update bins");

        this.world.initThings.updateConstants();
        this.tracker.printStepNanoseconds("Update frames");
    }

    Engine() {
        this.addMouseListener(new Mouse(this));
        this.setPreferredSize(new Dimension(UiConstants.panelWidth, UiConstants.panelHeight));
        this.setBackground(Color.black);
        this.procedural = new ProceduralGeneration(world);
        this.userIO = new UserIO(this);
        this.timer = new Timer(0, this);
        this.timer.start();
        this.tracker = new TimeTracker(this);
        this.dashboard = new Dashboard(this);
    }
}