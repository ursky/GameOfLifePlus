package game.dashboard;

import game.userIO.UiConstants;
import game.Engine;

public class Dashboard {
    Engine engine;
    StackedLinePlot countsPlot;
    public CreatureIcons creatureIcons;
    LinePlot latencyPlot, fpsPlot, totalCountPlot;
    int minY, maxY, minX, maxX;

    public void update() {
        if (this.engine.tracker.timeToUpdateCounts() ) {
            this.countsPlot.update(this.engine.world.counter.classFractions, this.engine.world.counter.colors);
            this.latencyPlot.update(this.engine.tracker.latencyList);
            this.fpsPlot.update(this.engine.tracker.fpsList);
            this.totalCountPlot.update(this.engine.world.counter.totalCounts);
            this.creatureIcons.update(this.engine.world.counter.thingCounts);
        }
    }

    public void paint() {
        if (this.engine.tracker.frameCounter > 0) {
            this.paintBar();
            this.countsPlot.draw();
            this.latencyPlot.draw();
            this.fpsPlot.draw();
            this.totalCountPlot.draw();
            this.creatureIcons.draw();
        }
    }

    public void paintBar() {
        // paint blank info bar
        this.engine.g2D.setColor(DashboardConstants.backgroundColor);
        this.engine.g2D.fillRect(
                0, this.minY, (this.maxX - this.minX), (this.maxY - this.minY));
    }

    public Dashboard(Engine engine) {
        this.engine = engine;
        this.countsPlot = new StackedLinePlot(
                this.engine,
                DashboardConstants.stackedPlotXStart,
                DashboardConstants.stackedPlotXEnd,
                DashboardConstants.xPixelIncrement,
                "Relative abundance");

        this.latencyPlot = new LinePlot(
                this.engine,
                DashboardConstants.latencyPlotXStart,
                DashboardConstants.latencyPlotXEnd,
                DashboardConstants.xPixelIncrement,
                "Latency (ms)");

        this.fpsPlot = new LinePlot(
                this.engine,
                DashboardConstants.fpsPlotXStart,
                DashboardConstants.fpsPlotXEnd,
                DashboardConstants.xPixelIncrement,
                "FPS");

        this.totalCountPlot = new LinePlot(
                this.engine,
                DashboardConstants.countPlotXStart,
                DashboardConstants.countPlotXEnd,
                DashboardConstants.xPixelIncrement,
                "Rendered things #");

        this.creatureIcons = new CreatureIcons(
                this.engine,
                DashboardConstants.buttonsPlotXStart,
                DashboardConstants.buttonsPlotXEnd,
                "Critter buttons and counts");

        // useful pre-computed bounds
        this.minY = UiConstants.panelHeight - DashboardConstants.dashboardHeight;
        this.maxY = UiConstants.panelHeight;
        this.minX = 0;
        this.maxX = UiConstants.panelWidth;
    }
}