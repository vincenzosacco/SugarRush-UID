package view.menu;

import view.View;
import view.button.NextLevelButton;
import controller.GameLoop;
import model.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class WinPanel extends BaseEndLevelPanel {

    private JLabel winMessageLabel;
    private JLabel timerLabel;
    private NextLevelButton nextLevelButton; // Specific button for victory
    private int stars;    // Number of stars earned in the level
    private int elapsedTime = 0;

    private JLabel starsLabel;   // Label to display the number of stars
    private JLabel coinLabel;   // Label to display the number of coins
    private ImageIcon starIcon;     // Icon for stars
    private ImageIcon coinIcon;     // Icon for coins

    public WinPanel() {
        super(); // Call the base class constructor

        //Initialize WinPanel specific components
        createWinSpecificComponents();
        // Add the specific center panel created by setupCenterPanel()
        add(setupCenterPanel(), BorderLayout.CENTER);
        // Adding the "Next Level" button to the bottom panel of the base class
        super.bottomPanel.add(nextLevelButton);

        stars = Model.getInstance().getGame().getStarCount();

        //For initial resizing
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                applyScalingBasedOnCurrentDimensions();
            }
            @Override
            public void componentShown(ComponentEvent e) {
                // Set the elapsed time when the panel is shown
                elapsedTime = GameLoop.getInstance().getElapsedSeconds();
                setElapsedTime(elapsedTime);
                //We make sure it has focus.
                requestFocusInWindow();
            }
        });

        // An initial resizing is forced to position the components correctly
        SwingUtilities.invokeLater(this::applyScalingBasedOnCurrentDimensions);
    }

    private void createWinSpecificComponents() {
        winMessageLabel = new JLabel("YOU WIN!", SwingConstants.CENTER);
        winMessageLabel.setForeground(Color.GREEN);
        winMessageLabel.setFont(new Font("Arial", Font.BOLD, 48)); //Initial dimenison

        timerLabel=new JLabel("Time: " + elapsedTime +"s",SwingConstants.CENTER);
        timerLabel.setForeground(Color.BLACK);
        timerLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        nextLevelButton = new NextLevelButton();
        nextLevelButton.addActionListener(e -> {
            View.getInstance().getGamePanel().getPauseButton().setEnabled(true);
            this.setVisible(false);
            GameLoop.getInstance().shutdown();
            Model.getInstance().getGame().clearGameMatrix();
            int nextLevel = Model.getInstance().getGame().getCurrLevel() + 1;
            // Handling the case where there are no more levels
            if (nextLevel <= 6) { // Assuming a maximum of 6 levels
                View.getInstance().getGamePanel().endGame();
                View.getInstance().getGamePanel().resetPanelForNewLevel();
                Model.getInstance().getGame().setLevel(nextLevel);
                View.getInstance().showPanel(View.PanelName.GAME.getName());
                this.requestFocusInWindow(); // needed to get user input
                assert this.getParent() != null ;
                JOptionPane.showMessageDialog(this.getParent(),"Try to reach the sugar piece",
                    "New Game",JOptionPane.INFORMATION_MESSAGE);
                GameLoop.getInstance().start();
            } else {
                // You have completed all levels, return to the main menu or show a final message (if levels are unlocked gradually)
                JOptionPane.showMessageDialog(this, "You have completed all the levels! Congratulations! ", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                Model.getInstance().getGame().clearGameMatrix();
                View.getInstance().showPanel(View.PanelName.CUSTOM_TABBED_PANE.getName());
            }

            starIcon = new ImageIcon(getClass().getResource("/imgs/icons/star.png"));
            coinIcon = new ImageIcon(getClass().getResource( "/imgs/icons/coin.png"));

            starsLabel = new JLabel();
            coinLabel = new JLabel();
        });
    }

    @Override
    protected JPanel setupCenterPanel() {
        JPanel centerPanel = new JPanel(new GridBagLayout()); // GridBagLayout for centering
        centerPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(winMessageLabel, gbc);

        gbc.gridy = 1; // Move to next line
        gbc.insets = new Insets(0, 0, 0, 0);
        centerPanel.add(timerLabel, gbc);

        return centerPanel;
    }

    // Update the time
    public void setElapsedTime(int seconds) {
        timerLabel.setText("Time: " + seconds + "s");
        timerLabel.revalidate();
        timerLabel.repaint();
    }

    @Override
    protected void applyScalingBasedOnCurrentDimensions() {
        super.applyScalingBasedOnCurrentDimensions(); // Call the base class method for common buttons

        // Specific scaling for the victory message
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int fontSize = Math.min(panelWidth / 8, panelHeight / 8);
        winMessageLabel.setFont(new Font("Arial", Font.BOLD, Math.max(24, fontSize)));
        winMessageLabel.revalidate();
        winMessageLabel.repaint();

        // Resizing for Next Level button
        int buttonSize = Math.min(panelWidth, panelHeight) / 10;
        buttonSize = Math.max(buttonSize, 30);
        nextLevelButton.setPreferredSize(new Dimension(buttonSize * 2, buttonSize));
        nextLevelButton.revalidate();
        nextLevelButton.repaint();
    }
}
