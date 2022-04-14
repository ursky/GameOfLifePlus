package constants;

public final class TreeConstants {
    private TreeConstants() {
        // restricted
    }
    // thing constants
    public static final String imagePath = "graphics/tree_3.png";
    public static final int startingDensity = 2; // per 100pixels^2
    public static final float maxSize = 20;
    public static final float minSizeToShow = 1f;

    // organism constants
    public static final float metabolismRate = 5.0f; // adjust to FPS
    public static final float growAtHealth = 80;
    public static final float maxGrowthRate = 5.0f; // adjust to FPS
    public static final float reproduceAtSize = 0.8f * maxSize;
    public static final float reproduceAtHealth = 90;
    public static final float reproductionPenalty = 0.5f;
    public static final int maxOffsprings = 5;

    // plant constants
    public static final float dispersalRange = 50;
    public static final float shadeRange = 15;
    public static final float shadePenalty = -20f; // adjust to FPS
    public static final float sproutTime = 3f; // adjust to FPS
}
