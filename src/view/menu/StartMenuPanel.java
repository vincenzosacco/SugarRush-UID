package view.menu;

import config.ModelConfig;
import controller.ControllerObj;
import model.profile.ProfileManager;
import view.ViewComp;
import view.button.CustomButton;
import view.button.LevelButton;
import view.menu.tutorial.Tutorial;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import static config.ModelConfig.MAX_COINS;
import static config.ViewConfig.BOARD_HEIGHT;
import static config.ViewConfig.BOARD_WIDTH;

public class StartMenuPanel extends JPanel implements ViewComp {
    // Background image for the panel
    private BufferedImage backgroundImage;

    // Array to hold buttons representing levels (e.g. Level 1 to Level 6)
    private final LevelButton[] levelButton;

    // Total number of levels available in the game
    private final int numLevel = ModelConfig.NUM_LEVELS;

    private final JLabel coinCounterLabel;
    private ImageIcon coinIcon; // needed for the coin counter label resizing

    // Tutorial's button
    private final CustomButton tutorialButton;

    public StartMenuPanel() {
        // Initialize the array of level buttons
        levelButton = new LevelButton[numLevel];

        coinIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/imgs/icons/coinsImmage.png")));
        // Resize the icon to fit better
        Image image = coinIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        coinIcon = new ImageIcon(image);

        // The current number of coins
        coinCounterLabel = new JLabel(coinIcon, JLabel.LEFT);
        coinCounterLabel.setFont(new Font("Arial", Font.BOLD, 24));
        coinCounterLabel.setForeground(Color.WHITE); // Make text more visible
        add(coinCounterLabel);

        // The tutorial's button
        tutorialButton = new CustomButton("Tutorial",Color.BLACK,Color.YELLOW);
        add(tutorialButton);

        tutorialButton.addActionListener(e -> showTutorialDialog());

        // Create each level button and load its corresponding level data
        for (int i = 1; i <= numLevel; i++) {
            LevelButton button = new LevelButton(i);

            // Set coins collected status for the button (used to update its display)
            button.setStarsCollected(ProfileManager.getLastProfile().getLevelStarsCount(i));

            // Store button in the array and add to the panel
            levelButton[i - 1] = button;
            add(button);
        }

        // Load the background image for the start menu panel

        try {
            // Gets the resource URL from the classpath.
            URL imageUrl = getClass().getResource("/imgs/panels/levels/levelMenu.jpg");

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

        this.setFocusable(true);
    }

    // FIXME ci sono modi più efficienti e con meno righe per fare questo. Ad esempio usare un layout manager adatto
    // Position the level buttons dynamically based on the current size of the panel
    private void positionButtons() {
        int w = getWidth();   // Current width of the panel
        int h = getHeight();  // Current height of the panel

        // Coin label
        FontMetrics fm= coinCounterLabel.getFontMetrics(coinCounterLabel.getFont());

//        int labelWidth = (int)(w * 0.12);  // 12% of panel width
//        int labelHeight = (int)(h * 0.07); // 7% of panel height
        int labelWidth= fm.stringWidth(String.valueOf(MAX_COINS)) +coinIcon.getImage().getWidth(null)+10;
        int labelHeight= fm.getHeight();

        int labelX= (int) (w * 0.02);
        int labelY= (int) (h * 0.05);
        coinCounterLabel.setBounds(labelX, labelY, labelWidth, labelHeight);
        // Tutorial button
        int tutBtnWidth = (int)(w * 0.2);  // 20% of width
        int tutBtnHeight = (int)(h * 0.08); // 8% of height
        int tutBtnX = (int)(w * 0.77);      // margin from right
        int tutBtnY = (int)(h * 0.05);
        tutorialButton.setBounds(tutBtnX, tutBtnY, tutBtnWidth, tutBtnHeight);
        tutorialButton.setFont(new Font("Arial", Font.BOLD, Math.max(12, tutBtnHeight / 3)));  // scale font

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
    // Opens a dialog window containing the tutorial
    private void showTutorialDialog() {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentWindow);
        //Removes the standard window decoration, i.e. title bar, borders,etc.
        dialog.setUndecorated(true);
        //Set the dialog to modal.
        //This means that as long as the dialog is open, the user cannot interact with other windows of the same application.
        dialog.setModal(true);
        dialog.setResizable(false);

        Tutorial tutorialPanel = new Tutorial(dialog);

        Dimension parentSize = parentWindow.getSize();
        int newWidth = (int) (parentSize.width * 0.8);
        int newHeight = (int) (parentSize.height * 0.8);
        tutorialPanel.setPreferredSize(new Dimension(newWidth, newHeight));

        //Adds the tutorialPanel component to the dialog window's content pane.
        dialog.getContentPane().add(tutorialPanel);
        //Calculates the minimum size needed and resizes the window accordingly.
        dialog.pack();

        try {
            dialog.setShape(new RoundRectangle2D.Double(0, 0, dialog.getWidth(), dialog.getHeight(), 30, 30));
        } catch (UnsupportedOperationException ex) {
            System.out.println("Rounded corners not supported");
        }

        //the dialog is positioned in the center of the parentWindow.
        dialog.setLocationRelativeTo(parentWindow);
        dialog.setVisible(true);
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

// ----------------------------------------OVERRIDE METHODS-------------------------------------------------------------

    // Override paintComponent to draw the scaled background image behind the buttons
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            // Draw the background image stretched to fill the entire panel
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        // coinCounter con lable
        int coins = ProfileManager.getLastProfile().getCoins();
        coinCounterLabel.setText(String.valueOf(coins));
    }

    // Provides external access to the array of level buttons
    public LevelButton[] getLevelButtons() {
        return levelButton;
    }


    // Returns the specific level button at index i
    public LevelButton getLevelButton(int i) {
        return levelButton[i];
    }

    @Override
    public void bindController(ControllerObj controller) {

    }
}
