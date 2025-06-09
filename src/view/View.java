package view;

import utils.audio.GameAudioController;
import view.game.GamePanel;
import view.menu.CustomTabbedPane;
import view.menu.StartMenuPanel;
import view.settings.GameSettings;

import javax.swing.*;
import java.awt.*;

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
     * An enumeration representing the different panels or views that can be displayed in the gamelication.
     * Each constant is associated with a unique string name to facilitate the identification
     * and management.
     */
    public enum PanelName {
        START_MENU("startMenu"),
        GAME("game"),
        CUSTOM_TABBED_PANE("customTabbedPane"),
        LEVEL_EDITOR("levelEditor"),
        SHOP("shop"),
        SETTINGS("settings");

        private final  String name;
        PanelName(String name){this.name = name;}
        public String getName() {return name;}
    }

    private final JFrame Window = new JFrame();
    private final JPanel Container = new JPanel(); // need this for card layout
    private final CardLayout cardLayout = new CardLayout();

    private final StartMenuPanel startMenuPanel;

    private final CustomTabbedPane customTabbedPane;

    private final GameSettings gameSettings;

    private View(){
        Window.setTitle("Sugar Rush");
        // using setSize() because component doesnt have a parent neither a layout manager.ù
        // setSize() sets the absolute size instead setPreferredSize() is more an 'hint' to layout manager.
        Window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Window.setResizable(false);

        // CONTAINER
        Window.setContentPane(Container);

        startMenuPanel = new StartMenuPanel();
        customTabbedPane = new CustomTabbedPane();
        gameSettings =new GameSettings();

        // COMPONENTS
        Container.setLayout(cardLayout);
        Container.add(gamePanel, PanelName.GAME.getName());
        Container.add(startMenuPanel, PanelName.START_MENU.getName());
        Container.add(customTabbedPane,PanelName.CUSTOM_TABBED_PANE.getName());
        Container.add(gameSettings,PanelName.SETTINGS.getName());


        Window.pack();

        Window.setLocationRelativeTo(null); // center on screen

        GameAudioController.getInstance().playMenuMusic();
    }

    // VIEW ACTIONS //

    /**
     * Notifies the view to refresh or update its state.
     * This method is typically responsible for ensuring that changes
     * to the underlying model or controller data are reflected
     * in the user interface elements currently displayed.
     */
    public void notifyView(){
        Window.repaint();
    }

    public void launchView(){
        Window.setVisible(true);
    }

    public void showPanel(String panelName){
        assert panelName != null;
        cardLayout.show(Container, panelName);
        Container.revalidate();
        Container.repaint();
        // Request focus to the panel that is being shown TODO modfica questo
        for (Component comp : Container.getComponents()) {
            if (comp.isVisible() && comp instanceof JComponent) {
                ((JComponent) comp).requestFocusInWindow();
                break; // Only request focus for the first visible component
            }
        }

        if (panelName.equals(PanelName.GAME.getName())) {
            GameAudioController.getInstance().stopBackgroundMusic();
            GameAudioController.getInstance().playGameMusic();
        }else{
            GameAudioController.getInstance().stopBackgroundMusic();
            GameAudioController.getInstance().playMenuMusic();
        }
    }

    private final GamePanel gamePanel = new GamePanel();
    public GamePanel getGamePanel(){
        return gamePanel;
    }

    //GETTERS
    public CustomTabbedPane getCustomTabbedPane(){
        return customTabbedPane;
    }
}
