package constants;

public final class TreeConstants {
    private TreeConstants() {
        // restricted
    }
    // thing constants
    public static final String imagePath = "graphics/tree_3.png";
    public static final int startingCount = (int) (5 * UiConstants.viewsInField);
    public static final float maxSize = 20;
    public static final float minSizeToShow = 1.1f;

    // organism constants
    public static final float metabolismRate = 5.0f / UiConstants.targetFPS;
    public static final float growAtHealth = 80;
    public static final float maxGrowthRate = 5.0f / UiConstants.targetFPS;
    public static final float reproduceAtSize = 0.8f * maxSize;
    public static final float reproduceAtHealth = 90;
    public static final float reproductionPenalty = 0.9f;
    public static final int maxOffsprings = 3;

    // plant constants
    public static final float dispersalRange = 100;
    public static final float shadeRange = 25;
    public static final float shadePenalty = -20f / UiConstants.targetFPS;
}
