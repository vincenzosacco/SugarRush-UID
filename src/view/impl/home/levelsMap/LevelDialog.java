package view.impl.home.levelsMap;

import utils.Resources;
import view.base.BaseDialog;
import view.impl._common.buttons.CustomButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

/**
 * The custom dialog showed at the beginning of each level and at the dialogs of the last level
 */
public class LevelDialog extends BaseDialog {
    // Button for closing the dialog
    private final CustomButton button;

    public LevelDialog(String title, String text, String buttonText){
        super(); // <-- load bg image
        button = new CustomButton(buttonText, Color.WHITE, new Color(100, 149, 237));
        button.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose(); // Close the dialog
            }
        });

        // --------------------- TOP PANEL ---------------------
        // Title
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(titleLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.PAGE_START);

        // --------------------- CENTER PANEL ---------------------
        // Text
        JLabel textLabel = new JLabel(text, SwingConstants.CENTER);
        textLabel.setForeground(Color.BLACK);
        textLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(textLabel);
        add(centerPanel, BorderLayout.CENTER);

        // --------------------- BOTTOM PANEL ---------------------
        // Button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.add(button);
        add(bottomPanel, BorderLayout.PAGE_END);

        // --- KEY BINDINGS ---
        setupKeyBindings();

        // --------------------- RESIZE LISTENER ---------------------
        //Dynamically resize when the panel changes size
        resizeComponents(textLabel,titleLabel);

    }

    private void resizeComponents(JLabel textLabel,JLabel titleLabel){
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = getWidth();
                int height = getHeight();

                // Title font
                int titleFontSize = Math.max(30, Math.min(width, height) / 10);
                titleLabel.setFont(new Font("Arial", Font.BOLD, titleFontSize));


                // Text font
                int textFontSize = Math.max(18, Math.min(width, height) / 18);
                textLabel.setFont(new Font("Arial", Font.BOLD, textFontSize));

                // Button size
                int size = Math.max(30, Math.min(width, height) / 10);
                button.setPreferredSize(new Dimension(size * 3, size));

                revalidate();
                repaint();
            }
        });
    }

    // Method to set up key bindings
    private void setupKeyBindings() {
        // Get the InputMap for when the component is focused or one of its children is focused
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        // Get the ActionMap to associate keys with actions
        ActionMap actionMap = this.getActionMap();

        // --- Bind ENTER key to Menu buttons ---
        // Create an InputStroke for the ENTER key
        KeyStroke enterKeyStroke = KeyStroke.getKeyStroke("ENTER");
        // Put the KeyStroke and an identifier (e.g., "clickButton") into the InputMap
        inputMap.put(enterKeyStroke, "clickButton");
        // Associate the identifier with an AbstractAction in the ActionMap
        actionMap.put("clickButton", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Programmatically trigger the action listener of the playButton
                button.doClick();
            }
        });
    }

    //------------------------- ABSTRACT OVERRIDE -----------------------------------

    @Override
    protected BufferedImage loadBackgroundImage() {
        return Resources.getBestImage("/imgs/panels/levels/endLevelImage.jpg", getWidth(), getHeight());
    }
}
