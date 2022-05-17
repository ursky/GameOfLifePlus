package game.visuals;

import game.world.things.Classes.Thing;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ImageLoadThread implements Runnable {
    Thread t;
    public ImageStack imageStack;
    public int rotation, opacity;
    public ArrayList<Thing> things;
    public BufferedImage finalImage;

    public ImageLoadThread(ImageStack imageStack, int rotation, int opacity) {
        this.imageStack = imageStack;
        this.rotation = rotation;
        this.opacity = opacity;
        this.t = new Thread(this);
    }

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
        this.finalImage = this.imageStack.makeOrLoadImage(this.rotation, this.opacity);
    }
}