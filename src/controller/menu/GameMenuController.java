package controller.menu;

import controller.ControllerObj;
import controller.GameLoop;
import model.Model;
import view.View;
import view.ViewComp;
import view.menu.GameMenuPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameMenuController implements ControllerObj {

    private GameMenuPanel panel;

    public GameMenuController(GameMenuPanel panel) {
        this.panel=panel;
    }
    /**
     * Define what happens when continue button is pressed
     */
    public void onPlay(){
        panel.open=false;
        panel.setVisible(false);
        GameLoop.getInstance().start();
        View.getInstance().getGamePanel().getPauseButton().setEnabled(true);
        View.getInstance().getGamePanel().requestFocusInWindow();
    }
    public void onRestart(){
        int levelToRestart = Model.getInstance().getGame().getCurrLevel();
        panel.open=false;
        panel.setVisible(false);
        //Start the level showing the GamePanel
        View.getInstance().getGamePanel().endGame();
        Model.getInstance().getGame().clearGameMatrix();
        View.getInstance().getGamePanel().resetPanelForNewLevel();
        Model.getInstance().getGame().setLevel(levelToRestart);
        View.getInstance().showPanel(View.PanelName.GAME.getName());
        View.getInstance().getGamePanel().getPauseButton().setEnabled(true);
        GameLoop.getInstance().start();
    }

    public void onExit(){
        panel.open=false;
        panel.setVisible(false);
        GameLoop.getInstance().shutdown();
        Model.getInstance().getGame().clearGameMatrix();
        View.getInstance().showPanel(View.PanelName.CUSTOM_TABBED_PANE.getName());
    }

    public void onSettings(){
        panel.open=false;
        View.getInstance().showPanel(View.PanelName.SETTINGS.getName());
    }

}
