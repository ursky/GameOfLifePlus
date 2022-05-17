package game.utilities;

import game.Engine;
import game.userIO.UiConstants;

import java.util.ArrayList;

public class TimeTracker {
    public Engine engine;
    public float currentFPS = UiConstants.targetFPS;
    public long currentTime = System.currentTimeMillis();
    private int framesSinceLastFPS = 0;
    private long timeOfLastFPS = this.currentTime;
    private long timeOfLastUpdate = System.nanoTime();
    private long timeOfLastPrint = this.currentTime;
    public long timeOfLastCount = this.currentTime;
    public int frameCounter = 0;
    private boolean printThisFrame = false;
    public float threadBuffer = 0;
    public int latency;
    public int sleepDelay;
    public ArrayList<Integer> latencyList;
    public ArrayList<Integer> fpsList;

    public void printStepNanoseconds(String stepName) {
        long currentTime = System.nanoTime();
        long timeDelta = currentTime - this.timeOfLastUpdate;
        this.timeOfLastUpdate = currentTime;
        if (this.printThisFrame && UiConstants.doPrintStepNanoseconds) {
            System.out.println(stepName + ": " + (float) timeDelta / 1000 + " ns");
        }
    }

    public boolean timeToUpdateCounts() {
        return this.currentTime - this.timeOfLastCount >= UiConstants.infoUpdateMs;
    }

    public void updateFPS() {
        this.frameCounter++;
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

    public TimeTracker(Engine engine) {
        this.engine = engine;
        this.latencyList = new ArrayList<>();
        this.fpsList = new ArrayList<>();
    }
}