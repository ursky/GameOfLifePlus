package game.visuals;

import game.constants.UiConstants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * This class handles loading and storage of a single image and its rotation/opacity variants in memory.
 * The image variants are saved to disk for faster retrieval in future runs.
 */
public class ImageStack extends JFrame {
    public String origImageName;
    public BufferedImage origImage;
    public ArrayList<ArrayList<BufferedImage>> imageStack;
    public int startRotation;
    public int rotationVariety;
    public int opacityVariety;
    public int imageResolution;
    public int imagePadding;

    /**
     * Retrieve from memory the correct image given a creature's rotation and opacity
     * @param rotation: current rotation 0-360
     * @param opacity: current opacity 0-255
     * @return: the image that most accurately represents the creature
     */
    public BufferedImage getImage(float rotation, float opacity) {
        if (rotation >= 360) {
            rotation -= 360;
        }
        else if (rotation < 0) {
            rotation += 360;
        }
        if (opacity < 0) {
            opacity = 0;
        }
        int roundedRotation = (int)(rotation * this.rotationVariety / 360);
        int roundedOpacity = (int)((255 - opacity) * this.opacityVariety / 256);
        return this.imageStack.get(roundedRotation).get(roundedOpacity);
    }

    /**
     * Rotate an image a certain number of degrees clockwise. Note: this is quite slow, which is why its done once
     * during initialization.
     * @param imageToRotate: image to rotate
     * @param rotation: degrees to rotate clockwise
     * @return: rotated image
     */
    public BufferedImage rotate(BufferedImage imageToRotate, int rotation) {
        int widthOfImage = imageToRotate.getWidth();
        int heightOfImage = imageToRotate.getHeight();
        int typeOfImage = imageToRotate.getType();
        BufferedImage newImageFromBuffer = new BufferedImage(widthOfImage, heightOfImage, typeOfImage);
        Graphics2D graphics2D = newImageFromBuffer.createGraphics();
        graphics2D.rotate(Math.toRadians(rotation), widthOfImage / 2.0, heightOfImage / 2.0);
        graphics2D.drawImage(imageToRotate, null, 0, 0);
        return newImageFromBuffer;
    }

    /**
     * Convert image to bufferedImage
     * @param image: image to convert
     * @return: buffered image object
     */
    public BufferedImage toBufferedImage(Image image)
    {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bufferedImage.createGraphics();
        bGr.drawImage(image, 0, 0, null);
        bGr.dispose();
        return bufferedImage;
    }

