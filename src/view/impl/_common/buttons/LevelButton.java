package view.impl._common.buttons;

import controller.menu.LevelButtonController;
import utils.Resources;
import view.impl.home.levelsMap.LevelsMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class LevelButton extends JButton {
    // Background image for this level buttons
    private BufferedImage backgroundImage;

    // Images used for coin display (collected and missing)
    private BufferedImage coinImage;
    private BufferedImage missingCoinImage;

    /** Level number represented by this buttons*/
    private int levelIndex;

    /**Array indicating whether the 3 coins in the level have been collected*/
    private Boolean[] coinsCollected = new Boolean[3];

    // Default constructor (not used here, but provided)
    public LevelButton() {}

    // Constructor that initializes the buttons with a specific level number
    public LevelButton(int i) {
        this.levelIndex = i;

        // Remove default padding around the buttons text
        setMargin(new Insets(0, 0, 0, 0));

        // Set the buttons label to the level number
        setText(String.valueOf(levelIndex));
        setForeground(Color.BLACK); // Set text color to black

        // Make the buttons transparent and styled
        setContentAreaFilled(false);   // Don't fill with default background
        setOpaque(false);              // Transparent background
        setFocusPainted(false);        // No focus border
        setBorderPainted(false);       // No painted border
        setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover

        SwingUtilities.invokeLater(this::bindController);
    }


    // Setter method to update which stars have been collected
    public void setStarsCollected(Boolean[] stars) {
        this.coinsCollected = stars.clone();
        repaint(); // Refresh buttons appearance
    }
    //------------------------------------- SWING METHODS -------------------------------------------------------------------
    // Custom painting logic for the buttons
    @Override
    protected void paintComponent(Graphics g) {

        // INIT BUFFERED IMAGES (for better performance/quality ratio )//
        // maybe this 'if' can be in another method (not constructor because size is not set yet) to avoid
        // check every time paintComponent is called. At the moment, it is not a big problem
        if (backgroundImage == null)
            backgroundImage = Resources.getBestImage("/imgs/panels/levels/level" + levelIndex + "-button.jpg",
                    getWidth(), getHeight());

        int coinSize = (int) (getHeight() * 0.40); // Coin image size
        if (coinImage == null )
            coinImage = Resources.getBestImage("/imgs/icons/star.jpg", coinSize, coinSize);
        if (missingCoinImage == null )
            missingCoinImage = Resources.getBestImage("/imgs/panels/levels/missingStar.jpg", coinSize, coinSize);


        // DRAWING LOGIC //
        Graphics2D g2 = (Graphics2D) g.create();

        // Enable antialiasing for smoother graphics
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Clip drawing area to a circular shape
        Shape circle = new Ellipse2D.Double(0, 0, getWidth(), getHeight());
        g2.setClip(circle);

        // Draw background image scaled to buttons size
        g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        // Draw a white circular border around the buttons
        g2.setClip(null); // Remove clip so border isn't cut
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2.5f)); // 2.5-pixel border
        g2.drawOval(1, 1, getWidth() - 3, getHeight() - 3); // Circle border

        // Draw coin indicators at the bottom of the buttons
        int spacing = coinSize / 20;               // Small spacing between coins
        int totalWidth = (3 * coinSize) + (2 * spacing); // Total width of coin row
        int startX = (getWidth() - totalWidth) / 2; // Center alignment
        int y = getHeight() - coinSize;             // Bottom of the buttons

        // Loop to draw 3 coin images (collected or missing)
        for (int i = 0; i < 3; i++) {
            int x = startX + i * (coinSize + spacing);
            BufferedImage img = coinsCollected[i] ? coinImage : missingCoinImage;

            g2.drawImage(img, x, y, coinSize, coinSize, this);
        }

        // Draw buttons label text and UI elements
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

    // ------------------------------------- CONTROLLER METHODS -----------------------------------------------------
    /** called by addNotify */
    private void bindController() {
        // Main controller
        LevelsMap parent = (LevelsMap) SwingUtilities.getAncestorOfClass(LevelsMap.class, this);
        assert parent != null: "Error: LevelButton must be a child of LevelsMap";
        addActionListener(new LevelButtonController(parent, levelIndex));

        // Adjust font size dynamically based on buttons size
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int fontSize = (int) (getHeight() * 0.4); // Font is 40% of height
                setFont(new Font("Arial", Font.BOLD, fontSize));
            }
        });
    }

}
