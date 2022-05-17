package game.visuals;

import game.Game;

import javax.swing.*;

/**
 * JFrame extension for handling individual images
 */
public class CreateFrame extends JFrame {
    Game game;

    /**
     * Initialize JFrame extension and open a view window. Then launch the game by initializing Game!
     */
    public CreateFrame() {
        game = new Game();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(game);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}