package view.impl.home.levelsMap;

import config.ModelConfig;
import persistance.profile.ProfileManager;
import utils.Resources;
import view.base.BasePanel;
import view.impl._common.buttons.CustomButton;
import view.impl._common.buttons.LevelButton;
import view.impl._common.panels.CoinCountPanel;
import view.impl.tutorial.Tutorial;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import static config.ViewConfig.BOARD_HEIGHT;
import static config.ViewConfig.BOARD_WIDTH;

public class LevelsMap extends BasePanel {
    // Background image for the panel
    private BufferedImage backgroundImage;

    // Array to hold buttons representing levelsMap (e.g. Level 1 to Level 6)
    private final LevelButton[] levelButtons;

    // Total number of levelsMap available in the game
    private final int numLevels = ModelConfig.NUM_LEVELS;

    private final CoinCountPanel coinPanel = new CoinCountPanel(ProfileManager.getLastProfile().getCoins());

    // Tutorial's buttons
    private final CustomButton tutorialButton;


    // DIALOGS
    private final LevelInfoDialog levelInfoDialog;


    public LevelsMap() {
        // Initialize the array of level buttons
        levelButtons = new LevelButton[numLevels];
        // Default level dialog
        levelInfoDialog = new LevelInfoDialog(1);
        levelInfoDialog.setupKeyBindings();
        // COIN COUNT PANEL //
        add(coinPanel);

        // The tutorial's buttons
        tutorialButton = new CustomButton("Tutorial", Color.BLACK, new Color(255, 235, 59)); // Soft Yellow
        add(tutorialButton);

        tutorialButton.addActionListener(e -> showTutorialDialog());

        // Create each level buttons and load its corresponding level data
        for (int i = 1; i <= numLevels; i++) {
            LevelButton button = new LevelButton(i);

            // Set coins collected status for the buttons (used to update its display)
            button.setStarsCollected(ProfileManager.getLastProfile().getLevelStarsCount(i));

            // Store buttons in the array and add to the panel
            levelButtons[i - 1] = button;
            add(button);
        }


        // Use absolute positioning to allow precise control over buttons placement
        setLayout(null);
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
    }

    /** Position components dynamically based on the current size of the panel*/
    private void positionButtons() {
        int w = getWidth();   // Current width of the panel
        int h = getHeight();  // Current height of the panel

        // Tutorial buttons
        int tutBtnWidth = (int)(w * 0.2);  // 20% of width
        int tutBtnHeight = (int)(h * 0.08); // 8% of height
        int tutBtnX = (int)(w * 0.77);      // margin from right
        int tutBtnY = (int)(h * 0.05);
        tutorialButton.setBounds(tutBtnX, tutBtnY, tutBtnWidth, tutBtnHeight);
        tutorialButton.setFont(new Font("Arial", Font.BOLD, Math.max(12, tutBtnHeight / 3)));  // scale font

        int posX = 0, posY = 0;

        // Define the relative positions of each buttons (customized per level)
        for (int i = 0; i < numLevels; i++) {
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

            // Calculate buttons size as 8% of the smaller panel dimension (width or height)
            double DimButton = (Math.min(w, h)) * 0.08;

            // Set the bounds of the buttons: x, y position and width & height size
            levelButtons[i].setBounds(posX, posY, (int) DimButton, (int) DimButton);

            // COIN COUNT PANEL //
            int coinPanelWidth = coinPanel.getPreferredSize().width;
            int coinPanelHeight = coinPanel.getPreferredSize().height;
            int coinPanelX = (int)(getWidth() * 0.02);  // 2% from left edge
            int coinPanelY = (int)(getHeight() * 0.05);  // 5% from top
            coinPanel.setBounds(coinPanelX, coinPanelY, coinPanelWidth, coinPanelHeight);
        }
    }

    /**
     * Update icons and counter when panel becomes visible.
     */
    public void refreshLevelButtons() {
        for (int i = 0; i < numLevels; i++) {
            Boolean[] stars = ProfileManager.getLastProfile().getLevelStarsCount(i + 1);
            levelButtons[i].setStarsCollected(stars);
            levelButtons[i].repaint(); // Force repaint buttons
        }
//        coinPanel.setText(String.valueOf(ProfileManager.getLastProfile().getCoins()));
        repaint(); // Also repaint the panel and background
    }

// ----------------------------------------OVERRIDE METHODS-------------------------------------------------------------

    // Override paintComponent to draw the scaled background image behind the buttons
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage == null) {
            backgroundImage = Resources.getBestImage("/imgs/panels/levels/levelMenu.jpg", getWidth(), getHeight());
        }
        // Draw the background image stretched to fill the entire panel
        g.drawImage(backgroundImage, 0, 0, this);

        positionButtons();
    }

//------------------------------------------CONTROLLER RELATED METHODS---------------------------------------------

    @Override
    protected void bindControllers() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e){
                // Refresh level buttons when the panel is shown
                refreshLevelButtons();
            }
        });
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
    public void showLevelDialog(int levelIndex) {
        // UPDATE LEVEL INFO DIALOG //
        levelInfoDialog.updateLevelIndex(levelIndex);

        // Retrieve the top-level window (e.g., JFrame) that contains this panel
        Window parentWindow = SwingUtilities.getWindowAncestor(this);

        if (parentWindow != null) {
            // Get the dimensions of the parent window to calculate proportional dialog size
            Dimension parentSize = parentWindow.getSize();
            int newWidth = parentSize.width / 2;
            int newHeight = parentSize.height / 2;
            // Set the preferred size of the level panel to be displayed in the dialog
            levelInfoDialog.setPreferredSize(new Dimension(newWidth, newHeight));
        }


        // Create a modal dialog (blocks interaction with other windows while open)
        JDialog dialog = new JDialog(parentWindow);
        dialog.setUndecorated(true);  // Remove window borders and title bar
        dialog.setModal(true);        // Make dialog modal
        dialog.setResizable(false);   // Disable resizing by the user

        // Add the level panel to the dialog and adjust dialog size
        dialog.setContentPane(levelInfoDialog);
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


    public void hideInfoDialog() {
        if (levelInfoDialog.isVisible()) {
            levelInfoDialog.setVisible(false);
        }
    }

    @Override
    public void addNotify(){
        refreshLevelButtons();
    }
}
