package constants;

public final class UiConstants {
    public static final String imageDir = "graphics";
    public static final String renderedImageDir = "rendered_images";

    public static final int threadCount = 12;
    public static final int fastPreRenderFrames = 100;
    public static final float postStartSpawnPenalty = 0.1f;
    public static final float fastForward = 8; // no more than 10x
    public static final int fullDimX = 1000000;
    public static final int fullDimY = 1000000;
    public static final int nProceduralBins = fullDimX / 500;
    public static float targetFPS = 60;
    public static float targetFrameTime = 1000 / (targetFPS + 1);
    public static final int fpsDisplayUpdateMs = 100;
    public static final int printDisplayUpdateMs = 100;
    public static final boolean doPrintStepNanoseconds = false;
    public static final boolean doPrintCounts = false;

    public static final int panelWidth = 1600;
    public static final int panelHeight = 900;
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