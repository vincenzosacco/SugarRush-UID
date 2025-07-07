package view.menu;

import controller.ControllerObj;
import utils.Resources;
import view.ViewComp;
import view.button.CustomButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * The custom dialog showed at the beginning of each level and at the end of the last level
 */
public class CustomDialog extends JPanel implements ViewComp {
    // Background image for the panel
    private BufferedImage backgroundImage;

    // Button for closing the dialog
    private final CustomButton button;

    public CustomDialog(String title, String text,String buttonText){

        // Use BorderLayout and transparency
        setLayout(new BorderLayout());
        setOpaque(false);

        try {
            // Gets the resource URL from the classpath.
            URL imageUrl = getClass().getResource("/imgs/panels/levels/backgroundCustomDialog.jpg"); // Path of the correct image

            if (imageUrl == null) {
                System.err.println("Error: Image resource not found in classpath: /resources/imgs/panels/levels/backgroundCustomDialog.jpg");

            } else {
                backgroundImage = ImageIO.read(imageUrl);
            }
        } catch (IOException e) { // Catch IOException specifically for ImageIO.read
            e.printStackTrace();
            System.err.println("Error loading image backgroundCustomDialog.jpg: " + e.getMessage());
        }

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

        // --- Bind ENTER key to Menu button ---
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

    private int lastWidth = 0;
    private int lastHeight = 0;
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int arc = 30;

        // Rounded rectangle clipping for a smooth shape
        RoundRectangle2D panelShape = new RoundRectangle2D.Float(0, 0, width, height, arc, arc);
        // Save the original clip of the graphic context
        Shape originalClip = g2.getClip();
        g2.setClip(panelShape);

        // Draw background image if it is null or dimensions changed
        if (backgroundImage == null || width != lastWidth || height != lastHeight) {
            lastWidth = width;
            lastHeight = height;
            // Get the background image
            backgroundImage = Resources.getBestImage("/imgs/panels/levels/backgroundCustomDialog.jpg", width, height);

        } else {
            g2.drawImage(backgroundImage, 0, 0, width, height, this);
        }

        // Draw rounded border
        //Restore the original clip BEFORE drawing the border and child components
        //to prevent the edge and child components from being cut by the rounded clip
        g2.setClip(originalClip);

        float borderWidth = 2.0f;
        g2.setStroke(new BasicStroke(borderWidth));
        g2.setColor(Color.BLACK);

        // Draw the border slightly indented to keep it within the bounds of the panel.
        RoundRectangle2D borderOutline = new RoundRectangle2D.Float(
                borderWidth / 2, borderWidth / 2,
                width - borderWidth, height - borderWidth,
                arc, arc
        );
        g2.draw(borderOutline);

        g2.dispose();

        // Paint child components
        super.paintComponent(g);
    }

    @Override
    public void bindController(ControllerObj controller) {
    }
}
