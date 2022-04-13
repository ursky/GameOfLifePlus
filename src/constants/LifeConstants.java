package constants;

public final class LifeConstants {
    private LifeConstants() {
        // restricted
    }
    public static final int treesStartingCount = 1000;
    public static final String treeImage = "tree.png";
    public static final float treeMaxSize = 15;
    public static final float treeMinSize = 1;
    public static final float treeGrowthRate = 10.0f / Constants.targetFPS;
    public static final float treeMaturitySize = 0.99f * treeMaxSize;
    public static final float treeMaxSeeds = 10;
    public static final float treeReproductionPenalty = 0.8f;
    public static final float treeDispersalRange = 100;
    public static final float treeShadeRange = 20;
    public static final float treeShadePenalty = -10.5f / Constants.targetFPS;

}
