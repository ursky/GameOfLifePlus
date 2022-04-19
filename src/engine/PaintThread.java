package engine;

import things.Thing;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PaintThread implements Runnable {
    Thread t;
    private final Engine engine;
    private final float minSize;
    private final float maxSize;

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

    private int transformSize(Thing thing) {
        return (int)(thing.size * this.engine.zoomLevel);
    }

    private int transformX(Thing thing) {
        float posX = thing.xPosition - (this.engine.world.playerPositionX - this.engine.povDimX / 2) - thing.size / 2;
        return (int)(posX * this.engine.zoomLevel);
    }

    private int transformY(Thing thing) {
        float posY = thing.yPosition - (this.engine.world.playerPositionY - this.engine.povDimY / 2) - thing.size / 2;
        return (int)(posY * this.engine.zoomLevel);
    }

    public PaintThread(Engine engine, float minSize, float maxSize) {
        this.engine = engine;
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.t = new Thread(this);
    }
}