package view.impl.game.dialogs;

import config.ModelConfig;
import model.game.Game;
import utils.Resources;
import view.impl._common.buttons.CustomRoundLogoButton;
import view.impl._common.panels.CoinCountPanel;

import javax.swing.*;
import java.awt.*;


public class WinDialog extends _EndLevelDialog {

    private final CustomRoundLogoButton nextLevelButton = new CustomRoundLogoButton(
                    "nextLevel", Color.GREEN
                    );;// Specific button for victory

    private final JPanel starsPanel = new JPanel(); //  JPanel to display the number of stars
    private final CoinCountPanel coinCount = new CoinCountPanel();   // Label to display the number of coins

    public WinDialog(int currentLevel) {
        super(currentLevel); // Call the base class constructor
        messageLabel.setText("YOU WIN!");
        messageLabel.setForeground(Color.GREEN);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 48)); //Initial dimenison
    }


    private void setStars(int stars) {
        starsPanel.removeAll();
        int iconSize = 55;
        ImageIcon scaledStarIcon = new ImageIcon(Resources.getBestImage("/imgs/icons/star.jpg", iconSize, iconSize));
        for (int i = 0; i < stars; i++) {
            JLabel star = new JLabel(scaledStarIcon);
            starsPanel.add(star);
        }

        starsPanel.revalidate();
        starsPanel.repaint();
    }


//--------------------------------- PARENT METHODS ---------------------------------------------------------------------------
        @Override
        protected void buildBottomArea() {
            super.buildBottomArea(); // <-- USE a GridLayout with 3 columns
            assert bottomArea.getLayout()!= null;
            assert bottomArea.getLayout() instanceof GridLayout;
            assert ((GridLayout) bottomArea.getLayout()).getColumns() == 3 : "Bottom area should have 3 columns";
            assert ((GridLayout) bottomArea.getLayout()).getRows() == 1 : "Bottom area should have 1 row";

            //--- CENTER ---//
            // Remove the button at col 1
            bottomArea.remove(1);
            // Add a placeholder where the play button was
            bottomArea.add(Box.createHorizontalGlue(), 1);

            //--- RIGHT ---//
            // Remove components at col 2
            bottomArea.remove(2);
            // Add the nextLevelButton at col 2
            int buttonSize = Math.min(getWidth(), getHeight()) / 10;
            buttonSize = Math.max(buttonSize, 30);
            nextLevelButton.setPreferredSize(new Dimension(buttonSize * 2, buttonSize));
            bottomArea.add(nextLevelButton);
        }

        @Override
        protected void buildCenterArea() {
            centerArea.removeAll(); // If existing components, remove them first -> avoid bugs

            centerArea.setLayout(new GridBagLayout()); // GridBagLayout for centering
            centerArea.setOpaque(false);

            // CONSTRAINTS for GridBagLayout
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.CENTER;

            // MESSAGE LABEL
            centerArea.add(messageLabel, gbc);

            // TIMER LABEL
            gbc.gridy = 1;      // Move to next line
            gbc.insets = new Insets(0, 0, 0, 0);
            centerArea.add(timerLabel, gbc);

            // COIN LABEL and STARS PANEL
            gbc.gridy = 2;      // Next row after timerLabel
            coinCount.setTextColor(Color.BLACK);
            centerArea.add(coinCount, gbc);

            // STARS PANEL
            starsPanel.setOpaque(false); // Make it transparent
            starsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Center aligned with gaps
            setStars(Game.getInstance().getStarCount());
            gbc.gridy = 3;      // Next row after coinLabel
            centerArea.add(starsPanel, gbc);
        }

    // --------------------------------- CONTROLLER METHODS ---------------------------------------------------------------------------
    @Override
    public void updateLevel(int currentLevel) {
        super.updateLevel(currentLevel);
        int stars = Game.getInstance().getStarCount();
        // Update the stars based on the current level
        setStars(stars);
        // Update the coin count
        coinCount.updateCoinCount(stars* ModelConfig.COINS_PER_STAR);
    }
}