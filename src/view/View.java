package view;


import javax.swing.*;
import java.awt.*;

import static config.View.BOARD_HEIGHT;
import static config.View.BOARD_WIDTH;

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
    /**
     * An enumeration representing the different panels or views that can be displayed in the application.
     * Each constant is associated with a unique string name to facilitate the identification
     * and management.
     */
    public enum PanelName {
        START_MENU("startMenu"),
        GAME("game");

        private final  String name;
        PanelName(String name){this.name = name;}
        public String getName() {return name;}
    }

    private final JFrame Window = new JFrame();
    private final JPanel Container = new JPanel(); // need this for card layout
    private final CardLayout cardLayout = new CardLayout();


    private View(){
        Window.setTitle("Sugar Rush");
        // using setSize() because component doesnt have a parent neither a layout manager.ù
        // setSize() sets the absolute size instead setPreferredSize() is more an 'hint' to layout manager.
        Window.setLocationRelativeTo(null); // center on screen
        Window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Window.setResizable(false);

        // CONTAINER
        Window.setContentPane(Container);

        // COMPONENTS
        Container.setLayout(cardLayout);
        Container.add(gamePanel, PanelName.GAME.getName());
        Container.add(startMenuPanel, PanelName.START_MENU.getName());

        Window.pack();
    }

    // VIEW ACTIONS //
    public void updateView(){
        Window.repaint();
    }

    public void launchView(){
        Window.setVisible(true);
    }

    public void showPanel(String panelName){
        assert panelName != null;
        cardLayout.show(Container, panelName);
        Container.revalidate();
    }

    private final GamePanel gamePanel = new GamePanel();
    public GamePanel getGamePanel(){
        return gamePanel;
    }

    private final StartMenuPanel startMenuPanel = new StartMenuPanel();
    public StartMenuPanel getStartMenuPanel(){
        return startMenuPanel;
    }









}
