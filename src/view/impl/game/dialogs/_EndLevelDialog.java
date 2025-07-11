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

    private int currentLevel;

    // only used in WinPanel and LosePanel- keep it Package-Private
    _EndLevelDialog(int currentLevel) {
        super(currentLevel);


//        setupCommonLayoutElements(); // Set layout without center panel
//
//        // Dynamically scale UI elements when resized
//        this.addComponentListener(new ComponentAdapter() {
//            @Override
//            public void componentResized(ComponentEvent e) {
//                applyScalingBasedOnCurrentDimensions();
//            }
//        });
//
//        setupKeyBindings();
//
//        SwingUtilities.invokeLater(this::applyScalingBasedOnCurrentDimensions);
//
//        this.setFocusable(true);
//        this.setVisible(false); // Hidden by default
    }



    // Sets up top and bottom layout areas
    private void setupCommonLayoutElements() {
        // --- TOP PANEL ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Left side: Settings button
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setOpaque(false);
        leftPanel.add(settingsButton);
        topPanel.add(leftPanel, BorderLayout.LINE_START);

        // Center: Level label
        JLabel levelLabel = new JLabel("Level " + currentLevel, SwingConstants.CENTER);
        levelLabel.setForeground(Color.BLACK);
        int levelFontSize = Math.min(getWidth() / 8, getHeight() / 8);
        levelFontSize = Math.max(25, levelFontSize);
        levelLabel.setFont(new Font("Arial", Font.BOLD, levelFontSize));
        topPanel.add(levelLabel, BorderLayout.CENTER);



        // Right side: empty for spacing
        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        topPanel.add(rightPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.PAGE_START);
    }


    // Resize and reposition components based on panel size
//    public void applyScalingBasedOnCurrentDimensions() {
//        if (levelLabel != null) {
//            int levelFontSize = Math.min(getWidth() / 8, getHeight() / 8);
//            levelFontSize = Math.max(25, levelFontSize);
//            levelLabel.setFont(new Font("Arial", Font.BOLD, levelFontSize));
//        }
//
//        // Specific scaling for the defeat message
//        int panelWidth = getWidth();
//        int panelHeight = getHeight();
//        int fontSize = Math.min(panelWidth / 8, panelHeight / 8);
////        messageLabel.setFont(new Font("Arial", Font.BOLD, Math.max(24, fontSize)));
//
//        // Specific scaling for the timer label
//        int fontSizeTimer = Math.min(panelWidth / 15, panelHeight / 15);
//        timerLabel.setFont(new Font("Arial", Font.PLAIN, Math.max(16, fontSizeTimer)));
//
//        revalidate();
//        repaint();
//    }

    // Method to set up key bindings
//    private void setupKeyBindings() {
//        // Get the InputMap for when the component is focused or one of its children is focused
//        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
//        // Get the ActionMap to associate keys with actions
//        ActionMap actionMap = this.getActionMap();
//
//        // --- Bind ESCAPE key to Exit button ---
//        // Create an InputStroke for the ESCAPE key
//        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
//        // Put the KeyStroke and an identifier (e.g., "pressExit") into the InputMap
//        inputMap.put(escapeKeyStroke, "pressExit");
//        // Associate the identifier with an AbstractAction in the ActionMap
//        actionMap.put("pressExit", new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // Programmatically trigger the action listener of the exitButton
//                exitButton.doClick();
//            }
//        });
//    }

    // ---------------------------------------- CONTROLLER RELATED METHODS -------------------------------------------------------

    // Sets the current level and updates the label if already created
    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
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
    protected void buildCenterArea(){
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