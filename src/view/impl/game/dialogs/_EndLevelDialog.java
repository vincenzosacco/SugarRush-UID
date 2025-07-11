package view.impl.game.dialogs;

import utils.Resources;
import view.impl.game.GameMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

// public here because controller needs to access it
public abstract class _EndLevelDialog extends GameMenu {

    protected final JLabel levelLabel = new JLabel(); // Label to show the current level
    /** Label for the win/lose message*/
    protected final JLabel messageLabel = new JLabel();

    /** Label to show the elapsed time */
    protected final JLabel timerLabel = new JLabel("Time: 0s");


    // only used in WinPanel and LosePanel- keep it Package-Private
    _EndLevelDialog(int currentLevel) {
        super(currentLevel);
    }

    // ---------------------------------------- CONTROLLER RELATED METHODS -------------------------------------------------------

    // Sets the current level and updates the label if already created
    public void updateLevel(int currentLevel) {
        if (levelLabel != null) {
            levelLabel.setText("Level " + currentLevel);
        }
    }

    // Update the time
    public void updateElapsedTime(long seconds) {
        timerLabel.setText("Time: " + seconds + "s");
        timerLabel.revalidate();
        timerLabel.repaint();
    }

    // Dynamically updates the level label with a new level
    public void updateLabels(int level) {
        JLabel levelLabel = (JLabel) ((JPanel) getComponent(0)).getComponent(1);
        levelLabel.setText("Level " + level);
//        applyScalingBasedOnCurrentDimensions();
    }

    // ---------------------------------------- ABSTARCT OVERRIDES  -------------------------------------------------------

    @Override
    protected BufferedImage loadBackgroundImage() {
        return Resources.getBestImage("/imgs/panels/levels/endLevelImage.jpg", getWidth(), getHeight());
    }

    //------------------------------------------ OVERRIDE PARENT ---------------------------------------------------------------------
    @Override
    protected final void buildTopArea() { // Win and Lose share the same top area layout, so is not required to override this
        super.buildTopArea(); // Same as in GameMenu
        // Remove the settings button from the top area
        settingsButton.getParent().remove(settingsButton);
    }


    @Override
    protected void buildCenterArea(){ // Win and Lose share the same center area layout, so is not required to override this
        centerArea.removeAll(); // If existing components, remove them first -> avoid bugs
        centerArea.setLayout(new FlowLayout());
        centerArea.setOpaque(false);

        // BOTH WIN AND LOSE have message and time
        centerArea.add(messageLabel);

        timerLabel.setForeground(Color.BLACK);
        timerLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        centerArea.add(timerLabel);
    }
}