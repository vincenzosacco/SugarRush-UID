package view.menu;

import controller.ControllerObj;
import controller.GameLoop;
import model.Model;
import model.game.LevelData;
import view.View;
import view.ViewComp;
import view.button.RoundCloseButton;
import view.button.RoundPlayButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class LevelPanel extends JPanel implements ViewComp {

    // Background image for the panel
    private Image backgroundImage;

    // Buttons for closing and starting the level
    private final RoundCloseButton closeButton;
    private final RoundPlayButton playButton;

    // Arrays to store coin icons and corresponding text
    private final JLabel[] iconLabels = new JLabel[3];
    private final JTextArea[] textLabels = new JTextArea[3];

    // Original images for resizing
    private final Image[] originalImages = new Image[3];

    // Constructor takes the level file and its index
    public LevelPanel(InputStream levelFile, int levelIndex){
        // Load level data (coin status and descriptive text)
        LevelData levelData = new LevelData(levelFile);
        boolean[] coinsCollected = levelData.getCoinsCollected();
        String[] textRequest = levelData.getTextRequest();

        // Use BorderLayout and transparency
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true)); // Rounded border

        // Load the background image
        try {
            // Gets the resource URL from the classpath.
            URL imageUrl = getClass().getResource("/backgroundLevelDialog.jpg"); // Corrected path

            if (imageUrl == null) {
                System.err.println("Error: Image resource not found in classpath: /resources/backgroundLevelDialog.jpg");

            } else {
                backgroundImage = ImageIO.read(imageUrl);
            }
        } catch (IOException e) { // Catch IOException specifically for ImageIO.read
            e.printStackTrace();
            System.err.println("Error loading image backgroundLevelDialog.jpg: " + e.getMessage());
            // Optionally, set a fallback image or handle the error
        }

        // Create and configure the close button
        closeButton = new RoundCloseButton();
        closeButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(LevelPanel.this);
            if (window != null) {
                window.dispose(); // Close the dialog
            }
        });

        // Create and configure the play button
        playButton = new RoundPlayButton();
        playButton.addActionListener(e -> {

            // 1. Close the LevelPanel dialog after starting the game
            Window window = SwingUtilities.getWindowAncestor(LevelPanel.this);
            if (window != null) {
                window.dispose(); // Close the current dialog
            }

            // 2. Start the level showing the GamePanel
            Model.getInstance().getGame().setLevel(levelIndex);
            View.getInstance().getGamePanel().resetPanelForNewLevel();
            View.getInstance().showPanel(View.PanelName.GAME.getName());
            GameLoop.getInstance().start();
        });

        // --------------------- TOP PANEL ---------------------

        JPanel topPanel = new JPanel(new GridLayout(1, 3)); // 1 row, 3 columns
        topPanel.setOpaque(false);

        // Left placeholder (empty panel)
        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        topPanel.add(leftPanel);

        // Centered level label
        JLabel levelLabel = new JLabel("Level " + levelIndex, SwingConstants.CENTER);
        levelLabel.setForeground(Color.BLACK);
        topPanel.add(levelLabel);

        // Right panel with close button
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(closeButton);
        topPanel.add(rightPanel);

        add(topPanel, BorderLayout.PAGE_START);

        // --------------------- CENTER PANEL ---------------------

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS)); // Vertical layout

        // For each coin (3 total), create a row with an icon and a description
        for (int i = 0; i < 3; i++) {
            JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            rowPanel.setOpaque(false);

            // Choose correct image based on coin collected or not
            String imgPath = coinsCollected[i] ? "coin.jpg" : "missingCoin.jpg";
            try {
                // Use ClassLoader for robustness
                URL coinImageUrl = getClass().getResource("/"+imgPath); // Corrected path

                if (coinImageUrl == null) {
                    System.err.println("Error: Coin image resource not found: " + imgPath);
                    originalImages[i] = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB); // Fallback image
                } else {
                    originalImages[i] = ImageIO.read(coinImageUrl);
                }
            } catch (IOException e) { // Catch IOException specifically for ImageIO.read
                e.printStackTrace();
                originalImages[i] = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB); // Fallback image
            }

            // Scaled image icon
            ImageIcon icon = new ImageIcon(originalImages[i].getScaledInstance(40, 40, Image.SCALE_SMOOTH));
            iconLabels[i] = new JLabel(icon);
            rowPanel.add(iconLabels[i]);

            // Text description for each coin objective
            JTextArea textArea = new JTextArea(textRequest[i]);
            textArea.setFont(new Font("Arial", Font.PLAIN, 14));
            textArea.setForeground(coinsCollected[i] ? Color.GREEN : Color.RED);
            textArea.setBackground(new Color(255, 255, 255, 220)); // Semi-transparent white
            // Enables automatic line wrapping when text exceeds the width of the text area.
            textArea.setLineWrap(true);
            // Ensures lines wrap at word boundaries (not in the middle of a word), for better readability.
            textArea.setWrapStyleWord(true);
            textArea.setEditable(false);
            // Prevents the text area from receiving focus (e.g., when tabbing through components).
            textArea.setFocusable(false);
            // Makes the background of the text area visible (not transparent).
            textArea.setOpaque(true);
            // Adds empty padding around the text (top, left, bottom, right) for visual spacing.
            textArea.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));

            textLabels[i] = textArea;
            rowPanel.add(textArea);
            centerPanel.add(rowPanel);
        }

        add(centerPanel, BorderLayout.CENTER);

        // --------------------- BOTTOM PANEL ---------------------

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.add(playButton);
        add(bottomPanel, BorderLayout.PAGE_END);

        // --- KEY BINDINGS ---
        setupKeyBindings();

        // --------------------- RESIZE LISTENER ---------------------

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int panelWidth = getWidth();
                int panelHeight = getHeight();

                // Resize level label font
                int dimensionFontSize = Math.min(panelWidth / 15, panelHeight / 15);
                int levelFontSize = Math.max(12, dimensionFontSize);
                levelLabel.setFont(new Font("Arial", Font.BOLD, levelFontSize));
                levelLabel.revalidate();
                levelLabel.repaint();

                // Resize coin icons and text areas
                int dimIconSize = Math.min(panelHeight / 6, panelWidth / 6);
                int dimFontSize = Math.min(panelHeight / 20, panelWidth / 20);
                int iconSize = Math.max(30, dimIconSize);
                int fontSize = Math.max(12, dimFontSize);

                for (int i = 0; i < 3; i++) {
                    Image scaled = originalImages[i].getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
                    iconLabels[i].setIcon(new ImageIcon(scaled));
                    textLabels[i].setFont(new Font("Arial", Font.PLAIN, fontSize));

                    // Resize the text area to fit nicely next to the icon
                    int textWidth = (panelWidth - iconSize) * 9 / 10;
                    textLabels[i].setPreferredSize(new Dimension(textWidth, fontSize * 4));
                    textLabels[i].revalidate();
                    textLabels[i].repaint();
                }

                // Resize close button
                int size = Math.min(getWidth(), getHeight()) / 10;
                size = Math.max(size, 30);
                closeButton.setPreferredSize(new Dimension(size, size));
                closeButton.revalidate();
                closeButton.repaint();

                // Resize play button
                playButton.setPreferredSize(new Dimension(size * 2, size));
                playButton.revalidate();
                playButton.repaint();

                revalidate();
                repaint();
            }
        });

        // Force an initial resize event to apply layout immediately
        SwingUtilities.invokeLater(() -> {
            getComponentListeners()[0].componentResized(
                    new ComponentEvent(LevelPanel.this, ComponentEvent.COMPONENT_RESIZED)
            );
        });
    }

    // Method to set up key bindings
    private void setupKeyBindings() {
        // Get the InputMap for when the component is focused or one of its children is focused
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        // Get the ActionMap to associate keys with actions
        ActionMap actionMap = this.getActionMap();

        // --- Bind ENTER key to Play button ---
        // Create an InputStroke for the ENTER key
        KeyStroke enterKeyStroke = KeyStroke.getKeyStroke("ENTER");
        // Put the KeyStroke and an identifier (e.g., "pressPlay") into the InputMap
        inputMap.put(enterKeyStroke, "pressPlay");
        // Associate the identifier with an AbstractAction in the ActionMap
        actionMap.put("pressPlay", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Programmatically trigger the action listener of the playButton
                playButton.doClick();
            }
        });

        // --- Bind ESCAPE key to Close button ---
        // Create an InputStroke for the ESCAPE key
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
        // Put the KeyStroke and an identifier (e.g., "pressClose") into the InputMap
        inputMap.put(escapeKeyStroke, "pressClose");
        // Associate the identifier with an AbstractAction in the ActionMap
        actionMap.put("pressClose", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Programmatically trigger the action listener of the closeButton
                closeButton.doClick();
            }
        });
    }

    // Custom painting to render the rounded background and border
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int arc = 30;

        // Rounded rectangle clipping for a smooth shape
        Shape clip = new RoundRectangle2D.Float(0, 0, width, height, arc, arc);
        g2.setClip(clip);

        // Draw background image or fallback to white
        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, width, height, this);
        } else {
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, width, height);
        }

        // Draw rounded border
        g2.setClip(null); // Remove clipping for border
        g2.setStroke(new BasicStroke(10f));
        g2.setColor(Color.BLACK);
        g2.draw(clip);

        g2.dispose();

        // Paint child components
        super.paintComponent(g);
    }

    @Override
    public void bindController(ControllerObj controller) {

    }

}