package controller.menu.endLevel;

import controller.GameLoop;
import model.Model;
import view.View;
import view.menu.endLevel.WinPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class WinController implements ActionListener {
    private WinPanel panel;
    public WinController(WinPanel panel) {
        this.panel=panel;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        View.getInstance().getGamePanel().getPauseButton().setEnabled(true);
        panel.setVisible(false);
        GameLoop.getInstance().shutdown();
        Model.getInstance().getGame().clearGameMatrix();
        int nextLevel = Model.getInstance().getGame().getCurrLevel() + 1;
        // Handling the case where there are no more levels
        if (nextLevel <= 6) { // Assuming a maximum of 6 levels
            View.getInstance().getGamePanel().endGame();
            View.getInstance().getGamePanel().repaintBackground();
            Model.getInstance().getGame().setLevel(nextLevel);
            View.getInstance().showPanel(View.PanelName.GAME.getName());
            panel.requestFocusInWindow(); // needed to get user input
            Objects.requireNonNull(panel.getParent(), "The panel does not have a parent");
            JOptionPane.showMessageDialog(panel.getParent(),"Try to reach the sugar piece",
                    "New Game",JOptionPane.INFORMATION_MESSAGE);
            GameLoop.getInstance().start();
        } else {
            // You have completed all levels, return to the main menu or show a final message (if levels are unlocked gradually)
            JOptionPane.showMessageDialog(panel, "You have completed all the levels! Congratulations! ", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            Model.getInstance().getGame().clearGameMatrix();
            View.getInstance().showPanel(View.PanelName.CUSTOM_TABBED_PANE.getName());
        }
    }
}
