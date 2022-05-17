package game.dashboard;

import game.constants.DashboardConstants;
import game.constants.UiConstants;
import game.Engine;

/**
 * Handles the plots, images, and calculations for the entire dashboard at the bottom of the screen.
 */
public class Dashboard {
    Engine engine;
    // this plot is for storing the stacked counts plot (the rainbow one)
    StackedLinePlot countsPlot;
    // this stores the creature buttons
    public CreatureIcons creatureIcons;
    // this is for the other more basic line plots
    LinePlot latencyPlot, fpsPlot, totalCountPlot;
    // the bounds of the whole dashboard
    int minY, maxY, minX, maxX;

    /**
     * Go over all the plots and update their values/status. This is done every few frames.
     */
    public void update() {
        // notice they are not updated every frame
        if (this.engine.tracker.timeToUpdateCounts() ) {
            this.countsPlot.update(this.engine.world.counter.classFractions, this.engine.world.counter.colors);
            this.latencyPlot.update(this.engine.tracker.latencyList);
            this.fpsPlot.update(this.engine.tracker.fpsList);
            this.totalCountPlot.update(this.engine.world.counter.totalCounts);
            this.creatureIcons.update(this.engine.world.counter.thingCounts);
        }
    }

    /**
     * Display the plots on the dashboard. This is done every frame.
     */
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

    /**
     * Paints the bounds of the dashboard for a cleaner look
     */
    public void paintBar() {
        // paint blank info bar
        this.engine.g2D.setColor(DashboardConstants.backgroundColor);
        this.engine.g2D.fillRect(
                0, this.minY, (this.maxX - this.minX), (this.maxY - this.minY));
    }

    /**
     * Initialize the plots in the dashboard.
     * @param engine: game engined
     */
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