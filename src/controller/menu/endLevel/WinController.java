package controller.menu.endLevel;

import controller.GameLoop;
import model.Model;
import view.View;
import view.menu.CustomDialog;
import view.menu.endLevel.WinPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class WinController implements ActionListener {
    private final WinPanel panel;
    public WinController(WinPanel panel) {
        this.panel=panel;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        View.getInstance().getGamePanel().getPauseButton().setEnabled(true);
        GameLoop.getInstance().shutdown();
        Model.getInstance().getGame().clearGameMatrix();
        int nextLevel = Model.getInstance().getGame().getCurrLevel() + 1;
        // Handling the case where there are no more levels
        if (nextLevel <= 6) { // Assuming a maximum of 6 levels
            View.getInstance().getGamePanel().endGame();
            View.getInstance().getGamePanel().repaintBackground();
            Model.getInstance().getGame().setLevel(nextLevel);
            View.getInstance().showPanel(View.PanelName.GAME.getName());
            panel.setVisible(false);
            panel.requestFocusInWindow(); // needed to get user input
            Objects.requireNonNull(panel.getParent(), "The panel does not have a parent");
            CustomDialog informationDialog=new CustomDialog("New Game","Try to reach the sugar piece", "Start");
            panel.showCustomDialog(informationDialog);
            GameLoop.getInstance().start();
        } else {
            // You have completed all levels, show a final message and return to the main menu
            CustomDialog customDialog=new CustomDialog(" Congratulations! ","You have completed all the levels!", "Back to menu'");
            panel.showCustomDialog(customDialog);
            Model.getInstance().getGame().clearGameMatrix();
            //Update the StartMenuPanel button graphics
            View.getInstance().getCustomTabbedPane().getStartMenuPanel().refreshLevelButtons();
            View.getInstance().showPanel(View.PanelName.CUSTOM_TABBED_PANE.getName());
            panel.setVisible(false);
        }
    }
}
