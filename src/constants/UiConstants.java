package constants;

public final class UiConstants {
    private UiConstants() {
        // restricted
    }
    public static final int threads = 12;
    public static final int fullDimX = 100000;
    public static final int fullDimY = 100000;
    public static final int nProceduralBins = fullDimX / 100;
    public static int targetFPS = 60;
    public static int targetFrameTime = 1000 / (targetFPS + 1);

    public static final float panelWidth = 800;
    public static final float panelHeight = 600;
    public static final float startZoom = 2f;
    public static final float minZoom = 0.5f;
    public static final float maxZoom = 20f;
    public static final float loadRangeMultiplier = 3f;
    public static final float startPositionX = fullDimX / 2f;
    public static final float startPositionY = fullDimY / 2f;
    public static final float scrollSpeed = 500.0f; // adjust to FPS and enlargement
    public static final float zoomSpeed = 2f; // adjust to FPS
}
