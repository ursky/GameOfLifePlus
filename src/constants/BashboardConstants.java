package constants;

public final class BashboardConstants {
    public static final int dashboardHeight = 100;

    // frame latency plot constants
    public static final int latencyPlotXStart = (int) (UiConstants.panelWidth * 0.001f);
    public static final int latencyPlotXEnd = latencyPlotXStart + (int) (UiConstants.panelWidth * 0.15f);
    public static final int latencyPlotIncrement = 1;

    // frames per second plot constants
    public static final int fpsPlotXStart = (int) (UiConstants.panelWidth * 0.18);
    public static final int fpsPlotXEnd = fpsPlotXStart + (int) (UiConstants.panelWidth * 0.15f);
    public static final int fpsPlotIncrement = 1;

    // total thing count plot constants
    public static final int countPlotXStart = (int) (UiConstants.panelWidth * 0.36);
    public static final int countPlotXEnd = countPlotXStart + (int) (UiConstants.panelWidth * 0.15f);
    public static final int countPlotIncrement = 1;

    // counts stacked plot constants
    public static final int stackedPlotXStart = (int) (UiConstants.panelWidth * 0.54f);
    public static final int stackedPlotXEnd = stackedPlotXStart + (int) (UiConstants.panelWidth * 0.15f);
    public static final int stackedPlotIncrement = 1;

    // icons and buttons constants
    public static final int buttonsPlotXStart = (int) (UiConstants.panelWidth * 0.72f);
    public static final int buttonsPlotXEnd = buttonsPlotXStart + (int) (UiConstants.panelWidth * 0.25f);
}