package controller.menu;

import model.Model;
import view.View;
import view.impl.home.levelsMap.LevelDialog;
import view.impl.home.levelsMap.LevelInfoDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

public class LevelInfoController {
    private final LevelInfoDialog panel;
    private int levelIndex;

    public LevelInfoController(LevelInfoDialog panel, int levelIndex) {
        this.panel = panel;
        this.levelIndex = levelIndex;
    }

    public void onClose(){
        Window window = SwingUtilities.getWindowAncestor(panel);
        if (window != null) {
            window.dispose(); // Close the dialog
        }
    }

    public void onPlay(ActionEvent e){
        assert e != null : "ActionEvent cannot be null. This method is intended to be called from a component's actionListener.";

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
//        panel.showCustomDialog(informationDialog);
    }

    // necessary to avoid re-initialization each time another level is selected
    public void setLevelIndex(int levelIndex) {
        this.levelIndex = levelIndex;
    }
}
