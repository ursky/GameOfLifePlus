package game.constants;

/**
 * This stores the major constants for how the game window looks to the player. These values are very important
 */
public final class UiConstants {
    // file directory locations (assumed to already exist)
    public static final String imageDir = "graphics";
    public static final String renderedImageDir = "rendered_images";
    public static final String savedImageDir = "saved_images";

    // how many threads/CPUs to use for processing. Scales linearly
    public static final int threadCount = 12;

    // fast-forward for the first few frames. Helps to establish and stabilize the world quicker
    // note that animals spawn half-way throughout this period
    public static final int fastPreRenderFrames = 0;

    // the speed of the fast-forward when pressing SPACE
    public static final float fastForward = 12;

    // when fast forwarding at very fast rates in low FPS some things start breaking (e.g. movement)
    // this constant prevents these errors by limiting the speed-up based on FPS
    public static final float maxRateFactor = 1f / 5; // no less than 1/5

    // dimensions of the full play field (how far you can scroll)
    public static final int fullDimX = 1000000;
    public static final int fullDimY = 1000000;

    // how many "bins" to break the field into. Higher N means more bins which means finer control of what is run
    public static final int nProceduralBins = fullDimX / 500;

    // maximum FPS
    public static float targetFPS = 60;

    // minimum frame time in milliseconds
    public static float targetFrameTime = 1000 / (targetFPS + 1);

    // how often to print messages, update the FPS counter, and update the dashboard
    public static final int infoUpdateMs = 500;

    // print how long each step of the program takes (warning: printing to console causes FPS dips)
    public static final boolean doPrintStepNanoseconds = false;

    // display the creatures on the screen (in case you want to turn it off to speed things up - this doubles the FPS)
    public static final boolean doPaintThings = true;

    // do not automatically seed any creatures on the screen; the player has to add everything themselves (can be fun)
    public static final boolean blankCanvas = false;


    // display window size
    public static final int panelWidth = 1600;
    public static final int panelHeight = 900;

    // player camera zoom constants
    public static final float startZoom = 1.5f;
    public static final float minZoom = 0.5f;
    public static final float maxZoom = 20f;


    // player POV constants
    public static final float startPositionX = fullDimX / 2f;
    public static final float startPositionY = fullDimY / 2f;
    public static final float scrollSpeed = 500.0f; // adjust to FPS and enlargement
    public static final float zoomSpeed = 2f; // adjust to FPS


    // how far around the current view to still calculate creatures (1 means only things on screen)
    public static final float loadRangeMultiplier = 1.2f;

    // minimum pixel load range diameter (prevents strange bugs when the FOV is tiny)
    public static final float minLoadRange = 400;

    // larger things get painted over smaller things. this constant defines the resolution of that filter process
    public static final float paintSizeIncrement = 5;
}