    /**
     * Change opacity of an image
     * @param image: image to modify
     * @param opacity: desired opacity
     * @return: fixed image
     */
    private BufferedImage changeOpacity(BufferedImage image, int opacity) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < image.getWidth(); ++x) {
            for (int y = 0; y < image.getHeight(); ++y) {
                int argb = image.getRGB(x, y);
                if ((argb & 0x00ffffff) > 0) {
                    argb = changeAlpha(argb, opacity);
                }
                newImage.setRGB(x, y, argb);
            }
        }
        return newImage;
    }

    /**
     * Change the opacity of a color
     * @param origColor: color to modify
     * @param alphaValue: derised opacity
     * @return: fixed color
     */
    private int changeAlpha(int origColor, int alphaValue) {
        origColor = origColor & 0x00ffffff; // drop the previous alpha value
        return (alphaValue << 24) | origColor; // add the one the user inputted
    }

    /**
     * Initialize all requested rotation/opacity variant images from the original image
     */
    private void initImageStack() {
        // load in the images first, but don't add them yet to prevent threading exceptions
        ArrayList<ImageLoadThread> threads = new ArrayList<>();
        for (int rotation=0; rotation<=360; rotation+=(360 / this.rotationVariety)) {
            this.imageStack.add(new ArrayList<>());
            for (int opacity=255; opacity>0; opacity-=(255 / this.opacityVariety)) {
                ImageLoadThread thread = new ImageLoadThread(this, rotation, opacity);
                threads.add(thread);
            }
        }
        // note: this does not save that much time, but still about 3x faster
        for (ImageLoadThread thread: threads) { thread.start(); }
        for (ImageLoadThread thread: threads) { thread.join(); }

        // once everything is loaded, save the images in correct locations
        for (ImageLoadThread thread: threads) {
            this.imageStack.get(thread.rotation * this.rotationVariety / 360).add(thread.finalImage);
        }
    }

    /**
     * Check if an image was previously created and saved to disk. If yes, then load it to save time (fast).
     * If no, then generate it from scratch from the original main source image (slow).
     * @param rotation: desired rotation of image
     * @param opacity: desired opacity of image
     * @return: requested image
     */
    public BufferedImage makeOrLoadImage(int rotation, int opacity) {
        String imageFile = this.origImageName.substring(0, this.origImageName.lastIndexOf('.'))
                + "_" + this.imageResolution + "_" + this.imagePadding + "_" + rotation + "_" + opacity + ".png";
        Path imagePath = Paths.get(UiConstants.renderedImageDir + "/" + imageFile);
        if (Files.exists(imagePath)) {
            return toBufferedImage(new ImageIcon(UiConstants.renderedImageDir + "/" + imageFile).getImage());
        }
        else {
            System.out.println("Making image " + imageFile + " for the first time");
            BufferedImage imageToMod = resizeToSquare(this.origImage);
            BufferedImage paddedImage = addPadding(imageToMod);
            BufferedImage rotatedImage = this.rotate(paddedImage, rotation-this.startRotation);
            BufferedImage finalImage = this.changeOpacity(rotatedImage, opacity);
            File fileOut = new File(UiConstants.renderedImageDir + "/" + imageFile);
            try {
                ImageIO.write(finalImage, "PNG", fileOut);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return finalImage;
        }
    }

    /**
     * Add some transparent pixels to edge of image to prevent cropping artifacts during rotation
     * @param image: image to modify
     * @return: modified image
     */
    private BufferedImage addPadding(BufferedImage image) {
        int newDim = this.imageResolution + 2 * this.imagePadding;
        BufferedImage newImage = new BufferedImage(newDim, newDim, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, newDim, newDim);
        g.drawImage(image, this.imagePadding, this.imagePadding,null);
        g.dispose();
        return newImage;
    }

    /**
     * Add transparent padding pixels to image to make it a perfect square
     * @param image: image to modify
     * @return: modified image
     */
    private BufferedImage resizeToSquare(BufferedImage image) {
        BufferedImage squareImage;
        int imageHeight = image.getHeight();
        int imageWidth = image.getWidth();
        if (imageWidth == imageHeight) {
            squareImage = image;
        }
        else {
            // pad the original image with transparent bars to make it a proper square
            int newDim = Math.max(imageHeight, imageWidth);
            squareImage = new BufferedImage(newDim, newDim, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = squareImage.createGraphics();
            g.setColor(new Color(0, 0, 0, 0));
            g.fillRect(0, 0, newDim, newDim);

            int xPad, yPad;
            if (imageHeight > imageWidth) {
                xPad = (imageHeight - imageWidth) / 2;
                yPad = 0;
            }
            else {
                xPad = 0;
                yPad = (imageWidth - imageHeight) / 2;
            }
            g.drawImage(image, xPad, yPad, null);
            g.dispose();
        }

        Image tmp = squareImage.getScaledInstance(this.imageResolution, this.imageResolution, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(
                this.imageResolution, this.imageResolution, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(tmp, 0, 0, null);
        g.dispose();
        return resizedImage;
    }

    /**
     * Create object and launch stack initialization
     * @param imageName: name of png file to load
     * @param startRotation: the start rotation of creature in the source image (0 if pointing up)
     * @param rotationVariety: how many rotation variants to generate (1-360)
     * @param opacityVariety: how many opacity variants to generate (1-255)
     * @param imageResolution: what resolution to keep the image at?
     * @param imagePadding: how many pixels to crop off the original image
     */
    public ImageStack(String imageName, int startRotation, int rotationVariety, int opacityVariety,
                      int imageResolution, int imagePadding) {
        this.origImageName = imageName;
        this.origImage = toBufferedImage(new ImageIcon(UiConstants.imageDir + "/" + imageName).getImage());
        this.startRotation = startRotation;
        this.imageStack = new ArrayList<>();
        this.rotationVariety = rotationVariety;
        this.opacityVariety = opacityVariety;
        this.imagePadding = imagePadding;
        this.imageResolution = imageResolution;
        this.initImageStack();
    }
}