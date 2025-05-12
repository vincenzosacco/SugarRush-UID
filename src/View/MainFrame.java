package View;


import Controller.SugarController;
import Model.Game;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import static config.View.BOARD_HEIGHT;
import static config.View.BOARD_WIDTH;

/**
 *
 */
public class MainFrame extends JFrame{
    // Singleton
    private static MainFrame instance = null;

    // panels
    private SugarPanel gamePanel;

    private MainFrame(){
        this.setTitle("Sugar Rush");
        // using setSize() because component doesnt have a parent neither a layout manager.ù
        // setSize() sets the absolute size instead setPreferredSize() is more an 'hint' to layout manager.
        this.setSize(BOARD_WIDTH, BOARD_HEIGHT);

        this.setLocationRelativeTo(null); // center on screen
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        // init panels
        this.sugarPanel();

        JOptionPane.showMessageDialog(this,"Try to reach the sugar piece",null,JOptionPane.INFORMATION_MESSAGE);
    }

    public static MainFrame getInstance(){
        if (instance == null) {
            instance = new MainFrame();
        }
        return instance;
    }

    public void launch(){
        this.setVisible(true);
    }

    // PRIVATE //
    private void sugarPanel(){
        gamePanel = new SugarPanel();
        gamePanel.setController(new SugarController(gamePanel, Game.getInstance()));

        this.add(gamePanel);
        this.pack();
    }
}
