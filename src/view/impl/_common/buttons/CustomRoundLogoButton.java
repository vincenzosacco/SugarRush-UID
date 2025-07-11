package view.impl._common.buttons;

import utils.Resources;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class CustomRoundLogoButton extends JButton {

    private BufferedImage settingsImage; // Field to hold the settings icon image
    private final Color color;


    public CustomRoundLogoButton(String text,Color color) {
        this.color=color;

        setContentAreaFilled(false);         // Disables the default background filling.
        setBorderPainted(false);             // Disables the default border drawing.
        setFocusPainted(false);              // Disables the focus highlight.
        setOpaque(false);                    // Makes the background transparent.
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Changes cursor to hand on hover.
        setToolTipText(text);             // Tooltip text shown when hovering over the buttons.
        setMargin(new Insets(0, 0, 0, 0));    // Removes all internal margins.
        setText(null); // Ensure no default text is drawn
        // Load the image once in the constructor
        // Gets the resource URL from the classpath.
        settingsImage = (BufferedImage) Resources.getImage("/imgs/icons/"+text+".jpg");
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Smooth edges

        int width = getWidth();
        int height = getHeight();
        float strokeWidth = 2.5f;

        // Fill the buttons with a color background
        g2.setColor(color);
        g2.fillOval(0, 0, width, height);

        // Draw a black inner border with centered stroke
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(strokeWidth));
        float offset = strokeWidth / 2f;
        g2.draw(new Ellipse2D.Float(offset, offset, width - strokeWidth, height - strokeWidth));

        // Remove clipping to allow full drawing range
        g2.setClip(null);

        // Draw the settings image at the center
        if (settingsImage != null) {
            // Determine the size for the image (e.g., 60% of the buttons's diameter)
            int imageDiameter = (int) (Math.min(width, height) * 0.6);

            // Scale the image
            Image scaledImage = settingsImage.getScaledInstance(imageDiameter, imageDiameter, Image.SCALE_SMOOTH);

            // Calculate position to center the image
            int x = (width - imageDiameter) / 2;
            int y = (height - imageDiameter) / 2;

            g2.drawImage(scaledImage, x, y, this);
        } else {
            // Fallback: draw an "E" or placeholder if image fails to load
            g2.setColor(color);
            g2.setFont(new Font("Arial", Font.BOLD, Math.min(width, height) / 4));
            String fallbackText = "E";
            FontMetrics fm = g2.getFontMetrics();
            int textX = (width - fm.stringWidth(fallbackText)) / 2;
            int textY = (height - fm.getAscent()) / 2 + fm.getAscent();
            g2.drawString(fallbackText, textX, textY);
        }

        g2.dispose();  // Clean up graphics context
        super.paintComponent(g);
    }

    @Override
    public boolean contains(int x, int y) {
        // Override to make only the circular area respond to mouse clicks
        Ellipse2D shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
        return shape.contains(x, y);
    }

    @Override
    public Dimension getPreferredSize() {
        // Ensure the buttons is square by making width and height equal
        Dimension d = super.getPreferredSize();
        int size = Math.min(d.width, d.height);
        return new Dimension(size, size);
    }
}