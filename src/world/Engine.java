package world;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import constants.UiConstants;
import things.Thing;
import utilities.Keyboard;

public class Engine extends JPanel implements ActionListener {
    World world = new World();
    Procedural procedural = new Procedural(world);
    Timer timer;
    int currentFPS = UiConstants.targetFPS;
    long currentTime = System.currentTimeMillis();
    int framesSinceLastFPS = 0;
    long timeOfLastFPS = System.currentTimeMillis();

    Engine() {
        this.setPreferredSize(new Dimension((int) (UiConstants.povDimX * UiConstants.enlargeFactor),
                (int) (UiConstants.povDimY * UiConstants.enlargeFactor)));
        this.setBackground(Color.black);
        timer = new Timer(0, this);
        timer.start();
    }

    public void updateFPS(Graphics g) {
        throttleFPS();
        framesSinceLastFPS ++;
        long timeSinceLastFPS = System.currentTimeMillis() - timeOfLastFPS;
        if (timeSinceLastFPS > 1000) {
            currentFPS = 1000 * framesSinceLastFPS / (int) timeSinceLastFPS;
            framesSinceLastFPS = 0;
            timeOfLastFPS = System.currentTimeMillis();
        }
        g.setColor(Color.white);
        String strFPS = String.valueOf(currentFPS);
        g.drawString("FPS: " + strFPS + "; Trees: " + world.things.size(), 0, 12);
    }

    public void throttleFPS() {
        long frameLength = System.currentTimeMillis() - currentTime;
        if (frameLength < UiConstants.targetFrameTime) {
            try {
                Thread.sleep(UiConstants.targetFrameTime - frameLength);
            }
            catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        currentTime = System.currentTimeMillis();
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        String[] paintOrder = new String[]{"Bush", "Tree"};
        for (String className: paintOrder) {
            scanThingsToPaint(className, g2D);
        }
        updateFPS(g);
    }

    private void scanThingsToPaint(String className, Graphics2D g2D) {
        for (Thing thing : world.things) {
            String thingClass = thing.getClass().getSimpleName();
            if (thing.size >= thing.minSizeToShow && thingClass.equals(className)) {
                paintThing(thing, g2D);
            }
        }
    }

    private void paintThing(Thing thing, Graphics2D g2D) {
        float xPos = transformCoordinate(thing.xPosition, thing.size, world.playerPositionX, UiConstants.povDimX);
        float yPos = transformCoordinate(thing.yPosition, thing.size, world.playerPositionY, UiConstants.povDimY);
        float size = thing.size * UiConstants.enlargeFactor;
        if (inView(xPos, yPos, size)) {
            g2D.drawImage(thing.itemImage, (int) (xPos * UiConstants.enlargeFactor),
                    (int) (yPos * UiConstants.enlargeFactor), (int) size, (int) size, null);
        }
    }

    private boolean inView(float xPos, float yPos, float size) {
        return xPos + size / 2 >= 0 && yPos + size / 2 >= 0
                && xPos - size / 2 < UiConstants.povDimX
                && yPos - size / 2 < UiConstants.povDimY;
    }

    private float transformCoordinate(float position, float size, float fovPosition, float dimension) {
        position = position - size / 2;
        position = position - fovPosition + dimension / 2;
        return position;
    }

    private void keyboardCheck() {
        if (Keyboard.isKeyPressed(KeyEvent.VK_W)) {
            this.world.playerPositionY -= UiConstants.scrollSpeed / this.currentFPS;
        }
        if (Keyboard.isKeyPressed(KeyEvent.VK_S)) {
            this.world.playerPositionY += UiConstants.scrollSpeed / this.currentFPS;
        }
        if (Keyboard.isKeyPressed(KeyEvent.VK_A)) {
            this.world.playerPositionX -= UiConstants.scrollSpeed / this.currentFPS;
        }
        if (Keyboard.isKeyPressed(KeyEvent.VK_D)) {
            this.world.playerPositionX += UiConstants.scrollSpeed / this.currentFPS;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        procedural.updateBins();
        world.currentFPS = this.currentFPS;
        world.updateWorld();
        repaint();
        keyboardCheck();
    }
}