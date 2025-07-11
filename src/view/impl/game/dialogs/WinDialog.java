package view.impl.game.dialogs;

import view.impl._common.buttons.CustomRoundLogoButton;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;


public class WinDialog extends _EndLevelDialog {

    private CustomRoundLogoButton nextLevelButton ;// Specific button for victory

    private JPanel starsPanel; //  JPanel to display the number of stars
    private JLabel coinLabel;   // Label to display the number of coins
    private final ImageIcon starIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/imgs/icons/star.jpg")));     // Icon for stars
    private final ImageIcon coinIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/imgs/icons/coinsImmage.png")));   // Icon for coins

    public WinDialog(int currentLevel) {
        super(currentLevel); // Call the base class constructor
    }

//    private void createSpecificComponents() {
//        messageLabel = new JLabel("YOU WIN!", SwingConstants.CENTER);
//        messageLabel.setForeground(Color.GREEN);
//        messageLabel.setFont(new Font("Arial", Font.BOLD, 48)); //Initial dimenison
//
//        // Load icons for stars and coins
//        starsPanel = new JPanel();
//        starsPanel.setOpaque(false);
//        coinLabel = new JLabel();
//
//        // Set a larger font for the label
//        coinLabel.setFont(new Font("Arial", Font.BOLD, 25));
//    }

    @Override
    protected void bindControllers(){
    }

//    @Override
//    protected JPanel setupCenterPanel() {
//        JPanel centerPanel = new JPanel(new GridBagLayout()); // GridBagLayout for centering
//        centerPanel.setOpaque(false);
//
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.anchor = GridBagConstraints.CENTER;
//        centerPanel.add(messageLabel, gbc);
//
//        gbc.gridy = 1;      // Move to next line
//        gbc.insets = new Insets(0, 0, 0, 0);
//        centerPanel.add(timerLabel, gbc);
//
//        gbc.gridy = 2;      // Next row after timerLabel
//        centerPanel.add(coinLabel, gbc);
//
//        gbc.gridy = 3;      // Next row after coinLabel
//        centerPanel.add(starsPanel, gbc);
//
//        return centerPanel;
//    }


    // Update the number of Coins
    public void setCoins(int coins) {
        // Scale the icon to a larger size
        Image img = coinIcon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
        coinLabel.setIcon(new ImageIcon(img));
        coinLabel.setText(""+ coins); // Set the number of coins earned
        coinLabel.revalidate();
        coinLabel.setIcon(new ImageIcon(img));
        coinLabel.repaint();
    }

    public void setStars(int stars) {
        starsPanel.removeAll();
        int iconSize = 55;
        Image scaledImg = starIcon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
        ImageIcon scaledStarIcon = new ImageIcon(scaledImg);
        for (int i = 0; i < stars; i++) {
            JLabel star = new JLabel(scaledStarIcon);
            starsPanel.add(star);
        }
        starsPanel.revalidate();
        starsPanel.repaint();
    }

    //------------------------ OVERRIDE PARENT -----------------------------------------------

    //        super.paintComponent(g);
    //    }

        //--------------------------------- PARENT METHODS ---------------------------------------------------------------------------
//        @Override
//        protected void buildBottomArea() {
////            super.buildBottomArea(); keep commented to override parent behavior
////            nextLevelButton = new CustomRoundLogoButton(
////                    "nextLevel", Color.GREEN
////            );
////
////            bottomArea.setLayout(new BoxLayout(bottomArea, BoxLayout.LINE_AXIS));
////            bottomArea.add(nextLevelButton);
//
//        }

//    @Override
//    public void applyScalingBasedOnCurrentDimensions() {
//        super.applyScalingBasedOnCurrentDimensions(); // Call the base class method for common buttons
//
//        // Resizing for Next Level button
//        int buttonSize = Math.min(getWidth(), getHeight()) / 10;
//        buttonSize = Math.max(buttonSize, 30);
//        nextLevelButton.setPreferredSize(new Dimension(buttonSize * 2, buttonSize));
//        nextLevelButton.revalidate();
//        nextLevelButton.repaint();
//    }

}