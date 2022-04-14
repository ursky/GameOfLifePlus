package constants;

public final class UiConstants {
    private UiConstants() {
        // restricted
    }
    // todo: find a way to create the extra things as they are discovered instead of at the start
    public static final int fieldDimX = 2000;
    public static final int fieldDimY = 2000;
    public static int targetFPS = 60;
    public static int targetFrameLength = 1000 / (targetFPS + 1);

    public static final int panelDimX = 200;
    public static final int panelDimY = 200;
    public static final float enlargeFactor = 5.0f;
    public static final float interactionRange = 300;
    public static final float startPositionX = fieldDimX / 2.0f;
    public static final float startPositionY = fieldDimY / 2.0f;
    public static final float scrollSpeed = 100.0f / targetFPS;

    public static final float viewsInField = (float) (UiConstants.fieldDimX * UiConstants.fieldDimY) /
            (float) (UiConstants.panelDimX * UiConstants.panelDimY);
}
