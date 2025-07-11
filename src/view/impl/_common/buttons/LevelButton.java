package view.impl._common.buttons;

import utils.Resources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class LevelButton extends JButton {

    // Images used for coin display (collected and missing)
    private BufferedImage coinImage;
    private BufferedImage missingCoinImage;

    /** Level number represented by this button*/
    private int num;

    /**Array indicating whether the 3 coins in the level have been collected*/
    private Boolean[] coinsCollected = new Boolean[3];

    // Field to store the calculated background color
    private Color currentBackgroundColor;

    // Default constructor (not used here, but provided)
    public LevelButton() {}

    // Constructor that initializes the button with a specific level number
    public LevelButton(int i) {
        this.num = i;

        // Remove default padding around the button text
        setMargin(new Insets(0, 0, 0, 0));

        // Set the button label to the level number
        setText(String.valueOf(num));
        // Text color will always be black,
        setForeground(Color.BLACK);

        // Make the button transparent and styled
        setContentAreaFilled(false);   // Don't fill with default background
        setOpaque(false);              // Transparent background
        setFocusPainted(false);        // No focus border
        setBorderPainted(false);       // No painted border
        setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover

        // Adjust font size dynamically based on button size
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int fontSize = (int) (getHeight() * 0.4); // Font is 40% of height
                setFont(new Font("Arial", Font.BOLD, fontSize));
            }
        });

    }

    /**
     * Determines and stores the background color of the button based on its enabled state
     * and the number of stars collected for this level.
     */
    private void updateBackgroundColor() {
        if (!isEnabled()) {
            // Button is blocked
            currentBackgroundColor = new Color(158, 158, 158,200); // Medium Gray if locked
        } else {
            // Button is enabled, check stars
            int collectedStars = 0;
            for (Boolean collected : coinsCollected) {
                if (collected) {
                    collectedStars++;
                }
            }

            if (collectedStars == 3) {
                // new Color(r, g, b, alpha)
                currentBackgroundColor = new Color(255, 215, 0, 200); // Semi-transparent gold
                // Soft Gold if 3 stars
            } else if (collectedStars >= 1) {
                currentBackgroundColor = new Color(76, 175, 80,200); // Semi-transparent Material green if 1 or 2 stars
            } else { // collectedStars == 0
                currentBackgroundColor = new Color(244, 67, 54,200); // Semi-transparent soft red if 0 stars but enabled
            }
        }
    }
    // Custom painting logic for the button
    @Override
    protected void paintComponent(Graphics g) {
        // DRAWING LOGIC //

        Graphics2D g2 = (Graphics2D) g.create();
        // Enable antialiasing for smoother graphics
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // --- DRAWING BACKGROUND CIRCLE WITH currentBackgroundColor ---
        if (currentBackgroundColor == null) { // Fallback if updateBackgroundColor hasn't been called yet
            updateBackgroundColor();
        }
        g2.setColor(currentBackgroundColor);
        // Fill a circular shape with the calculated background color
        int inset = 2; // inner margin
        g2.fill(new Ellipse2D.Double(inset, inset, getWidth() - 2 * inset, getHeight() - 2 * inset));
        // --- END DRAWING BACKGROUND CIRCLE ---

        // Draw a white circular border around the button
        g2.setClip(null); // Remove clip so border isn't cut
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2.5f)); // 2.5-pixel border
        g2.drawOval(inset, inset, getWidth() - 2 * inset, getHeight() - 2 * inset); // Circle border

        int coinSize = (int) (getHeight() * 0.40); // Coin image size
        if (coinImage == null )
            coinImage = Resources.getBestImage("/imgs/icons/star.jpg", coinSize, coinSize);
        if (missingCoinImage == null )
            missingCoinImage = Resources.getBestImage("/imgs/panels/levels/missingStar.jpg", coinSize, coinSize);

        // Draw coin indicators at the bottom of the button
        int spacing = coinSize / 20;               // Small spacing between coins
        int totalWidth = (3 * coinSize) + (2 * spacing); // Total width of coin row
        int startX = (getWidth() - totalWidth) / 2; // Center alignment
        int y = getHeight() - coinSize;             // Bottom of the button

        // Loop to draw 3 coin images (collected or missing)
        for (int i = 0; i < 3; i++) {
            int x = startX + i * (coinSize + spacing);
            BufferedImage img = coinsCollected[i] ? coinImage : missingCoinImage;

            g2.drawImage(img, x, y, coinSize, coinSize, this);
        }

        // --- CUSTOM TEXT DRAWING: Always Black ---
        g2.setColor(Color.BLACK); // Text is ALWAYS black
        g2.setFont(getFont()); // Use the button's current font

        // Get FontMetrics to center the text
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(getText());
        int textHeight = fm.getHeight();
        int textX = (getWidth() - textWidth) / 2;

        // Position text slightly higher than center to make space for stars
        int textY = (getHeight() - textHeight) / 2 + fm.getAscent();

        g2.drawString(getText(), textX, textY);
        // --- END CUSTOM TEXT DRAWING ---

        g2.dispose();
    }

    // Override setEnabled to also call updateBackgroundColor
    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        updateBackgroundColor(); // Update the stored background color
        repaint(); // Request a repaint to draw with the new background color
    }

    // Custom click detection — only respond to clicks within the circular shape
    @Override
    public boolean contains(int x, int y) {
        double radius = getWidth() / 2.0;
        double centerX = radius;
        double centerY = getHeight() / 2.0;

        // Check if the click point is inside the circle
        return Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2) <= radius * radius;
    }

    // Setter method to update which stars have been collected
    public void setStarsCollected(Boolean[] stars) {
        this.coinsCollected = stars.clone();
        updateBackgroundColor(); // Update background color when stars collected change
        repaint(); // Refresh button appearance
    }
}