package view.impl.home.settings;

import utils.Resources;
import view.impl._common.buttons.RoundCloseButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class GameControlsPanel extends JPanel {
    private Image gameControlsImage;

    private final RoundCloseButton closeButton;
    private final JLabel gameLabel;

    private final JLabel[] iconLabels = new JLabel[5];
    private final JTextArea[] textAreas = new JTextArea[5];
    private final Image[] originalImages = new Image[5];

    private int currentLineIndex = 0;

    private int lastWidth = 0;
    private int lastHeight = 0;

    public GameControlsPanel(Dimension desiredSize) {
        setSize(desiredSize); // Set the same size as the settings panel
        setOpaque(false);

        setLayout(new BorderLayout());

        // Load all images
        Image upArrowOriginal = loadImages("/imgs/panels/settings/upArrow.jpg");
        Image downArrowOriginal = loadImages("/imgs/panels/settings/downArrow.jpg");
        Image leftArrowOriginal = loadImages("/imgs/panels/settings/leftArrow.jpg");
        Image rightArrowOriginal = loadImages("/imgs/panels/settings/rightArrow.jpg");
        Image escOriginal = loadImages("/imgs/panels/settings/esc.jpg");

        gameControlsImage = Resources.getBestImage("/imgs/panels/settings/gameControlsImage.jpg", desiredSize.width, desiredSize.height);

        closeButton = new RoundCloseButton();
        closeButton.addActionListener(e -> {
            // Find the parent window (which should be the JDialog)
            Window window = SwingUtilities.getWindowAncestor(GameControlsPanel.this);
            if (window instanceof JDialog) {
                window.dispose(); // Close and release resources of the dialog
            } else {
                // Fallback for unexpected parent or if this panel is used differently
                setVisible(false);
            }
        });

        // --------------------- TOP PANEL ---------------------

        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; // Components fill their display area
        gbc.gridy = 0; // All components are in the first row

        // Left placeholder (empty panel)
        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        gbc.weightx = 0.1; // Small horizontal weight, takes up 10% of available space
        gbc.gridx = 0;
        topPanel.add(leftPanel, gbc);

        // Centered level label
        gameLabel = new JLabel("Game controls", SwingConstants.CENTER);
        gameLabel.setForeground(Color.BLACK);
        gameLabel.setFont(new Font("Arial", Font.BOLD, 25)); // Initial font size, will be adjusted
        gbc.weightx = 0.8; // High horizontal weight, takes up 80% of available space
        gbc.gridx = 1;
        topPanel.add(gameLabel, gbc);

        // Right panel with close buttons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(closeButton);
        gbc.weightx = 0.1; // Small horizontal weight, takes up 10% of available space
        gbc.gridx = 2;
        topPanel.add(rightPanel, gbc);

        add(topPanel, BorderLayout.PAGE_START);

        // --------------------- CENTER PANEL ---------------------

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS)); // Vertical layout

        // Use addLine and store references
        centerPanel.add(addLine("Move the creature in up direction;", upArrowOriginal));
        centerPanel.add(addLine("Move the creature in down direction;", downArrowOriginal));
        centerPanel.add(addLine("Move the creature in right direction;", rightArrowOriginal));
        centerPanel.add(addLine("Move the creature in left direction;", leftArrowOriginal));
        centerPanel.add(addLine("Open the menu level", escOriginal));

        add(centerPanel, BorderLayout.CENTER);

        // --------------------- RESIZE LISTENER ---------------------

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int panelWidth = getWidth();
                int panelHeight = getHeight();

                if (panelWidth > 0 && panelHeight > 0 && (panelWidth != lastWidth || panelHeight != lastHeight)) {
                    gameControlsImage = Resources.getBestImage("/imgs/panels/settings/gameControlsImage.jpg", panelWidth, panelHeight);
                    lastWidth = panelWidth;
                    lastHeight = panelHeight;
                }

                // Resize game label font
                int gameFontSize = Math.min(getWidth() / 12, getHeight() / 12);
                gameFontSize = Math.max(30, gameFontSize);
                gameLabel.setFont(new Font("Arial", Font.BOLD, gameFontSize));

                // Resize image and text areas
                int iconSize = Math.min(panelHeight / 8, panelWidth / 12); // Made icons relatively smaller
                iconSize = Math.max(30, iconSize); // Minimum size

                int fontSize = Math.min(panelHeight / 25, panelWidth / 30); // Font size adjusted
                fontSize = Math.max(12, fontSize); // Minimum font size

                // Iterate through the stored components and update their sizes
                for (int i = 0; i < iconLabels.length; i++) {
                    if (originalImages[i] != null && iconLabels[i] != null && textAreas[i] != null) {
                        Image originalImage = originalImages[i];
                        JLabel iconLabel = iconLabels[i];
                        JTextArea textArea = textAreas[i];

                        // Scale the image
                        Image scaled = getScaledImage(originalImage, iconSize, iconSize);
                        iconLabel.setIcon(new ImageIcon(scaled));
                        iconLabel.setPreferredSize(new Dimension(iconSize, iconSize));

                        // Update text area font and size
                        textArea.setFont(new Font("Arial", Font.PLAIN, fontSize));
                        // Resize the text area to fit nicely next to the icon, considering padding/borders
                        int availableWidth = panelWidth - (iconSize + rowPanelInitialPadding * 2 + textAreaPadding * 2 + 20); // Account for icon, flowlayout hgap, textarea padding
                        int textWidth = Math.max(100, availableWidth);

                        int preferredTextHeight = (fontSize * 2) + (textAreaPadding * 2);
                        textArea.setPreferredSize(new Dimension(textWidth, preferredTextHeight));
                    }
                }

                // Resize close buttons
                int buttonSize = Math.min(panelWidth, panelHeight) / 10;
                buttonSize = Math.max(buttonSize, 30);
                closeButton.setPreferredSize(new Dimension(buttonSize, buttonSize));

                revalidate();
                repaint();
            }
        });

        // Force an initial resize event to apply layout immediately
        SwingUtilities.invokeLater(() -> {
            if (getWidth() > 0 && getHeight() > 0 && getComponentListeners().length > 0) {
                getComponentListeners()[0].componentResized(
                        new ComponentEvent(GameControlsPanel.this, ComponentEvent.COMPONENT_RESIZED)
                );
            }
        });
    }

    // A small constant for initial padding in rowPanel (adjust as needed)
    private static final int rowPanelInitialPadding = 10;
    // A small constant for padding in textArea (adjust as needed)
    private static final int textAreaPadding = 6;

    private JPanel addLine(String text, Image originalArrowImage){
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, rowPanelInitialPadding, rowPanelInitialPadding));
        rowPanel.setOpaque(false);

        if (currentLineIndex >= originalImages.length) {
            System.err.println("Warning: Attempted to add more control lines than array capacity. Increase array size.");
            return rowPanel; // Return empty panel to prevent further errors
        }

        // Store the original image
        originalImages[currentLineIndex] = originalArrowImage;

        // Create JLabel with a temporary icon. It will be scaled by the resize listener.
        int initialIconSize = 40; // Default size, will be updated
        Image initialScaledImage = getScaledImage(originalArrowImage, initialIconSize, initialIconSize);
        JLabel arrowLabel = new JLabel(new ImageIcon(initialScaledImage));
        arrowLabel.setPreferredSize(new Dimension(initialIconSize, initialIconSize)); // Set preferred size
        iconLabels[currentLineIndex] = arrowLabel; // Store reference
        rowPanel.add(arrowLabel);

        JTextArea textArea = new JTextArea(text);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setForeground(Color.BLACK);
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
        textArea.setBorder(BorderFactory.createEmptyBorder(textAreaPadding, textAreaPadding, textAreaPadding, textAreaPadding));
        textAreas[currentLineIndex] = textArea; // Store reference
        rowPanel.add(textArea);

        currentLineIndex++;
        return rowPanel;
    }

    // Utility method to scale images with Graphics2D for better quality and control
    private Image getScaledImage(Image srcImg, int w, int h){
        if (srcImg == null || w <= 0 || h <= 0) {
            return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB); // Return a tiny blank image
        }
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY); // High quality rendering
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Anti-aliasing
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }

    private Image loadImages(String path){
        try {
            // Get the URL for the resource from the classpath
            URL imageUrl = getClass().getResource(path);
            if (imageUrl != null) {
                // Read the image using ImageIO
                return ImageIO.read(imageUrl);
            } else {
                System.err.println("Resource not found: " + path);
                return null;
            }
        } catch (IOException e) {
            System.err.println("Error loading image from " + path + ": " + e.getMessage());
            return null;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        int width = getWidth();
        int height = getHeight();
        int arc = 30;

        // Draw background image if it is null or dimensions changed
        if (gameControlsImage != null) {
            g2.drawImage(gameControlsImage, 0, 0, width, height, this);
        } else {
            // Fallback
            g2.setColor(Color.GRAY.brighter());
            g2.fillRect(0, 0, width, height); // Fill entire panel
        }

        // Rounded rectangle clipping for a smooth shape
        RoundRectangle2D panelShape = new RoundRectangle2D.Float(0, 0, width, height, arc, arc);
        // Save the original clip of the graphic context
        g2.setClip(panelShape);


        // Draw rounded border
        //Restore the original clip BEFORE drawing the border and child components
        //to prevent the edge and child components from being cut by the rounded clip
        g2.setClip(null);

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
}
