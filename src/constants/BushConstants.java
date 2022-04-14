package constants;

public final class BushConstants {
    private BushConstants() {
        // restricted
    }
    // thing constants
    public static final String imagePath = "graphics/tree_5.png";
    public static final int startingCount = (int) (10 * UiConstants.viewsInField);
    public static final float maxSize = 10;
    public static final float minSizeToShow = 1.1f;

    // organism constants
    public static final float metabolismRate = 5f / UiConstants.targetFPS;
    public static final float growAtHealth = 50;
    public static final float maxGrowthRate = 5.0f / UiConstants.targetFPS;
    public static final float reproduceAtSize = 0.8f * maxSize;
    public static final float reproduceAtHealth = 90;
    public static final float reproductionPenalty = 0.95f;
    public static final int maxOffsprings = 10;

    // plant constants
    public static final float dispersalRange = 30;
    public static final float shadeRange = 10;
    public static final float shadePenalty = -20f / UiConstants.targetFPS;
}
