package view.button;

import javax.swing.*;
import java.awt.*;

public class TutorialButton extends JButton {
    public TutorialButton() {
        super();
        setContentAreaFilled(false); // Disable default background rendering by JButton
        setFocusPainted(false);      // Disable focus border rendering
        setForeground(Color.BLACK);  // Set text color to black (not used directly, but kept for consistency)
        setBackground(Color.YELLOW); // Set yellow background color
        setFont(new Font("Arial", Font.BOLD, 16)); // Set font style for any possible text
        setOpaque(false); // Make button non-opaque (custom paint instead)
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Show hand cursor on hover
        setToolTipText("Tutorial"); // Tooltip displayed on mouse hover
        setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12)); // Padding around the content
        setText(null); // Removes any default text
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Smooth rendering

        // Draw rounded yellow background
        g2.setColor(Color.YELLOW);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        g2.setColor(Color.BLACK);

        int fontSize = Math.min(getWidth(), getHeight()) / 3; // Example: 1/3 of the smallest size
        g2.setFont(new Font("Arial", Font.BOLD, Math.max(12, fontSize))); // At least 12

        // Get font metrics to calculate text position
        FontMetrics fm = g2.getFontMetrics();
        String text = "Tutorial";
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
        g2.setColor(Color.BLACK); // Use black color for the border

        float strokeWidth = 2.5f;
        g2.setStroke(new BasicStroke(strokeWidth)); // Set border thickness

        int offset = (int)(strokeWidth / 2);
        int width = getWidth() - 2 * offset +1;
        int height = getHeight() - 2 * offset+1;

        // Draw rounded border around the button
        g2.drawRoundRect(0, 0, width, height, 20, 20);

        g2.dispose(); // Clean up the graphics context
    }
}
