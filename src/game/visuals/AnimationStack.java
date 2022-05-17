package game.visuals;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This class stores a sets of images (with different opacities, rotations, ect) meant to form a cohesive animation
 * for fast retrieval later. An animation can be 1 image (static) or many (dynamic creature like the butterfly).
 */
public class AnimationStack {
    public ArrayList<ImageStack> orderedImageStacks = new ArrayList<>();
    ArrayList<String> loadedFiles = new ArrayList<>();
    ArrayList<ImageStack> loadedStacks = new ArrayList<>();

    /**
     * Initialize an animation stack
     * @param orderedImageNames: names of images of animation frames
     * @param startRotation: the initial rotation of the creature in the image (0 if it is pointing up)
     * @param rotationVariety: how many rotation positions (1-360) should we make?
     * @param opacityVariety: how many opacities (0-255) should we make?
     * @param imageResolution: what resolution to store the images at (saves memory)
     * @param imagePadding: how many pixels to crop off all image sides?
     */
    public AnimationStack(ArrayList<String> orderedImageNames, int startRotation, int rotationVariety,
                          int opacityVariety, int imageResolution, int imagePadding) {
        for (String imageName: orderedImageNames) {
            // note the same frame/image can be used many time in the animation; keeping track of them prevents
            // loading multiple copies in memory
            boolean alreadyLoaded = false;
            for (int i=0; i<this.loadedFiles.size(); i++) {
                if (Objects.equals(imageName, this.loadedFiles.get(i))) {
                    this.orderedImageStacks.add(this.loadedStacks.get(i));
                    alreadyLoaded = true;
                    break;
                }
            }
            if (!alreadyLoaded) {
                // load the frame image!
                ImageStack imageStack = new ImageStack(imageName, startRotation, rotationVariety, opacityVariety,
                        imageResolution, imagePadding);
                this.orderedImageStacks.add(imageStack);
                this.loadedFiles.add(imageName);
                this.loadedStacks.add(imageStack);
            }
        }
    }
}