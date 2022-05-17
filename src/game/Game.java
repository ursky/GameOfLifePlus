package game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import game.constants.UiConstants;
import game.userIO.Mouse;
import game.utilities.TimeTracker;
import game.visuals.PaintingGroupThread;
import game.userIO.UserIO;
import game.world.ProceduralGeneration;
import game.dashboard.Dashboard;
import game.utilities.Random;
import game.world.World;

/**
 * This is the main GameOfLife+ game class. This class focuses on orchestrating the visualizations and presentation
 * of the game, while the World class handles the actual game world and the creatures within it.
 */
public class Game extends JPanel implements ActionListener {
    public World world = new World(this);
    public ProceduralGeneration procedural;
    public Timer timer;
    public TimeTracker tracker;
    public Graphics2D g2D;
    public final Dashboard dashboard;
    public UserIO userIO;
    private String randomID;
    public boolean recording;

    /**
     * Add a FPS counter to the top left corner
     */
    public void paintFPS() {
        String fpsMessage = "FPS: " + (int) this.tracker.currentFPS;
        this.g2D.setColor(Color.white);
        this.g2D.drawString(fpsMessage, 0, 12);
    }

    /**
     * This is the main paint function!
     * Paint all the creatures currently in view, the dashboard, and other things on screen.
     * @param g  the <code>Graphics</code> context in which to paint
     */
    public void paint(Graphics g) {
        super.paint(g);
        this.g2D = (Graphics2D) g;
        // paint creatures and main screen
        if (UiConstants.doPaintThings) {
            ArrayList<PaintingGroupThread> paintGroups = initializePaintGroups();
            this.tracker.printStepNanoseconds("Prepare painting");
            this.paintPaintGroup(paintGroups);
            this.paintFPS();
            this.tracker.printStepNanoseconds("\nAdd paint objects");
        }

        // paint dashboard
        this.dashboard.paint();
        this.tracker.printStepNanoseconds("Paint dashboard");

        if (this.recording) {
            this.saveFrame();
            this.tracker.printStepNanoseconds("Save frames");
        }
    }

    /**
     * Screen capture the current frame and save it to a file. This is how the game does screen caps and frame
     * capture to make GIFs with later.
     */
    private void saveFrame() {
        // where to save capture?
        StringBuilder frameName = new StringBuilder(String.valueOf(this.tracker.frameCounter));
        while (frameName.length() < 6) {
            frameName.insert(0, "0");
        }
        String fileName = UiConstants.savedImageDir + "/" + this.randomID + "/frame_" + frameName + ".png";
        // capture full screen as an image
        BufferedImage screenImage = null;
        try {
            Rectangle panelBounds = this.getBounds();
            panelBounds.setLocation(this.getLocationOnScreen());
            screenImage = new Robot().createScreenCapture(panelBounds);
        } catch (AWTException e1) {
            e1.printStackTrace();
        }
        // save image to file
        File file = new File(fileName);
        try {
            assert screenImage != null;
            ImageIO.write(screenImage, "png", file);
            System.out.println("Saved " + fileName);
        } catch (Exception e) {
            System.out.println("Export error");
        }
    }

    /**
     * Initialize the groups of creatures that will be painted in parallel with threads.
     * @return: the grouped things ready for painting onto canvas
     */
    private ArrayList<PaintingGroupThread> initializePaintGroups() {
        ArrayList<PaintingGroupThread> paintThreads = new ArrayList<>();
        float minSize = 0;
        float maxSize = (int)this.world.initThings.getBiggestSize() + 1;
        float sizeIncrement = UiConstants.paintSizeIncrement;
        for (float size=minSize; size<maxSize; size+=sizeIncrement) {
            PaintingGroupThread paintThread = new PaintingGroupThread(
                    this, size, size + sizeIncrement, false);
            paintThreads.add(paintThread);
        }
        PaintingGroupThread paintThread = new PaintingGroupThread(this, 0, 10000, true);
        paintThreads.add(paintThread);
        for (PaintingGroupThread thread: paintThreads) { thread.start(); }
        for (PaintingGroupThread thread: paintThreads) { thread.join(); }

        return paintThreads;
    }

    /**
     * Paint creature images things from a given paint group onto screen
     * @param paintGroups: paint group with images from creatures
     */
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

    /**
     * Paint a single image onto the canvas
     * @param image: the image to paint
     * @param xPos: x position of image
     * @param yPos: y position of image
     * @param size: size of image
     */
    public void paintImage(BufferedImage image, int xPos, int yPos, int size) {
        this.g2D.drawImage(image, xPos, yPos, size, size, null);
    }

    /**
     * Activate screen capture for just this frame. If this is the first screen capture this run then make a randomly
     * generated run name and make a directory for all the images from this run
     */
    public void startRecording() {
        this.recording = true;
        // this is purely for saving frames to make GIFs later
        if (Objects.equals(this.randomID, "")) {
            this.randomID = Random.randString(6);
            File theDir = new File(UiConstants.savedImageDir + "/" + this.randomID);
            if (!theDir.exists()){
                boolean created = theDir.mkdirs();
                if (created) {
                    System.out.println("Initialized saving directory: " + this.randomID);
                }
            }
        }
    }

    /**
     * Update all aspects of the game, including the visualizations as well as the world itself
     * @param e the event to be processed
     */
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

    /**
     * Initialize the game!
     */
    public Game() {
        this.addMouseListener(new Mouse(this));
        this.setPreferredSize(new Dimension(UiConstants.panelWidth, UiConstants.panelHeight));
        this.setBackground(Color.black);
        this.procedural = new ProceduralGeneration(world);
        this.userIO = new UserIO(this);
        this.timer = new Timer(0, this);
        this.timer.start();
        this.tracker = new TimeTracker(this);
        this.dashboard = new Dashboard(this);
        this.recording = false;
        this.randomID = "";
    }
}