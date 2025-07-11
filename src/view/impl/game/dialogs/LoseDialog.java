package view.impl.game.dialogs;

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

//    public LoseDialog(int) {
//        super(); // Call the base class constructor
//        createLoseSpecificComponents();
//        add(setupCenterPanel(), BorderLayout.CENTER);
//
//        setupKeyBindings();
//
//        // An initial resizing is forced to position the components correctly
//        SwingUtilities.invokeLater(this::applyScalingBasedOnCurrentDimensions);
//    }

    private void createLoseSpecificComponents() {
    }

    // Method to set up key bindings
//    private void setupKeyBindings() {
//        // Get the InputMap for when the component is focused or one of its children is focused
//        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
//        // Get the ActionMap to associate keys with actions
//        ActionMap actionMap = this.getActionMap();
//
//        // --- Bind ENTER key to Restart buttons ---
//        // Create an InputStroke for the ENTER key
//        KeyStroke enterKeyStroke = KeyStroke.getKeyStroke("ENTER");
//        // Put the KeyStroke and an identifier (e.g., "restart") into the InputMap
//        inputMap.put(enterKeyStroke, "restart");
//        // Associate the identifier with an AbstractAction in the ActionMap
//        actionMap.put("restart", new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // Programmatically trigger the action listener of the restartButton
//                restartButton.doClick();
//            }
//        });
//    }


    protected JPanel setupCenterPanel() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 10, 0); // Space below the message
        centerPanel.add(messageLabel, gbc);

        gbc.gridy = 1; // Move to next line
        gbc.insets = new Insets(0, 0, 0, 0);
        centerPanel.add(timerLabel, gbc);

        return centerPanel;
    }
}