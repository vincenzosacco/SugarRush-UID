package view.impl.game.dialogs;

import controller.game.dialogs.LoseDialogController;

import javax.swing.*;
import java.awt.*;

public class LoseDialog extends _EndLevelDialog {
    public LoseDialog(int currentLevel) {
        super(currentLevel);

        // override the messageLabel to show "GAME OVER"
        messageLabel.setText("GAME OVER");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setForeground(new Color(178, 34, 34)); // Firebrick
        messageLabel.setFont(new Font("Arial", Font.BOLD, 48)); // Initial dimension
    }



    @Override
    protected void buildBottomArea() {
        super.buildBottomArea(); // i want same buttons as in _EndLevelDialog
        // Remove the playButton from the bottom area
        playButton.getParent().remove(playButton);
        // Add placeholder where the play button was
        bottomArea.add(Box.createHorizontalGlue(), 1);
    }

    @Override
    protected void bindControllers(){
        LoseDialogController controller = new LoseDialogController(this);
        restartButton.addActionListener(controller::onRestart);
        exitButton.addActionListener(controller::onExit);
    }
}