package game.visuals;

import game.Engine;
import game.world.things.Classes.Thing;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PaintingGroupThread implements Runnable {
    Thread t;
    private final Engine engine;
    private final float minSize;
    private final float maxSize;
    private final boolean flying;

    public ArrayList<BufferedImage> images = new ArrayList<>();
    public ArrayList<Integer> sizes = new ArrayList<>();
    public ArrayList<Integer> xPositions = new ArrayList<>();
    public ArrayList<Integer> yPositions = new ArrayList<>();

    public void start() {
        this.t.start();
    }

    public void join() {
        try {
            this.t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        for (Thing thing: this.engine.world.things) {
            if (thing.isInView() && thing.size >= this.minSize && thing.size < this.maxSize) {
                boolean reallyFlying = (thing.constants.flying && !thing.isSeed);
                if (reallyFlying == this.flying) {
                    int size = transformSize(thing);
                    int xPos = transformX(thing);
                    int yPos = transformY(thing);
                    this.images.add(thing.itemImage);
                    this.sizes.add(size);
                    this.xPositions.add(xPos);
                    this.yPositions.add(yPos);;
                }
            }
        }
    }

    private int transformSize(Thing thing) {
        return (int)(thing.size * this.engine.userIO.zoomLevel);
    }

    private int transformX(Thing thing) {
        float posX = thing.xPosition - (this.engine.userIO.playerPositionX - this.engine.userIO.povDimX / 2)
                - thing.size / 2;
        return (int)(posX * this.engine.userIO.zoomLevel);
    }

    private int transformY(Thing thing) {
        float posY = thing.yPosition - (this.engine.userIO.playerPositionY - this.engine.userIO.povDimY / 2)
                - thing.size / 2;
        return (int)(posY * this.engine.userIO.zoomLevel);
    }

    public PaintingGroupThread(Engine engine, float minSize, float maxSize, boolean flying) {
        this.engine = engine;
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.flying = flying;
        this.t = new Thread(this);
    }
}