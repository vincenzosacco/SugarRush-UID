package view.base;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public abstract class BaseDialog extends BasePanel {
    // Background image for the panel
    private BufferedImage backgroundImage;

    public BaseDialog() {
        // Use BorderLayout and transparency
        setLayout(new BorderLayout());
        setOpaque(false);

        backgroundImage = loadBackgroundImage();
    }

    protected abstract BufferedImage loadBackgroundImage();

    private int lastWidth = 0;
    private int lastHeight = 0;
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int arc = 30;

        // Rounded rectangle clipping for a smooth shape
        RoundRectangle2D panelShape = new RoundRectangle2D.Float(0, 0, width, height, arc, arc);
        // Save the original clip of the graphic context
        Shape originalClip = g2.getClip();
        g2.setClip(panelShape);

        // Draw background image if it is null or dimensions changed
        if (backgroundImage == null || width != lastWidth || height != lastHeight) {
            lastWidth = width;
            lastHeight = height;
            // Get the background image
            backgroundImage = loadBackgroundImage();
        } else {
            g2.drawImage(backgroundImage, 0, 0, width, height, this);
        }

        // Draw rounded border
        //Restore the original clip BEFORE drawing the border and child components
        //to prevent the edge and child components from being cut by the rounded clip
        g2.setClip(originalClip);

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

