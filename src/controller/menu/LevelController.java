package controller.menu;

import controller.ControllerObj;
import controller.GameLoop;
import model.Model;
import view.View;
import view.menu.LevelPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class LevelController implements ControllerObj {
    private final LevelPanel panel;

    public LevelController(LevelPanel panel) {
        this.panel = panel;
    }

    public void onClose(){
        Window window = SwingUtilities.getWindowAncestor(panel);
        if (window != null) {
            window.dispose(); // Close the dialog
        }
    }

    public void onPlay(int levelIndex){
        View.getInstance().getGamePanel().getPauseButton().setEnabled(true);

        // 1. Close the LevelPanel dialog after starting the game
        Window window = SwingUtilities.getWindowAncestor(panel);
        if (window != null) {
            window.dispose(); // Close the current dialog
        }

        // 2. Start the level showing the GamePanel
        Model.getInstance().getGame().setLevel(levelIndex);
        View.getInstance().getGamePanel().resetPanelForNewLevel();
        View.getInstance().showPanel(View.PanelName.GAME.getName());
        panel.requestFocusInWindow(); // needed to get user input
        Objects.requireNonNull(panel.getParent(), "The panel does not have a defined parent");
        JOptionPane.showMessageDialog(panel.getParent(),"Try to reach the sugar piece",
                "New Game",JOptionPane.INFORMATION_MESSAGE);
        GameLoop.getInstance().start();
    }
}
