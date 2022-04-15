package engine;

import engine.Engine;

import javax.swing.*;

public class Frame extends JFrame {
    Engine panel;

    public Frame() {
        panel = new Engine();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}