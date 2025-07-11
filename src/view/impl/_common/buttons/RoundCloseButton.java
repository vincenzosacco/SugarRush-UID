package view.impl._common.buttons;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class RoundCloseButton extends JButton {

    private final int dimButton=30;

    public RoundCloseButton() {
        setContentAreaFilled(false);         // Disables the default background filling.
        setBorderPainted(false);             // Disables the default border drawing.
        setFocusPainted(false);              // Disables the focus highlight.
        setOpaque(false);                    // Makes the background transparent.
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Changes cursor to hand on hover.
        setToolTipText("Close");             // Tooltip text shown when hovering over the buttons.
        setMargin(new Insets(0, 0, 0, 0));    // Removes all internal margins.
        setText(null);                       // Removes any default text.
        setPreferredSize(new Dimension(dimButton,dimButton));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Smooth edges

        int width = getWidth();
        int height = getHeight();

        // Fill the buttons with a red circular background
        g2.setColor(new Color(220, 53, 69));
        g2.fillOval(0, 0, width, height);

        // Draw a black inner border with centered stroke
        float strokeInner = 2f;
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(strokeInner));
        float offsetInner = strokeInner / 2f;
        g2.drawOval(
                Math.round(offsetInner),
                Math.round(offsetInner),
                Math.round(width - strokeInner),
                Math.round(height - strokeInner)
        );

        // Draw the white "X" in the center of the buttons
        g2.setColor(Color.WHITE);

        int fontSize = Math.max(width / 2, 12);  // Font size is half the width, but at least 12
        Font font = new Font("Monospaced", Font.BOLD, fontSize);
        g2.setFont(font);

        FontMetrics fm = g2.getFontMetrics();
        String text = "X";
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        int x = (width - textWidth) / 2;
        int y = (height - textHeight) / 2 + fm.getAscent();

        g2.drawString(text, x, y);  // Draw the X text at calculated coordinates

        g2.dispose();  // Clean up graphics context
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