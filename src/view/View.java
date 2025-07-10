
package view;

import view.impl.game.GamePanel;
import view.impl.home.HomeContainer;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Singleton Interface to the View.
 */
public class View {
    // SINGLETON //
    private static View instance = null;

    public static View getInstance(){
        if (instance == null) {
            instance = new View();
        }
        return instance;
    }

    // VIEW //
    private final JFrame Window = new JFrame();

    private final GamePanel gamePanel = new GamePanel();
    private final HomeContainer home = new HomeContainer();

    private View(){
        Window.setTitle("Sugar Rush");
        // using setSize() because component doesnt have a parent neither a layout manager.ù
        // setSize() sets the absolute size instead setPreferredSize() is more an 'hint' to layout manager.

        Window.addWindowListener(new WindowAdapter() {
            // When minimizing the window
            @Override
            public void windowIconified(WindowEvent e) {
                super.windowIconified(e);
            }

            // When deminimizing the window
            @Override
            public void windowDeiconified(WindowEvent e) {
                super.windowDeiconified(e);
                Window.getContentPane().requestFocusInWindow();
            }
        });

        Window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Window.setResizable(false);
    }

    // VIEW ACTIONS //

    /**
     * Notifies the view to refresh or update its state.
     * This method is typically responsible for ensuring that changes
     * to the underlying model or controller data are reflected
     * in the user interface elements currently displayed.
     */
    public void refresh(){
        Window.repaint();
    }

    public void launch(){
        Window.setVisible(true);
        showHome();
    }


    public void showGame(){
        Window.setContentPane(gamePanel);
        Window.revalidate();
        Window.pack();
        Window.setLocationRelativeTo(null); // center on screen
    }
    public void showHome(){
        Window.revalidate();
        Window.setContentPane(home);
        Window.pack();
        Window.setLocationRelativeTo(null); // center on screen
    }

    //GETTERS
    public GamePanel getGamePanel(){
        return gamePanel;
    }
    public HomeContainer getHome(){
        return home;
    }
}
