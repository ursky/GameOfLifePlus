package engine;

import javax.swing.*;

public class CreateFrame extends JFrame {
    Engine panel;

    public CreateFrame() {
        panel = new Engine();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}