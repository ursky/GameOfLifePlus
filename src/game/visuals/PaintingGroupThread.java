package game.visuals;

import game.Game;
import game.world.things.Classes.Thing;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * This launches a single thread for preparing for painting/drawing creatures in a specific narrow size range.
 * This is used to group creature images from small to large so that the larger things are on top of smaller ones.
 */
public class PaintingGroupThread implements Runnable {
    Thread t;
    private final Game game;
    private final float minSize;
    private final float maxSize;
    private final boolean flying;

    /**
     * These lists are what the painter will be drawing from later on
     */
    public ArrayList<BufferedImage> images = new ArrayList<>();
    public ArrayList<Integer> sizes = new ArrayList<>();
    public ArrayList<Integer> xPositions = new ArrayList<>();
    public ArrayList<Integer> yPositions = new ArrayList<>();

    /**
     * Launch scan on thread
     */
    public void start() {
        this.t.start();
    }

    /**
     * Wait for thread to finish
     */
    public void join() {
        try {
            this.t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Scan all the creatures in the world and add the ones in the correct size range to the paint list.
     * Note that flying creatures are saved if requested (since we need them to soar over everything else).
     */
    public void run() {
        for (Thing thing: this.game.world.things) {
            if (thing.isInView() && thing.size >= this.minSize && thing.size < this.maxSize) {
                // if the creature is flying its handles differently
                boolean reallyFlying = (thing.constants.flying && !thing.isSeed);
                if (reallyFlying == this.flying) {
                    int size = transformSize(thing);
                    int xPos = transformX(thing);
                    int yPos = transformY(thing);
                    this.images.add(thing.itemImage);
                    this.sizes.add(size);
                    this.xPositions.add(xPos);
                    this.yPositions.add(yPos);
                }
            }
        }
    }

    /**
     * Adjust size of the creature to the current zoom level
     * @param thing: creature to look at
     * @return: adjusted size
     */
    private int transformSize(Thing thing) {
        return (int)(thing.size * this.game.userIO.zoomLevel);
    }

    /**
     * Convert the creature's coordinate to the corresponding position on the screen/window given the current position
     * of the player and the zoom level.
     * @param thing: creature to look at
     * @return: adjusted coordinate
     */
    private int transformX(Thing thing) {
        float posX = thing.coordinate.getX() - (this.game.userIO.playerPositionX - this.game.userIO.povDimX / 2)
                - thing.size / 2;
        return (int)(posX * this.game.userIO.zoomLevel);
    }

    /**
     * Convert the creature's coordinate to the corresponding position on the screen/window given the current position
     * of the player and the zoom level.
     * @param thing: creature to look at
     * @return: adjusted coordinate
     */
    private int transformY(Thing thing) {
        float posY = thing.coordinate.getY() - (this.game.userIO.playerPositionY - this.game.userIO.povDimY / 2)
                - thing.size / 2;
        return (int)(posY * this.game.userIO.zoomLevel);
    }

    /**
     * Initialize paint scanner thread
     * @param game: game engine
     * @param minSize: lower size bound of creatures to prepare for plotting
     * @param maxSize: upper size bound of creatures to prepare for plotting
     * @param flying: keep flying things or not
     */
    public PaintingGroupThread(Game game, float minSize, float maxSize, boolean flying) {
        this.game = game;
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.flying = flying;
        this.t = new Thread(this);
    }
}