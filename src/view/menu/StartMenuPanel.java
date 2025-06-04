package view.menu;

import controller.ControllerObj;
import model.game.LevelData;
import view.ViewComp;
import view.button.LevelButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static config.View.BOARD_HEIGHT;
import static config.View.BOARD_WIDTH;

public class StartMenuPanel extends JPanel implements ViewComp {
    // Background image for the panel
    private BufferedImage backgroundImage;

    // Array to hold buttons representing levels (e.g. Level 1 to Level 6)
    private final LevelButton[] levelButton;

    // Total number of levels available in the game
    private final int numLevel = 6;

    public StartMenuPanel() {
        // Initialize the array of level buttons
        levelButton = new LevelButton[numLevel];

        // Create each level button and load its corresponding level data
        for (int i = 1; i <= numLevel; i++) {
            LevelButton button = new LevelButton(i);

            // Load level data file
            InputStream file = getClass().getResourceAsStream("/map" + i + ".txt");
            LevelData levelData = new LevelData(file);

            // Set coins collected status for the button (used to update its display)
            button.setCoinsCollected(levelData.getCoinsCollected());

            // Store button in the array and add to the panel
            levelButton[i - 1] = button;
            add(button);
        }

        // Load the background image for the start menu panel

        try {
            // Gets the resource URL from the classpath.
            URL imageUrl = getClass().getResource("/levelMenu.jpg");

            if (imageUrl == null) {
                System.err.println("Error: Image resource not found in classpath: /resources/levelMenu.jpg");
            } else {
                backgroundImage = ImageIO.read(imageUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading image levelMenu.jpg: " + e.getMessage());
        }

        // Use absolute positioning to allow precise control over button placement
        setLayout(null);

        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));

        // Add a listener to reposition buttons whenever the panel size changes
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                positionButtons();
            }
        });

        // Ensure buttons are positioned correctly after the component is first rendered
        SwingUtilities.invokeLater(this::positionButtons);
    }

    // Position the level buttons dynamically based on the current size of the panel
    private void positionButtons() {
        int w = getWidth();   // Current width of the panel
        int h = getHeight();  // Current height of the panel

        int posX = 0, posY = 0;

        // Define the relative positions of each button (customized per level)
        for (int i = 0; i < numLevel; i++) {
            switch (i) {
                case 0:
                    posX = (int) (w * 0.72);
                    posY = (int) (h * 0.8);
                    break;
                case 1:
                    posX = (int) (w * 0.28);
                    posY = (int) (h * 0.8);
                    break;
                case 2:
                    posX = (int) (w * 0.47);
                    posY = (int) (h * 0.55);
                    break;
                case 3:
                    posX = (int) (w * 0.18);
                    posY = (int) (h * 0.38);
                    break;
                case 4:
                    posX = (int) (w * 0.78);
                    posY = (int) (h * 0.4);
                    break;
                case 5:
                    posX = (int) (w * 0.45);
                    posY = (int) (h * 0.22);
                    break;
                default:
                    break;
            }

            // Calculate button size as 8% of the smaller panel dimension (width or height)
            double DimButton = (Math.min(w, h)) * 0.08;

            // Set the bounds of the button: x, y position and width & height size
            levelButton[i].setBounds(posX, posY, (int) DimButton, (int) DimButton);
        }
    }

    // Opens a dialog window containing the selected level's panel
    public void showLevelDialog(LevelPanel levelPanel) {
        // Retrieve the top-level window (e.g., JFrame) that contains this panel
        Window parentWindow = SwingUtilities.getWindowAncestor(this);

        // Get the dimensions of the parent window to calculate proportional dialog size
        Dimension parentSize = parentWindow.getSize();
        int newWidth = parentSize.width / 2;
        int newHeight = parentSize.height / 2;

        // Set the preferred size of the level panel to be displayed in the dialog
        levelPanel.setPreferredSize(new Dimension(newWidth, newHeight));

        // Create a modal dialog (blocks interaction with other windows while open)
        JDialog dialog = new JDialog(parentWindow);
        dialog.setUndecorated(true);  // Remove window borders and title bar
        dialog.setModal(true);        // Make dialog modal
        dialog.setResizable(false);   // Disable resizing by the user

        // Add the level panel to the dialog and adjust dialog size
        dialog.getContentPane().add(levelPanel);
        dialog.pack(); // Automatically size dialog to fit its contents

        // Attempt to apply rounded corners to the dialog (if the platform supports it)
        try {
            dialog.setShape(new RoundRectangle2D.Double(0, 0, dialog.getWidth(), dialog.getHeight(), 30, 30));
        } catch (UnsupportedOperationException ex) {
            System.out.println("Rounded corners not supported on this platform");
        }

        // Center the dialog relative to the parent window
        dialog.setLocationRelativeTo(parentWindow);

        // Display the dialog
        dialog.setVisible(true);
    }

    // Override paintComponent to draw the scaled background image behind the buttons
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            // Draw the background image stretched to fill the entire panel
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // Provides external access to the array of level buttons
    public LevelButton[] getLevelButtons() {
        return levelButton;
    }

    // Returns the total number of levels
    public int getNumLevels() {
        return numLevel;
    }

    // Returns the specific level button at index i
    public LevelButton getLevelButton(int i) {

        return levelButton[i];
    }

    @Override
    public void bindController(ControllerObj controller) {

    }
}
