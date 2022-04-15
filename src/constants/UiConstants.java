package constants;

public final class UiConstants {
    private UiConstants() {
        // restricted
    }
    public static final int threadCount = 12;
    public static final int fullDimX = 10000;
    public static final int fullDimY = 10000;
    public static final int nProceduralBins = fullDimX / 100;
    public static int targetFPS = 60;
    public static int targetFrameTime = 1000 / (targetFPS + 1);

    public static final float panelWidth = 800;
    public static final float panelHeight = 600;
    public static final float startZoom = 2f;
    public static final float maxZoom = 1 / 2f;
    public static final float minZoom = 1 / 0.06f;
    public static final float loadRangeMultiplier = 2f;
    public static final float minLoadRange = 200;
    public static final float startPositionX = fullDimX / 2f;
    public static final float startPositionY = fullDimY / 2f;
    public static final float scrollSpeed = 500.0f; // adjust to FPS and enlargement
    public static final float zoomSpeed = 2f; // adjust to FPS
}
