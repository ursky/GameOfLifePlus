package game.visuals;

import game.Engine;

import javax.swing.*;

public class CreateFrame extends JFrame {
    Engine engine;

    public CreateFrame() {
        engine = new Engine();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(engine);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}