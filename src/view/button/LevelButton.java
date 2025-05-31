package view.button;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LevelButton extends JButton {
    // Background image for this level button
    private BufferedImage backgroundImage;

    // Images used for coin display (collected and missing)
    private BufferedImage coinImage;
    private BufferedImage missingCoinImage;

    // Level number represented by this button
    private int num;

    // Array indicating whether the 3 coins in the level have been collected
    private boolean[] coinsCollected = new boolean[3];

    // Default constructor (not used here, but provided)
    public LevelButton() {}

    // Constructor that initializes the button with a specific level number
    public LevelButton(int i) {
        this.num = i;

        // Load background image for this level button
        try {
            backgroundImage = ImageIO.read(new File("resources/backgroundLevelButton" + num + ".jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Load images for coins (collected and not collected)
        try {
            coinImage = ImageIO.read(new File("resources/coin.jpg"));
            missingCoinImage = ImageIO.read(new File("resources/missingCoin.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Remove default padding around the button text
        setMargin(new Insets(0, 0, 0, 0));

        // Set the button label to the level number
        setText(String.valueOf(num));
        setForeground(Color.BLACK); // Set text color to black

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

    // Custom painting logic for the button
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // Enable antialiasing for smoother graphics
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Clip drawing area to a circular shape
        Shape circle = new Ellipse2D.Double(0, 0, getWidth(), getHeight());
        g2.setClip(circle);

        // Draw background image scaled to button size
        g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        // Draw a white circular border around the button
        g2.setClip(null); // Remove clip so border isn't cut
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3f)); // 3-pixel border
        g2.drawOval(1, 1, getWidth() - 3, getHeight() - 3); // Circle border

        // Draw coin indicators at the bottom of the button
        int coinSize = (int) (getHeight() * 0.40); // Coin image size
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

        // Draw button label text and UI elements
        super.paintComponent(g);

        g2.dispose();
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

    // Setter method to update which coins have been collected
    public void setCoinsCollected(boolean[] coins) {
        this.coinsCollected = coins;
        repaint(); // Refresh button appearance
    }
}
