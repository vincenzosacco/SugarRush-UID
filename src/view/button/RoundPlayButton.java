package view.button;

import javax.swing.*;
import java.awt.*;

public class RoundPlayButton extends JButton {

    public RoundPlayButton() {
        super();
        setContentAreaFilled(false); // Disable default background rendering by JButton
        setFocusPainted(false);      // Disable focus border rendering
        setForeground(Color.WHITE);  // Set text color to white (not used directly, but kept for consistency)
        setBackground(new Color(0, 153, 0)); // Set custom green background color
        setFont(new Font("Arial", Font.BOLD, 16)); // Set font style for any possible text
        setOpaque(false); // Make button non-opaque (custom paint instead)
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Show hand cursor on hover
        setToolTipText("Play"); // Tooltip displayed on mouse hover
        setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12)); // Padding around the content
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Smooth rendering

        // Draw rounded green background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        // Size of the play triangle icon
        int triangleSize = Math.min(getWidth(), getHeight()) / 2;

        // Coordinates of the triangle's three points (play symbol) relative to (0,0)
        int x1 = 0;
        int y1 = 0;
        int x2 = 0;
        int y2 = triangleSize;
        int x3 = triangleSize;
        int y3 = triangleSize / 2;

        // Calculate centroid of the triangle (for centering)
        int cx = (x1 + x2 + x3) / 3;
        int cy = (y1 + y2 + y3) / 3;

        // Center of the button
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // Offset needed to center the triangle in the button
        int offsetX = centerX - cx;
        int offsetY = centerY - cy;

        // Create and position the triangle polygon
        Polygon triangle = new Polygon();
        triangle.addPoint(x1 + offsetX, y1 + offsetY);
        triangle.addPoint(x2 + offsetX, y2 + offsetY);
        triangle.addPoint(x3 + offsetX, y3 + offsetY);

        // Draw the triangle in white
        g2.setColor(Color.WHITE);
        g2.fill(triangle);

        g2.dispose(); // Clean up the graphics context
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Smooth rendering
        g2.setColor(getBackground().darker()); // Use a darker color for the border
        g2.setStroke(new BasicStroke(3f)); // Set border thickness

        // Draw rounded border around the button
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

        g2.dispose(); // Clean up the graphics context
    }
}