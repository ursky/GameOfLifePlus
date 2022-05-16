package engine.dashboard;

import constants.BashboardConstants;
import constants.UiConstants;
import engine.Engine;

import java.awt.*;

public class infoDashboard {
    Engine engine;
    StackedLinePlot countsPlot;
    Buttons buttons;
    LinePlot latencyPlot, fpsPlot, totalCountPlot;
    int minY, maxY, minX, maxX;

    public void update() {
        if (this.engine.tracker.timeToUpdateCounts() ) {
            this.countsPlot.update(this.engine.world.counter.classFractions, this.engine.world.counter.colors);
            this.latencyPlot.update(this.engine.tracker.latencyList);
            this.fpsPlot.update(this.engine.tracker.fpsList);
            this.totalCountPlot.update(this.engine.world.counter.totalCounts);
            this.buttons.update(this.engine.world.counter.thingCounts);
        }
    }

    public void paint() {
        if (this.engine.tracker.frameCounter > 2) {
            this.paintBar();
            this.countsPlot.draw();
            this.latencyPlot.draw();
            this.fpsPlot.draw();
            this.totalCountPlot.draw();
            this.buttons.draw();
        }
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
                BashboardConstants.stackedPlotXStart,
                BashboardConstants.stackedPlotXEnd,
                BashboardConstants.stackedPlotIncrement,
                "Relative abundance");

        this.latencyPlot = new LinePlot(
                this.engine,
                BashboardConstants.latencyPlotXStart,
                BashboardConstants.latencyPlotXEnd,
                BashboardConstants.latencyPlotIncrement,
                "Latency (ms)");

        this.fpsPlot = new LinePlot(
                this.engine,
                BashboardConstants.fpsPlotXStart,
                BashboardConstants.fpsPlotXEnd,
                BashboardConstants.fpsPlotIncrement,
                "FPS");

        this.totalCountPlot = new LinePlot(
                this.engine,
                BashboardConstants.countPlotXStart,
                BashboardConstants.countPlotXEnd,
                BashboardConstants.countPlotIncrement,
                "Rendered things #");

        this.buttons = new Buttons(
                this.engine,
                BashboardConstants.buttonsPlotXStart,
                BashboardConstants.buttonsPlotXEnd,
                0,
                "Critter buttons and counts");

        // useful pre-computed bounds
        this.minY = UiConstants.panelHeight - BashboardConstants.dashboardHeight;
        this.maxY = UiConstants.panelHeight;
        this.minX = 0;
        this.maxX = UiConstants.panelWidth;
    }
}