package view.menu;

import controller.ControllerObj;
import controller.menu.LevelController;
import model.game.LevelData;
import model.profile.ProfileManager;
import utils.Resources;
import view.ViewComp;
import view.button.CustomLogoButton;
import view.button.RoundCloseButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class LevelPanel extends JPanel implements ViewComp{

    // Background image for the panel
    private BufferedImage backgroundImage;

    // Buttons for closing and starting the level
    private final RoundCloseButton closeButton;
    private final CustomLogoButton playButton;

    // Arrays to store coin icons and corresponding text
    private final JLabel[] iconLabels = new JLabel[3];
    private final JTextArea[] textLabels = new JTextArea[3];

    // Original images for resizing
    private final Image[] originalImages = new Image[3];

    // Constructor takes the level file and its index
    public LevelPanel(InputStream levelFile, int levelIndex){
        // Load level data (coin status and descriptive text)
        LevelData levelData = new LevelData(levelFile);
        Boolean[] coinsCollected = ProfileManager.getLastProfile().getLevelStarsCount(levelIndex);
        String[] textRequest = levelData.getTextRequest();

        // Use BorderLayout and transparency
        setLayout(new BorderLayout());
        setOpaque(false);

        // Load the background image
        try {
            // Gets the resource URL from the classpath.
            URL imageUrl = getClass().getResource("/imgs/panels/levels/level-dialog.jpg"); // Corrected path

            if (imageUrl == null) {
                System.err.println("Error: Image resource not found in classpath: /resources/imgs/panels/levels/level-dialog.jpg");

            } else {
                backgroundImage = ImageIO.read(imageUrl);
            }
        } catch (IOException e) { // Catch IOException specifically for ImageIO.read
            e.printStackTrace();
            System.err.println("Error loading image level-dialog.jpg: " + e.getMessage());
            // Optionally, set a fallback image or handle the error
        }

        LevelController controller=new LevelController(this);

        // Create and configure the close button
        closeButton = new RoundCloseButton();
        closeButton.addActionListener(e -> {
            controller.onClose();
        });

        // Create and configure the play button
        playButton = new CustomLogoButton("play",Color.GREEN.darker());
        playButton.addActionListener(e -> {
            controller.onPlay(levelIndex);
        });

        // --------------------- TOP PANEL ---------------------

        JPanel topPanel = new JPanel(new GridLayout(1, 3)); // 1 row, 3 columns
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Left placeholder (empty panel)
        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        topPanel.add(leftPanel);


        // Centered level label
        JLabel levelLabel = new JLabel("Level " + levelIndex, SwingConstants.CENTER);
        levelLabel.setForeground(Color.BLACK);

        int levelFontSize = Math.min(getWidth() / 15, getHeight() / 15);
        levelFontSize = Math.max(15, levelFontSize);
        levelLabel.setFont(new Font("Arial", Font.BOLD, levelFontSize));

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
            String imgPath = coinsCollected[i] ? "/imgs/icons/star.jpg" : "/imgs/panels/levels/missingStar.jpg";
            try {
                // Use ClassLoader for robustness
                URL coinImageUrl = getClass().getResource(imgPath); // Corrected path

                if (coinImageUrl == null) {
                    System.err.println("Error: Coin image resource not found: " + imgPath);
                    originalImages[i] = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB); // Fallback image
                } else {
                    originalImages[i] = ImageIO.read(coinImageUrl);
                }
            } catch (IOException e) { // Catch IOException specifically for ImageIO.read
                e.printStackTrace();
                originalImages[i] = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB); // Fallback
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
        //Dynamically resize when the panel changes size
        resizeComponents(levelLabel);

        // Force an initial resize event to apply layout immediately
        SwingUtilities.invokeLater(() -> {
            getComponentListeners()[0].componentResized(
                    new ComponentEvent(LevelPanel.this, ComponentEvent.COMPONENT_RESIZED)
            );
        });
    }

    private void resizeComponents(JLabel levelLabel){
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int panelWidth = getWidth();
                int panelHeight = getHeight();

                // Resize level label font
                int levelFontSize = Math.min(getWidth() / 12, getHeight() / 12);
                levelFontSize = Math.max(25, levelFontSize);
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

// ----------------------------------------OVERRIDE METHODS-------------------------------------------------------------

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
            backgroundImage = Resources.getBestImage("/imgs/panels/levels/level-dialog.jpg", width, height);

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