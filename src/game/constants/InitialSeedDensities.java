package game.constants;

/**
 * Store the initial seeding densities of all the creatures. Increase/decrease values to see more or less of something
 */
public final class InitialSeedDensities {
    // useful to quickly increase or decrease seeding density
    static float densityMultiplier = 2;

    // the values mean how many creatures on average are present for every 100x100 pixels in the world at seeding
    public static final float grassStartingDensity = densityMultiplier * 0.4f; // per 100pixels^2
    public static final float treeStartingDensity = densityMultiplier * 0.4f; // per 100pixels^2
    public static final float bushStartingDensity = densityMultiplier * 0.4f; // per 100pixels^2
    public static final float beetleStartingDensity = densityMultiplier * 0.9f; // per 100pixels^2
    public static final float butterflyStartingDensity = densityMultiplier * 0.2f; // per 100pixels^2
    public static final float seedEaterStartingDensity = densityMultiplier * 0.2f; // per 100pixels^2
    public static final float turtleStartingDensity = densityMultiplier * 0.05f; // per 100pixels^2

    // how rarely do things spontaneously spawn (after initialization) as the player scrolls around
    public static final float postStartSpawnPenalty = 0.1f;

    private InitialSeedDensities() {
        // restricted
    }
}