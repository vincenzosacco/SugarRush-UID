package view.button;

import javax.swing.*;
import java.awt.*;

public class ExitButton extends JButton {
    public ExitButton() {
        super();
        setContentAreaFilled(false); // Disable default background rendering by JButton
        setFocusPainted(false);      // Disable focus border rendering
        setForeground(Color.WHITE);  // Set text color to white (not used directly, but kept for consistency)
        setBackground(new Color(0, 153, 0)); // Set custom green background color
        setFont(new Font("Arial", Font.BOLD, 16)); // Set font style for any possible text
        setOpaque(false); // Make button non-opaque (custom paint instead)
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Show hand cursor on hover
        setToolTipText("Exit Game"); // Tooltip displayed on mouse hover
        setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12)); // Padding around the content
        setText(null); // Removes any default text
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Smooth rendering

        // Draw rounded green background
        g2.setColor(Color.RED);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        g2.setColor(Color.WHITE);

        int fontSize = Math.min(getWidth(), getHeight()) / 3; // Example: 1/3 of the smallest size
        g2.setFont(new Font("Arial", Font.BOLD, Math.max(12, fontSize))); // At least 12

        // Get font metrics to calculate text position
        FontMetrics fm = g2.getFontMetrics();
        String text = "EXIT";
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent(); // Text height from baseline

        //Calculate the coordinates to center the text
        int x = (getWidth() - textWidth) / 2;
        int y = (getHeight() - textHeight) / 2 + fm.getAscent(); // getAscent to align correctly

        g2.drawString(text, x, y);

        g2.dispose(); // Free up graphic resources

        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Smooth rendering
        g2.setColor(Color.RED.darker()); // Use a darker color for the border
        g2.setStroke(new BasicStroke(3f)); // Set border thickness

        // Draw rounded border around the button
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

        g2.dispose(); // Clean up the graphics context
    }
}
