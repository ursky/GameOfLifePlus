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

public class AnimationStack {
    public ArrayList<ImageStack> orderedImageStacks = new ArrayList<>();

    public AnimationStack(ArrayList<String> orderedImageNames, int startRotation, int rotationVariety,
                          int opacityVariety, int imageResolution, int imagePadding) {
        for (String imageName: orderedImageNames) {
            ImageStack imageStack = new ImageStack(imageName, startRotation, rotationVariety, opacityVariety,
                    imageResolution, imagePadding);
            this.orderedImageStacks.add(imageStack);
        }
    }
}