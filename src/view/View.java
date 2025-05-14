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
        Window.setSize(BOARD_WIDTH, BOARD_HEIGHT);
        Window.setLocationRelativeTo(null); // center on screen
        Window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Window.setResizable(false);
        Window.setContentPane(Container);

        // COMPONENTS
        Container.setLayout(cardLayout);
        Container.add(gamePanel, PanelName.GAME.getName());
        Container.add(startMenuPanel, PanelName.START_MENU.getName());
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
    }

    private final GamePanel gamePanel = new GamePanel();
    public GamePanel getGamePanel(){
        return gamePanel;
    }

    private final StartMenuPanel startMenuPanel = new StartMenuPanel();
    public StartMenuPanel getStartMenuPanel(){
        return startMenuPanel;
    }
//    /**
//     * Displays the specified panel.
//     *
//     * @param panelName the panel to be displayed, represented as an enumeration constant
//     *                  defining the type of panel and its associated unique name.
//     * @return the displayed view component, cast as an {@code AbsViewComp}, for further manipulation if needed.
//     * @see PanelName
//     */
//    public JPanel showPanel(PanelName panelName){
//        cardLayout.show(Container, panelName.getName());
//        return new JPanel(); // todo FIX this
//    }









}
