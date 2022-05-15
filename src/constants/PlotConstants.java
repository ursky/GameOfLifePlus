package constants;

public final class PlotConstants {
    public static final int dashboardHeight = 100;

    // frame latency plot constants
    public static final int latencyPlotXStart = (int) (UiConstants.panelWidth * 0.01f);
    public static final int latencyPlotXEnd = latencyPlotXStart + (int) (UiConstants.panelWidth * 0.2f);
    public static final int latencyPlotIncrement = 1;

    // frames per second plot constants
    public static final int fpsPlotXStart = (int) (UiConstants.panelWidth * 0.25);
    public static final int fpsPlotXEnd = fpsPlotXStart + (int) (UiConstants.panelWidth * 0.2f);
    public static final int fpsPlotIncrement = 1;

    // counts stacked plot constants
    public static final int stackedPlotXStart = (int) (UiConstants.panelWidth * 0.5f);
    public static final int stackedPlotXEnd = stackedPlotXStart + (int) (UiConstants.panelWidth * 0.2f);
    public static final int stackedPlotIncrement = 3;
}