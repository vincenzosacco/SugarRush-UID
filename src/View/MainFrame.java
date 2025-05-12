package View;


import javax.swing.*;

import static Config.View.BOARD_HEIGHT;
import static Config.View.BOARD_WIDTH;

/**
 * Main application frame for the "Sugar Rush" game.
 * <p>
 * This class serves as the main window of the game, implementing the Singleton design pattern
 * to ensure only one instance of the frame exists throughout the application lifecycle.
 * It is responsible for initializing and managing the game interface, providing the primary functionality
 * to display the game and interact with the user.
 * </p>
 */
public class MainFrame extends JFrame {
    // Singleton
    private static MainFrame instance = null;

    // panels
    private final GamePanel gamePanel = new GamePanel();

    private MainFrame(){
        this.setTitle("Sugar Rush");
        // using setSize() because component doesnt have a parent neither a layout manager.ù
        // setSize() sets the absolute size instead setPreferredSize() is more an 'hint' to layout manager.
        this.setSize(BOARD_WIDTH, BOARD_HEIGHT);

        this.setLocationRelativeTo(null); // center on screen
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
    }

    public static MainFrame getInstance(){
        if (instance == null) {
            instance = new MainFrame();
        }
        return instance;
    }

    public void launch(){
        this.setVisible(true);
        JOptionPane.showMessageDialog(this,"Try to reach the sugar piece",null,JOptionPane.INFORMATION_MESSAGE);
    }

    public GamePanel addGamePanel(){
        this.add(gamePanel);
        this.pack();
        return gamePanel;
    }
}
