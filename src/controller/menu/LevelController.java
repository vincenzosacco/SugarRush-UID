package controller.menu;

import model.Model;
import view.View;
import view.impl.home.levelsMap.LevelDialog;
import view.impl.home.levelsMap.LevelInfoDialog;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class LevelController  {
    private final LevelInfoDialog panel;

    public LevelController(LevelInfoDialog panel) {
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
        View.getInstance().getGamePanel().repaintBackground();
        View.getInstance().showGame();
        panel.requestFocusInWindow(); // needed to get user input
        Objects.requireNonNull(panel.getParent(), "The panel does not have a defined parent");
        LevelDialog informationDialog=new LevelDialog("New Game","Try to reach the sugar piece", "Start");
        panel.showCustomDialog(informationDialog);
    }
}
