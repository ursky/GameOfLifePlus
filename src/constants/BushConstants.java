package constants;

public final class BushConstants {
    private BushConstants() {
        // restricted
    }
    // thing constants
    public static final String imagePath = "graphics/tree_5.png";
    public static final int startingDensity = 30; // per 100pixels^2
    public static final float maxSize = 10;
    public static final float minSizeToShow = 1f;

    // organism constants
    public static final float metabolismRate = 5f; // adjust to FPS
    public static final float growAtHealth = 50;
    public static final float maxGrowthRate = 5.0f; // adjust to FPS
    public static final float reproduceAtSize = 0.8f * maxSize;
    public static final float reproduceAtHealth = 90;
    public static final float reproductionPenalty = 0.5f;
    public static final int maxOffsprings = 10;

    // plant constants
    public static final float dispersalRange = 30;
    public static final float shadeRange = 6;
    public static final float shadePenalty = -20f; // adjust to FPS
    public static final float sproutTime = 1.1f; // adjust to FPS
}
