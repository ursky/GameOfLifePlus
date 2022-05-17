package game.visuals;

import game.world.things.Classes.Thing;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Multi-thread handler for loading images into memory and processing them.
 * This has no effect on run time but saves some time in the initial loading.
 */
public class ImageLoadThread implements Runnable {
    Thread t;
    public ImageStack imageStack;
    public int rotation, opacity;
    public ArrayList<Thing> things;
    public BufferedImage finalImage;

    /**
     * Load an image with given parameters into memory
     * @param imageStack: which image stack to process
     * @param rotation: desired degree rotation (0-360)
     * @param opacity: desired opacity (0-255)
     */
    public ImageLoadThread(ImageStack imageStack, int rotation, int opacity) {
        this.imageStack = imageStack;
        this.rotation = rotation;
        this.opacity = opacity;
        this.t = new Thread(this);
    }

    /**
     * Launch thread
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
     * Run image loading on this thread
     */
    public void run() {
        this.finalImage = this.imageStack.makeOrLoadImage(this.rotation, this.opacity);
    }
}