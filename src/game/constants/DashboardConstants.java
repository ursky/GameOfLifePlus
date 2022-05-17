package game.constants;

import java.awt.*;

/**
 * Store plotting display constants for the dashboard at the bottom of the screen
 */
public final class DashboardConstants {
    // this constant helps resize things like line thicknesses and fonts if the window size changes (not dynamically)
    public static final float resizeRatio = UiConstants.panelWidth / 1600f;

    // color visuals of dashboard plots
    public static final Color backgroundColor = Color.darkGray;
    public static final Color mainColor = Color.white;
    public static final int fontSize = (int) (10 * resizeRatio);

    // height of the dashboard
    public static final int dashboardHeight = (int) (100 * resizeRatio);
    // this is how fast the graphs move per second (higher means faster since each time point will occupy more pixels)
    public static final int xPixelIncrement = 2;

    // the following constants just define where each plot starts and ends on the x-axis

    // frame latency plot constants
    public static final int latencyPlotXStart = 0;
    public static final int latencyPlotXEnd = latencyPlotXStart + (int) (UiConstants.panelWidth * 0.15f);

    // frames per second plot constants
    public static final int fpsPlotXStart = (int) (UiConstants.panelWidth * 0.17);
    public static final int fpsPlotXEnd = fpsPlotXStart + (int) (UiConstants.panelWidth * 0.15f);

    // total thing count plot constants
    public static final int countPlotXStart = (int) (UiConstants.panelWidth * 0.34);
    public static final int countPlotXEnd = countPlotXStart + (int) (UiConstants.panelWidth * 0.15f);

    // counts stacked plot constants
    public static final int stackedPlotXStart = (int) (UiConstants.panelWidth * 0.51f);
    public static final int stackedPlotXEnd = stackedPlotXStart + (int) (UiConstants.panelWidth * 0.15f);

    // icons and buttons constants
    public static final int buttonsPlotXStart = (int) (UiConstants.panelWidth * 0.68f);
    public static final int buttonsPlotXEnd = buttonsPlotXStart + (int) (UiConstants.panelWidth * 0.30f);
}