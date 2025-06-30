package view.menu.endLevel;

import controller.GameLoop;
import controller.menu.endLevel.WinController;
import model.Model;
import view.button.NextLevelButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Objects;

public class WinPanel extends BaseEndLevelPanel {

    private JLabel winMessageLabel;
    private JLabel timerLabel;
    private NextLevelButton nextLevelButton; // Specific button for victory
    private int stars;    // Number of stars earned in the level
    private int elapsedTime = 0;

    private JPanel starsPanel; //  JPanel to display the number of stars
    private JLabel coinLabel;   // Label to display the number of coins
    private final ImageIcon starIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/imgs/icons/star.jpg")));     // Icon for stars
    private final ImageIcon coinIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/imgs/icons/coinsImmage.png")));     // Icon for coins

    public WinPanel() {
        super(); // Call the base class constructor

        //Initialize WinPanel specific components
        createWinSpecificComponents();
        // Add the specific center panel created by setupCenterPanel()
        add(setupCenterPanel(), BorderLayout.CENTER);
        // Adding the "Next Level" button to the bottom panel of the base class
        super.bottomPanel.add(nextLevelButton);

        //For initial resizing
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                applyScalingBasedOnCurrentDimensions();
            }
            @Override
            public void componentShown(ComponentEvent e) {
                stars = Model.getInstance().getGame().getStarCount();
                setCoins(stars); // Set the number of stars earned in the level

                setStars(stars); // Set the number of stars earned in the level

                // Set the elapsed time when the panel is shown
                elapsedTime = GameLoop.getInstance().getElapsedSeconds();
                setElapsedTime(elapsedTime);
                //We make sure it has focus.

                requestFocusInWindow();
            }
        });

        setupKeyBindings();

        // An initial resizing is forced to position the components correctly
        SwingUtilities.invokeLater(this::applyScalingBasedOnCurrentDimensions);
    }

    private void createWinSpecificComponents() {
        winMessageLabel = new JLabel("YOU WIN!", SwingConstants.CENTER);
        winMessageLabel.setForeground(Color.GREEN);
        winMessageLabel.setFont(new Font("Arial", Font.BOLD, 48)); //Initial dimenison

        timerLabel = new JLabel("Time: " + elapsedTime +"s",SwingConstants.CENTER);
        timerLabel.setForeground(Color.BLACK);
        timerLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        // Load icons for stars and coins
        starsPanel = new JPanel();
        starsPanel.setOpaque(false);
        coinLabel = new JLabel();

        // Set a larger font for the label
        coinLabel.setFont(new Font("Arial", Font.BOLD, 25));

        nextLevelButton = new NextLevelButton();
        nextLevelButton.addActionListener(new WinController(this));

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

        gbc.gridy = 1;      // Move to next line
        gbc.insets = new Insets(0, 0, 0, 0);
        centerPanel.add(timerLabel, gbc);

        gbc.gridy = 2;      // Next row after timerLabel
        centerPanel.add(coinLabel, gbc);

        gbc.gridy = 3;      // Next row after coinLabel
        centerPanel.add(starsPanel, gbc);

        return centerPanel;
    }

    // Update the time
    public void setElapsedTime(int seconds) {
        timerLabel.setText("Time: " + seconds + "s");
        timerLabel.revalidate();
        timerLabel.repaint();
    }

    // Update the number of Coins
    public void setCoins(int stars) {
        this.stars = stars;
        int coins = stars * 10;         //10 coins per star

        // Scale the icon to a larger size
        Image img = coinIcon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
        coinLabel.setIcon(new ImageIcon(img));
        coinLabel.setText(""+ coins); // Set the number of coins earned
        coinLabel.revalidate();
        coinLabel.setIcon(new ImageIcon(img));
        coinLabel.repaint();
    }

    public void setStars(int stars) {
        this.stars = stars;
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

    // Method to set up key bindings
    private void setupKeyBindings() {
        // Get the InputMap for when the component is focused or one of its children is focused
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        // Get the ActionMap to associate keys with actions
        ActionMap actionMap = this.getActionMap();

        // --- Bind ENTER key to Next button ---
        // Create an InputStroke for the ENTER key
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ENTER");
        // Put the KeyStroke and an identifier (e.g., "pressNext") into the InputMap
        inputMap.put(escapeKeyStroke, "pressNext");
        // Associate the identifier with an AbstractAction in the ActionMap
        actionMap.put("pressNext", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Programmatically trigger the action listener of the nextButton
                nextLevelButton.doClick();
            }
        });
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

        // Specific scaling for the timer label
        int fontSizeTimer = Math.min(panelWidth / 15, panelHeight / 15);
        timerLabel.setFont(new Font("Arial", Font.PLAIN, Math.max(16, fontSizeTimer)));
        timerLabel.revalidate();
        timerLabel.repaint();

        // Resizing for Next Level button
        int buttonSize = Math.min(panelWidth, panelHeight) / 10;
        buttonSize = Math.max(buttonSize, 30);
        nextLevelButton.setPreferredSize(new Dimension(buttonSize * 2, buttonSize));
        nextLevelButton.revalidate();
        nextLevelButton.repaint();
    }

}
