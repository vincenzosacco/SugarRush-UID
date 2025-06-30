package view.menu.endLevel;

import controller.GameLoop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class LosePanel extends BaseEndLevelPanel {

    private JLabel loseMessageLabel;
    private JLabel timerLabel;

    private int elapsedTime = 0;

    public LosePanel() {
        super(); // Call the base class constructor
        createLoseSpecificComponents();
        add(setupCenterPanel(), BorderLayout.CENTER);

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

        setupKeyBindings();

        // An initial resizing is forced to position the components correctly
        SwingUtilities.invokeLater(this::applyScalingBasedOnCurrentDimensions);
    }

    private void createLoseSpecificComponents() {
        loseMessageLabel = new JLabel("GAME OVER", SwingConstants.CENTER);
        loseMessageLabel.setForeground(Color.RED);
        loseMessageLabel.setFont(new Font("Arial", Font.BOLD, 48)); // Initial dimension

        timerLabel=new JLabel("Time: " + elapsedTime +"s",SwingConstants.CENTER);
        timerLabel.setForeground(Color.BLACK);
        timerLabel.setFont(new Font("Arial", Font.PLAIN, 24));
    }

    // Method to set up key bindings
    private void setupKeyBindings() {
        // Get the InputMap for when the component is focused or one of its children is focused
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        // Get the ActionMap to associate keys with actions
        ActionMap actionMap = this.getActionMap();

        // --- Bind ENTER key to Restart button ---
        // Create an InputStroke for the ENTER key
        KeyStroke enterKeyStroke = KeyStroke.getKeyStroke("ENTER");
        // Put the KeyStroke and an identifier (e.g., "restart") into the InputMap
        inputMap.put(enterKeyStroke, "restart");
        // Associate the identifier with an AbstractAction in the ActionMap
        actionMap.put("restart", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Programmatically trigger the action listener of the restartButton
                restartButton.doClick();
            }
        });
    }

    // Update the time
    public void setElapsedTime(int seconds) {
        timerLabel.setText("Time: " + seconds + "s");
        timerLabel.revalidate();
        timerLabel.repaint();
    }

    @Override
    protected JPanel setupCenterPanel() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 10, 0); // Space below the message
        centerPanel.add(loseMessageLabel, gbc);

        gbc.gridy = 1; // Move to next line
        gbc.insets = new Insets(0, 0, 0, 0);
        centerPanel.add(timerLabel, gbc);

        return centerPanel;
    }

    @Override
    protected void applyScalingBasedOnCurrentDimensions() {
        super.applyScalingBasedOnCurrentDimensions(); // Call the base class method

        // Specific scaling for the defeat message
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int fontSize = Math.min(panelWidth / 8, panelHeight / 8);
        loseMessageLabel.setFont(new Font("Arial", Font.BOLD, Math.max(24, fontSize)));
        loseMessageLabel.revalidate();
        loseMessageLabel.repaint();

        // Specific scaling for the timer label
        int fontSizeTimer = Math.min(panelWidth / 15, panelHeight / 15);
        timerLabel.setFont(new Font("Arial", Font.PLAIN, Math.max(16, fontSizeTimer)));
        timerLabel.revalidate();
        timerLabel.repaint();
    }
}