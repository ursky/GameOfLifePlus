package game.utilities;

import game.Engine;
import game.constants.UiConstants;

import java.util.ArrayList;

/**
 * This class is broadly responsible for keeping time for the game. This includes FPS, delay throttling, saving
 * latency information for dashboard plotting, keeping track of the last update, etc.
 */
public class TimeTracker {
    public Engine engine;
    public float currentFPS = UiConstants.targetFPS;
    public long currentTime = System.currentTimeMillis();
    private int framesSinceLastFPS = 0;
    public int frameCounter = 0;
    private boolean printThisFrame = false;
    public float threadBuffer = 0;
    public int latency;
    public int sleepDelay;

    // this is to keep track of when the last print/update messages were updated
    private long timeOfLastFPS = this.currentTime;
    private long timeOfLastUpdate = System.nanoTime();
    private long timeOfLastPrint = this.currentTime;
    public long timeOfLastCount = this.currentTime;

    // these store data for the dashboard
    public ArrayList<Integer> latencyList;
    public ArrayList<Integer> fpsList;

    /**
     * Prints the time (in nanoseconds) since the last update like this. This is useful when called many times
     * throughout the program to test the duration of different steps.
     * @param stepName: the name of the step that was timed here
     */
    public void printStepNanoseconds(String stepName) {
        long currentTime = System.nanoTime();
        long timeDelta = currentTime - this.timeOfLastUpdate;
        this.timeOfLastUpdate = currentTime;
        if (this.printThisFrame && UiConstants.doPrintStepNanoseconds) {
            System.out.println(stepName + ": " + (float) timeDelta / 1000 + " ns");
        }
    }

    /**
     * Check if it is time to update the FPS message and dashboard this frame
     * @return: should we update this frame?
     */
    public boolean timeToUpdateCounts() {
        return this.currentTime - this.timeOfLastCount >= UiConstants.infoUpdateMs;
    }

    /**
     * Update the FPS information for this frame
     */
    public void updateFPS() {
        this.frameCounter++;
        // the thread buffer is a moving line indicating the division lines for quad tree search threads
        this.threadBuffer = this.frameCounter % (this.engine.procedural.loadRangeWidth);
        this.throttleFPS();
        this.framesSinceLastFPS ++;
        long time = System.currentTimeMillis();
        long timeSinceLastFPS = time - this.timeOfLastFPS;
        long timeSinceLastPrint = time - this.timeOfLastPrint;

        if (timeSinceLastFPS > UiConstants.infoUpdateMs) {
            this.currentFPS = 1000f * this.framesSinceLastFPS / (int) timeSinceLastFPS;
            this.currentFPS = Math.min(this.currentFPS, UiConstants.targetFPS);
            this.currentFPS = Math.max(this.currentFPS, 1);
            this.framesSinceLastFPS = 0;
            this.timeOfLastFPS = time;
        }
        if (timeSinceLastPrint > UiConstants.infoUpdateMs) {
            this.printThisFrame = true;
            this.timeOfLastPrint = time;
        }
        else {
            this.printThisFrame = false;
        }
    }

    /**
     * Wait for time to catch up if this frame was too quick!
     */
    public void throttleFPS() {
        this.latency = (int) (System.currentTimeMillis() - currentTime);
        if (this.latency < UiConstants.targetFrameTime) {
            try {
                this.sleepDelay = (int) (UiConstants.targetFrameTime - this.latency);
                Thread.sleep(this.sleepDelay);
            }
            catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        if (this.timeToUpdateCounts()) {
            this.latencyList.add(this.latency);
            this.fpsList.add(1000 / (this.sleepDelay + this.latency));
            this.timeOfLastCount = this.currentTime;
        }
        this.currentTime = System.currentTimeMillis();
    }

    /**
     * Initialize time tracker
     * @param engine: game engine
     */
    public TimeTracker(Engine engine) {
        this.engine = engine;
        this.latencyList = new ArrayList<>();
        this.fpsList = new ArrayList<>();
    }
}