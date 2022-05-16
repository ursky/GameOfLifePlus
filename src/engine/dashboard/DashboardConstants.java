package engine.dashboard;

import engine.userIO.UiConstants;

import java.awt.*;

public final class DashboardConstants {
    public static final Color backgroundColor = Color.darkGray;
    public static final Color mainColor = Color.white;

    public static final int dashboardHeight = 100;
    public static final int xPixelIncrement = 2;

    // frame latency plot constants
    public static final int latencyPlotXStart = 0;
    public static final int latencyPlotXEnd = latencyPlotXStart + (int) (UiConstants.panelWidth * 0.15f);

    // frames per second plot constants
    public static final int fpsPlotXStart = (int) (UiConstants.panelWidth * 0.18);
    public static final int fpsPlotXEnd = fpsPlotXStart + (int) (UiConstants.panelWidth * 0.15f);

    // total thing count plot constants
    public static final int countPlotXStart = (int) (UiConstants.panelWidth * 0.36);
    public static final int countPlotXEnd = countPlotXStart + (int) (UiConstants.panelWidth * 0.15f);

    // counts stacked plot constants
    public static final int stackedPlotXStart = (int) (UiConstants.panelWidth * 0.54f);
    public static final int stackedPlotXEnd = stackedPlotXStart + (int) (UiConstants.panelWidth * 0.15f);

    // icons and buttons constants
    public static final int buttonsPlotXStart = (int) (UiConstants.panelWidth * 0.72f);
    public static final int buttonsPlotXEnd = buttonsPlotXStart + (int) (UiConstants.panelWidth * 0.25f);
}