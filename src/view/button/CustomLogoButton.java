package view.button;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class CustomLogoButton extends JButton {
    private BufferedImage image;
    private final Color color;
    public CustomLogoButton(String text,Color color){
        this.color=color;
        setContentAreaFilled(false);         // Disables the default background filling.
        setBorderPainted(false);             // Disables the default border drawing.
        setFocusPainted(false);              // Disables the focus highlight.
        setOpaque(false);                    // Makes the background transparent.
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Changes cursor to hand on hover.
        setToolTipText(text);             // Tooltip text shown when hovering over the button.
        setMargin(new Insets(0, 0, 0, 0));    // Removes all internal margins.
        setText(null);                       // Removes any default text.

        try {
            // Gets the resource URL from the classpath.
            URL imageUrl = getClass().getResource("/imgs/icons/"+text+".jpg");

            if (imageUrl == null) {
                System.err.println("Error: Image resource not found in classpath: /imgs/icons/"+text+".jpg");
            } else {
                image = ImageIO.read(imageUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading image "+text+".jpg: " + e.getMessage());
        }
    }
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Smooth rendering

        // Draw rounded color background
        g2.setColor(color);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);


        if (image != null) {
            int buttonWidth = getWidth();
            int buttonHeight = getHeight();

            // Calculate the maximum size for the image (e.g. 70% of the smallest button size)
            int imageSize = (int) (Math.min(buttonWidth, buttonHeight) * 0.7);

            // Scale the image
            Image scaledImage = image.getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH);

            // Calculate the coordinates to center the image
            int x = (buttonWidth - imageSize) / 2;
            int y = (buttonHeight - imageSize) / 2;

            // Draw the scaled and centered image
            g2.drawImage(scaledImage, x, y, this);
        } else {
            // Fallback if image not loaded
            g2.setColor(Color.BLACK);
            g2.drawString("E", getWidth() / 2 - 5, getHeight() / 2 + 5); // paint "E"
        }

        g2.dispose(); //Free up graphic resources

        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Smooth rendering
        g2.setColor(Color.BLACK); // Use black color for the border
        g2.setStroke(new BasicStroke(2f)); // Set border thickness

        // Draw rounded border around the button
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

        g2.dispose(); // Clean up the graphics context
    }
}
