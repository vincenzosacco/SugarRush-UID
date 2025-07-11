package controller.menu;

import model.Model;
import model.game.Game;
import view.View;
import view.impl.home.levelsMap.LevelDialog;
import view.impl.home.levelsMap.LevelInfoDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

public class LevelInfoController {
    private final LevelInfoDialog infoDialog;
    private int levelIndex;

    public LevelInfoController(LevelInfoDialog levelInfoDialog, int levelIndex) {
        this.infoDialog = levelInfoDialog;
        this.levelIndex = levelIndex;
    }

    public void onClose(ActionEvent e){
        assert e != null : "ActionEvent cannot be null. This method is intended to be called from a component's actionListener.";
        JDialog parent = (JDialog) SwingUtilities.getAncestorOfClass(JDialog.class, (Component) e.getSource());
        if (parent == null){
            throw new AssertionError("This method should be called a component with a JDialog as ancestor");
        }
        parent.dispose();
    }

    public void onPlay(ActionEvent e){
        assert e != null : "ActionEvent cannot be null. This method is intended to be called from a component's actionListener.";

        View.getInstance().getGamePanel().setEnablePauseButton(true);

        // 1. Close the LevelPanel dialog after starting the game
        Window window = SwingUtilities.getWindowAncestor(infoDialog);
        if (window != null) {
            window.dispose(); // Close the current dialog
        }

        // 2. Start the level showing the GamePanel
        Game.getInstance().restart();
        Game.getInstance().setLevel(levelIndex);
        View.getInstance().getGamePanel().repaintBackground();
        View.getInstance().showGame();
        infoDialog.requestFocusInWindow(); // needed to get user input
        Objects.requireNonNull(infoDialog.getParent(), "The panel does not have a defined parent");
        LevelDialog informationDialog=new LevelDialog("New Game","Try to reach the sugar piece", "Start");
//        panel.showCustomDialog(informationDialog);
    }

    // necessary to avoid re-initialization each time another level is selected
    public void setLevelIndex(int levelIndex) {
        this.levelIndex = levelIndex;
    }
}
