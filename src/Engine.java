import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import static constants.Constants.*;
import constants.LifeConstants;
import creatures.Creature;

public class Engine extends JPanel implements ActionListener {
    World world = new World();
    Timer timer;
    int currentFPS = 0;
    long currentTime = 0;
    int framesSinceLastFPS = 0;
    long timeOfLastFPS = 0;

    Engine() {
        this.setPreferredSize(new Dimension(panelDimX, panelDimY));
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
        g.drawString("FPS: " + strFPS + "; Trees: " + world.creatures.size(), 0, 12);
    }

    public void throttleFPS() {
        long frameLength = System.currentTimeMillis() - currentTime;
        if (frameLength < targetFrameLength) {
            try {
                Thread.sleep(targetFrameLength - frameLength);
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

        for (Creature creature : world.creatures) {
            if (creature.size >= LifeConstants.treeMinSize) {
                g2D.drawImage(creature.creatureImage,
                        (int) (creature.xPosition - creature.size / 2),
                        (int) (creature.yPosition - creature.size / 2),
                        (int) creature.size, (int) creature.size, null);
            }
        }
        updateFPS(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        world.updateWorld();
        repaint();
    }
}