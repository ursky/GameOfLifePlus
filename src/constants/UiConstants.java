package constants;

public final class UiConstants {
    private UiConstants() {
        // restricted
    }
    public static final int fullDimX = 100000;
    public static final int fullDimY = 100000;
    public static final int nProceduralBins = fullDimX / 100;
    public static int targetFPS = 600;
    public static int targetFrameTime = 1000 / (targetFPS + 1);

    public static final float panelWidth = 800;
    public static final float panelHeight = 600;
    public static final float enlargeFactor = 1.0f;
    public static final float povDimX = panelWidth / enlargeFactor;
    public static final float povDimY = panelHeight / enlargeFactor;
    public static final float loadRange = 3f * (float) Math.max(povDimX / 2, povDimY / 2);
    public static final float startPositionX = fullDimX / 2f;
    public static final float startPositionY = fullDimY / 2f;
    public static final float scrollSpeed = 100.0f; // adjust to FPS
}
