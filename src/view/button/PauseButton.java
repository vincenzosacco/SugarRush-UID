package view.button;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class PauseButton extends JButton {

    private BufferedImage pauseImage; // Field to hold the pause icon image


    public PauseButton() {
        setContentAreaFilled(false);         // Disables the default background filling.
        setBorderPainted(false);             // Disables the default border drawing.
        setFocusPainted(false);              // Disables the focus highlight.
        setOpaque(false);                    // Makes the background transparent.
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Changes cursor to hand on hover.
        setToolTipText("Pause");             // Tooltip text shown when hovering over the button.
        setMargin(new Insets(0, 0, 0, 0));    // Removes all internal margins.
        setText(null); // Ensure no default text is drawn
        // Load the image once in the constructor
        try {
            // Gets the resource URL from the classpath.
            URL imageUrl = getClass().getResource("/imgs/icons/pause.jpg");

            if (imageUrl == null) {
                System.err.println("Error: Image resource not found in classpath: /resources/imgs/icons/pause.jpg");
            } else {
                pauseImage = ImageIO.read(imageUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading image pause.jpg: " + e.getMessage());
        }


    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Smooth edges

        int width = getWidth();
        int height = getHeight();
        float strokeWidth = 2f;

        // Fill the button with a solid White circular background
        g2.setColor(Color.WHITE);
        g2.fillOval(0, 0, width, height);

        // Draw a black inner border with centered stroke
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(strokeWidth));
        float offset = strokeWidth / 2f;
        g2.draw(new Ellipse2D.Float(offset, offset, width - strokeWidth, height - strokeWidth));

        // Remove clipping to allow full drawing range
        g2.setClip(null);

        // Draw a darker gray outer border for extra definition
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(3f));
        g2.drawOval(1, 1, width - 3, height - 3);

        // Draw the pause image at the center
        if (pauseImage != null) {
            // Determine the size for the image (e.g., 60% of the button's diameter)
            int imageDiameter = (int) (Math.min(width, height) * 0.6);

            // Scale the image
            Image scaledImage = pauseImage.getScaledInstance(imageDiameter, imageDiameter, Image.SCALE_SMOOTH);

            // Calculate position to center the image
            int x = (width - imageDiameter) / 2;
            int y = (height - imageDiameter) / 2;

            g2.drawImage(scaledImage, x, y, this);
        } else {
            // Fallback: draw an "P" or placeholder if image fails to load
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, Math.min(width, height) / 4));
            String fallbackText = "P";
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
        // Ensure the button is square by making width and height equal
        Dimension d = super.getPreferredSize();
        int size = Math.min(d.width, d.height);
        return new Dimension(size, size);
    }
}
