package game.userIO;

public final class UiConstants {
    public static final String imageDir = "graphics";
    public static final String renderedImageDir = "rendered_images";
    public static final String savedImageDir = "saved_images";

    public static final int threadCount = 12;
    public static final int fastPreRenderFrames = 0;
    public static final float postStartSpawnPenalty = 0.1f;
    public static final float fastForward = 12;
    public static final float maxRateFactor = 1f / 5; // no less than 1/5
    public static final int fullDimX = 1000000;
    public static final int fullDimY = 1000000;
    public static final int nProceduralBins = fullDimX / 500;
    public static float targetFPS = 60;
    public static float targetFrameTime = 1000 / (targetFPS + 1);
    public static final int infoUpdateMs = 500;

    public static final boolean doPrintStepNanoseconds = false;
    public static final boolean doPaintThings = true;
    public static final boolean saveFrames = true;
    public static final boolean blankCanvas = false;

    public static final int panelWidth = 600;
    public static final int panelHeight = 400;
    public static final float startZoom = 1.5f;
    public static final float minZoom = 0.5f;
    public static final float maxZoom = 20f;
    public static final float loadRangeMultiplier = 1.5f;
    public static final float minLoadRange = 400;
    public static final float startPositionX = fullDimX / 2f;
    public static final float startPositionY = fullDimY / 2f;
    public static final float scrollSpeed = 500.0f; // adjust to FPS and enlargement
    public static final float zoomSpeed = 2f; // adjust to FPS
    public static final float paintSizeIncrement = 5;
}