package constants;

public final class Constants {
    private Constants() {
        // restricted
    }
    public static final int panelDimX = 1500;
    public static final int panelDimY = 1000;
    public static int targetFPS = 1000;
    public static int targetFrameLength = 1000 / (targetFPS + 1);
}
