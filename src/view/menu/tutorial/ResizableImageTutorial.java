package view.menu.tutorial;

import javax.swing.*;
import java.awt.*;

public class ResizableImageTutorial extends JLabel {
    private Image originalImage;

    // Constructor takes the original image to be displayed and resized
    public ResizableImageTutorial(Image originalImage) {
        this.originalImage = originalImage;
        setHorizontalAlignment(CENTER);  // Center the image horizontally
        setVerticalAlignment(CENTER);    // Center the image vertically
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around the image
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (originalImage != null) {
            // Create a copy of Graphics to apply rendering hints without affecting others
            Graphics2D g2d = (Graphics2D) g.create();

            // Improve image rendering quality with interpolation and anti-aliasing
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Insets insets = getInsets();
            int availableWidth = getWidth() - insets.left - insets.right;   // Width available inside the label considering insets
            int availableHeight = getHeight() - insets.top - insets.bottom; // Height available inside the label considering insets

            int imgWidth = originalImage.getWidth(null);
            int imgHeight = originalImage.getHeight(null);

            // Calculate aspect ratios of image and available panel area
            float imgAspect = (float) imgWidth / imgHeight;
            float panelAspect = (float) availableWidth / availableHeight;

            int drawWidth, drawHeight;

            // Scale the image to fit inside the label without distortion,
            // preserving aspect ratio and centering it
            if (imgAspect > panelAspect) {
                drawWidth = availableWidth;
                drawHeight = (int) (availableWidth / imgAspect);
            } else {
                drawHeight = availableHeight;
                drawWidth = (int) (availableHeight * imgAspect);
            }

            // Calculate coordinates to center the image within available space
            int x = insets.left + (availableWidth - drawWidth) / 2;
            int y = insets.top + (availableHeight - drawHeight) / 2;

            // Draw the scaled image
            g2d.drawImage(originalImage, x, y, drawWidth, drawHeight, this);

            // Dispose the graphics context to free resources
            g2d.dispose();
        }
    }
}

