package engine.dashboard;

import constants.PlotConstants;
import constants.UiConstants;
import engine.Engine;

import java.awt.*;

public class infoDashboard {
    Engine engine;
    StackedLinePlot countsPlot;
    LinePlot latencyPlot, fpsPlot;
    int minY, maxY, minX, maxX;

    public void update() {
        if (this.engine.tracker.timeToUpdateCounts() ) {
            this.countsPlot.update(this.engine.world.counter.classFractions, this.engine.world.counter.colors);
            this.latencyPlot.update(this.engine.tracker.latencyList);
            this.fpsPlot.update(this.engine.tracker.fpsList);
        }
    }

    public void paint() {
        this.paintBar();
        this.countsPlot.draw();
        this.latencyPlot.draw();
        this.fpsPlot.draw();
    }

    public void paintBar() {
        // paint blank info bar
        this.engine.g2D.setColor(Color.darkGray);
        this.engine.g2D.fillRect(
                0, this.minY, (this.maxX - this.minX), (this.maxY - this.minY));
    }

    public infoDashboard(Engine engine) {
        this.engine = engine;
        this.countsPlot = new StackedLinePlot(
                this.engine,
                PlotConstants.stackedPlotXStart,
                PlotConstants.stackedPlotXEnd,
                PlotConstants.stackedPlotIncrement,
                "Relative abundance");
        this.latencyPlot = new LinePlot(
                this.engine,
                PlotConstants.latencyPlotXStart,
                PlotConstants.latencyPlotXEnd,
                PlotConstants.latencyPlotIncrement,
                "Latency (ms)");

        this.fpsPlot = new LinePlot(
                this.engine,
                PlotConstants.fpsPlotXStart,
                PlotConstants.fpsPlotXEnd,
                PlotConstants.fpsPlotIncrement,
                "FPS");

        // useful pre-computed bounds
        this.minY = UiConstants.panelHeight - PlotConstants.dashboardHeight;
        this.maxY = UiConstants.panelHeight;
        this.minX = 0;
        this.maxX = UiConstants.panelWidth;
    }
}