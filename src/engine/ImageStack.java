package engine;

import constants.UiConstants;

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

public class ImageStack extends JFrame {
    public String origImageName;
    public BufferedImage origImage;
    public ArrayList<ArrayList<BufferedImage>> imageStack;
    public int startRotation;
    public int rotationVariety;
    public int opacityVariety;
    public int imageResolution;
    public int imagePadding;

    public BufferedImage getImage(float rotation, float opacity) {
        int roundedRotation = (int)(rotation * this.rotationVariety / 360);
        int roundedOpacity = (int)((256 - opacity) * this.opacityVariety / 256);
        return this.imageStack.get(roundedRotation).get(roundedOpacity);
    }

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

    public BufferedImage toBufferedImage(Image image)
    {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bufferedImage.createGraphics();
        bGr.drawImage(image, 0, 0, null);
        bGr.dispose();
        return bufferedImage;
    }

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

    private int changeAlpha(int origColor, int alphaValue) {
        origColor = origColor & 0x00ffffff; // drop the previous alpha value
        return (alphaValue << 24) | origColor; // add the one the user inputted
    }

    private void initImageStack() {
        for (int rotation=0; rotation<360; rotation+=(360 / this.rotationVariety)) {
            this.imageStack.add(new ArrayList<>());
            for (int opacity=255; opacity>0; opacity-=(255 / this.opacityVariety)) {
                BufferedImage finalImage = makeOrLoadImage(rotation, opacity);
                this.imageStack.get(rotation * this.rotationVariety / 360).add(finalImage);
            }
        }
    }

    private BufferedImage makeOrLoadImage(int rotation, int opacity) {
        String imageFile = this.origImageName.substring(0, this.origImageName.lastIndexOf('.'))
                + "_" + this.imageResolution + "_" + this.imagePadding + "_" + rotation + "_" + opacity + ".png";
        Path imagePath = Paths.get(UiConstants.renderedImageDir + "/" + imageFile);
        if (Files.exists(imagePath)) {
            return toBufferedImage(new ImageIcon(UiConstants.renderedImageDir + "/" + imageFile).getImage());
        }
        else {
            System.out.println("Making image " + imageFile + " for the first time");
            BufferedImage imageToMod = resizeSquare(this.origImage);
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

    private BufferedImage resizeSquare(BufferedImage image) {
        Image tmp = image.getScaledInstance(this.imageResolution, this.imageResolution, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(
                this.imageResolution, this.imageResolution, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(tmp, 0, 0, null);
        g.dispose();
        return resizedImage;
    }

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