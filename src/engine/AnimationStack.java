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
import java.util.Objects;

public class AnimationStack {
    public ArrayList<ImageStack> orderedImageStacks = new ArrayList<>();
    ArrayList<String> loadedFiles = new ArrayList<>();
    ArrayList<ImageStack> loadedStacks = new ArrayList<>();

    public AnimationStack(ArrayList<String> orderedImageNames, int startRotation, int rotationVariety,
                          int opacityVariety, int imageResolution, int imagePadding) {
        for (String imageName: orderedImageNames) {
            boolean alreadyLoaded = false;
            for (int i=0; i<this.loadedFiles.size(); i++) {
                if (Objects.equals(imageName, this.loadedFiles.get(i))) {
                    this.orderedImageStacks.add(this.loadedStacks.get(i));
                    alreadyLoaded = true;
                    break;
                }
            }
            if (!alreadyLoaded) {
                ImageStack imageStack = new ImageStack(imageName, startRotation, rotationVariety, opacityVariety,
                        imageResolution, imagePadding);
                this.orderedImageStacks.add(imageStack);
                this.loadedFiles.add(imageName);
                this.loadedStacks.add(imageStack);
            }
        }
    }
}