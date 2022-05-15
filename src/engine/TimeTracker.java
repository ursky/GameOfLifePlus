package engine;

import constants.UiConstants;

import java.util.ArrayList;

public class TimeTracker {
    public Engine engine;
    public float currentFPS = UiConstants.targetFPS;
    private long currentTime = System.currentTimeMillis();
    private int framesSinceLastFPS = 0;
    private long timeOfLastFPS = this.currentTime;
    private long timeOfLastUpdate = System.nanoTime();
    private long timeOfLastPrint = this.currentTime;
    private long timeOfLastCount = this.currentTime;
    public int frameCounter = 0;
    private boolean printThisFrame = false;
    public float threadBuffer = 0;
    public int latency;
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

    public void printCounts() {
        if (timeToUpdateCounts()) {
            String message = this.engine.world.counter.generateCountsMessage();
            System.out.println(message);
            this.timeOfLastCount = this.currentTime;
        }
    }

    public boolean timeToUpdateCounts() {
        return this.currentTime - this.timeOfLastCount >= UiConstants.printDisplayUpdateMs;
    }

    public void updateFrames() {
        this.frameCounter++;
        this.threadBuffer = this.frameCounter % (this.engine.procedural.loadRangeWidth);
    }

    public String updateFPS() {
        this.throttleFPS();
        this.framesSinceLastFPS ++;
        long time = System.currentTimeMillis();
        long timeSinceLastFPS = time - this.timeOfLastFPS;
        long timeSinceLastPrint = time - this.timeOfLastPrint;

        if (timeSinceLastFPS > UiConstants.fpsDisplayUpdateMs && !this.engine.userIO.fastForward()) {
            this.currentFPS = 1000f * this.framesSinceLastFPS / (int) timeSinceLastFPS;
            this.currentFPS = Math.min(this.currentFPS, UiConstants.targetFPS);
            this.currentFPS = Math.max(this.currentFPS, 1);
            this.framesSinceLastFPS = 0;
            this.timeOfLastFPS = time;
        }
        if (timeSinceLastPrint > UiConstants.printDisplayUpdateMs) {
            this.printThisFrame = true;
            this.timeOfLastPrint = time;
        }
        else {
            this.printThisFrame = false;
        }
        this.fpsList.add((int) this.currentFPS);
        String strFPS = String.valueOf((int)currentFPS);
        return "FPS: " + strFPS + "; #Things: " + this.engine.world.things.size();
    }

    public void throttleFPS() {
        this.latency = (int) (System.currentTimeMillis() - currentTime);
        this.latencyList.add(this.latency);
        if (this.engine.userIO.fastForward()) {
            this.currentFPS = UiConstants.fastPreRenderFPS;
        }
        else if (this.latency < UiConstants.targetFrameTime) {
            try {
                Thread.sleep((long) (UiConstants.targetFrameTime - this.latency));
            }
            catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        this.currentTime = System.currentTimeMillis();
    }
    TimeTracker(Engine engine) {
        this.engine = engine;
        this.latencyList = new ArrayList<>();
        this.fpsList = new ArrayList<>();
    }
}