package view.impl.home.levelsMap;

import controller.menu.LevelInfoController;
import model.game.LevelData;
import persistance.profile.ProfileManager;
import utils.Resources;
import view.base.BaseDialog;
import view.impl._common.buttons.CustomLogoButton;
import view.impl._common.buttons.RoundCloseButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class LevelInfoDialog extends BaseDialog {

    // Arrays to store coin icons and corresponding text
    private final JLabel[] iconLabels = new JLabel[3];
    private final JTextArea[] textLabels = new JTextArea[3];
    protected final JLabel levelLabel = new JLabel();
    // Original images for resizing
    private final Image[] originalImages = new Image[3];

    // BORDER LAYOUT AREAS
    protected final JPanel bottomArea = new JPanel();
    protected final JPanel centerArea = new JPanel();
    protected final JPanel topArea = new JPanel();

    // Buttons for closing and starting the level
    protected final RoundCloseButton closeButton;
    protected final CustomLogoButton playButton = new CustomLogoButton("play", new Color(50, 205, 50)); // Lime green;

    // CONTROLLER
    private final LevelInfoController controller;
    private int levelIndex;

    public LevelInfoDialog(int levelIndex){
        super();
        controller = new LevelInfoController(this, levelIndex);
        this.levelIndex = levelIndex;

        // Use BorderLayout and transparency
        setLayout(new BorderLayout());

        // Create and configure the close buttons
        closeButton = new RoundCloseButton();

        // --------------------- TOP PANEL ---------------------
        buildTopArea();
        add(topArea, BorderLayout.PAGE_START);

        // --------------------- CENTER PANEL ---------------------
        buildCenterArea();
        add(centerArea, BorderLayout.CENTER);

        // --------------------- BOTTOM PANEL ---------------------
        buildBottomArea();
        add(bottomArea, BorderLayout.PAGE_END);




        // --- COMPONENT RESIZING ---
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                resizeComponents();
            }
        });
    }

    //---------------------------------- CONTRUCTOR METHODS ---------------------------------------------------------

    /**
     * Builds the top area of the game menu updating {@link #topArea}.
     * @apiNote This method is called by {@link #LevelInfoDialog(int)}
     */
    protected void buildTopArea(){
        // --------------------- TOP PANEL ---------------------
        topArea.setLayout(new GridLayout(1, 3)); // 1 row, 3 columns
        topArea.setOpaque(false);
        topArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        topArea.add(Box.createHorizontalGlue());


        // Centered level label
        levelLabel.setText("Level " + levelIndex);
        levelLabel.setHorizontalAlignment(SwingConstants.CENTER);
        levelLabel.setForeground(Color.BLACK);

        int levelFontSize = Math.min(getWidth() / 15, getHeight() / 15);
        levelFontSize = Math.max(15, levelFontSize);
        levelLabel.setFont(new Font("Arial", Font.BOLD, levelFontSize));

        topArea.add(levelLabel);

        // Right panel with close buttons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(closeButton);
        topArea.add(rightPanel);

    }

    /**
     * Builds the center area of the game menu updating {@link #centerArea}. .
     * @apiNote This method is called by {@link #LevelInfoDialog(int)}
     */
    protected void buildCenterArea(){
        // Load level data (coin status and descriptive text)
        LevelData levelData = new LevelData(Resources.getResourceAsStream("/maps/map"+ levelIndex +".txt"));
        Boolean[] coinsCollected = ProfileManager.getLastProfile().getLevelStarsCount(levelIndex);
        String[] textRequest = levelData.getTextRequest();


        centerArea.setOpaque(false);
        centerArea.setLayout(new BoxLayout(centerArea, BoxLayout.Y_AXIS)); // Vertical layout

        // For each coin (3 total), create a row with an icon and a description
        for (int i = 0; i < 3; i++) {
            JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            rowPanel.setOpaque(false);

            // Choose correct image based on coin collected or not
            String imgPath = coinsCollected[i] ? "/imgs/icons/star.jpg" : "/imgs/panels/levels/missingStar.jpg";
            try {
                // Use ClassLoader for robustness
                URL coinImageUrl = getClass().getResource(imgPath); // Corrected path

                if (coinImageUrl == null) {
                    System.err.println("Error: Coin image resource not found: " + imgPath);
                    originalImages[i] = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB); // Fallback image
                } else {
                    originalImages[i] = ImageIO.read(coinImageUrl);
                }
            } catch (IOException e) { // Catch IOException specifically for ImageIO.read
                e.printStackTrace();
                originalImages[i] = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB); // Fallback
            }

            // Scaled image icon
            ImageIcon icon = new ImageIcon(originalImages[i].getScaledInstance(40, 40, Image.SCALE_SMOOTH));
            iconLabels[i] = new JLabel(icon);
            rowPanel.add(iconLabels[i]);

            // Text description for each coin objective
            JTextArea textArea = new JTextArea(textRequest[i]);
            textArea.setFont(new Font("Arial", Font.PLAIN, 14));
            textArea.setForeground(coinsCollected[i] ? new Color(76, 175, 80) : new Color(220, 53, 69));
            textArea.setBackground(new Color(255, 255, 255, 220)); // Semi-transparent white
            // Enables automatic line wrapping when text exceeds the width of the text area.
            textArea.setLineWrap(true);
            // Ensures lines wrap at word boundaries (not in the middle of a word), for better readability.
            textArea.setWrapStyleWord(true);
            textArea.setEditable(false);
            // Prevents the text area from receiving focus (e.g., when tabbing through components).
            textArea.setFocusable(false);
            // Makes the background of the text area visible (not transparent).
            textArea.setOpaque(true);
            // Adds empty padding around the text (top, left, bottom, right) for visual spacing.
            textArea.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));

            textLabels[i] = textArea;
            rowPanel.add(textArea);
            centerArea.add(rowPanel);
        }

    }

    /**
     * Builds the bottom area of the game menu updating {@link #bottomArea}.
     * @apiNote This method is called by {@link #LevelInfoDialog(int)}
     */
    protected void buildBottomArea(){
        bottomArea.setLayout(new GridLayout(1, 3));
        bottomArea.setOpaque(false);
        bottomArea.add(Box.createHorizontalGlue());
        bottomArea.add(playButton);
        bottomArea.add(Box.createHorizontalGlue());
    }


    /**
     * Add key bindings to the components in this dialog.
     */
    public void setupKeyBindings() {
        // Get the InputMap for when the component is focused or one of its children is focused
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        // Get the ActionMap to associate keys with actions
        ActionMap actionMap = this.getActionMap();

        // --- Bind ENTER key to Play buttons ---
        // Create an InputStroke for the ENTER key
        KeyStroke enterKeyStroke = KeyStroke.getKeyStroke("ENTER");
        // Put the KeyStroke and an identifier (e.g., "pressPlay") into the InputMap
        inputMap.put(enterKeyStroke, "pressPlay");
        // Associate the identifier with an AbstractAction in the ActionMap
        actionMap.put("pressPlay", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Programmatically trigger the action listener of the playButton
                playButton.doClick();
            }
        });

        // --- Bind ESCAPE key to Close buttons ---
        // Create an InputStroke for the ESCAPE key
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
        // Put the KeyStroke and an identifier (e.g., "pressClose") into the InputMap
        inputMap.put(escapeKeyStroke, "pressClose");
        // Associate the identifier with an AbstractAction in the ActionMap
        actionMap.put("pressClose", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Programmatically trigger the action listener of the closeButton
                closeButton.doClick();
            }
        });
    }


    //---------------------------------------- BEHAVIOR -------------------------------------------------------------
    // Opens a dialog window containing the CustomDialog panel
//    public void showCustomDialog(LevelDialog levelDialog) {
//        // Retrieve the top-level window (e.g., JFrame) that contains this panel
//        Window parentWindow = SwingUtilities.getWindowAncestor(this);
//
//        // Get the dimensions of the parent window to calculate proportional dialog size
//        Dimension parentSize = parentWindow.getSize();
//        int newWidth = parentSize.width;
//        int newHeight = parentSize.height/2;
//
//        // Set the preferred size of the CustomDialog panel to be displayed in the dialog
//        levelDialog.setPreferredSize(new Dimension(newWidth, newHeight));
//
//        // Create a modal dialog (blocks interaction with other windows while open)
//        JDialog dialog = new JDialog(parentWindow);
//        dialog.setUndecorated(true);  // Remove window borders and title bar
//        dialog.setModal(true);        // Make dialog modal
//        dialog.setResizable(false);   // Disable resizing by the user
//
//        // Add the CustomDialog panel to the dialog and adjust dialog size
//        dialog.getContentPane().add(levelDialog);
//        dialog.pack(); // Automatically size dialog to fit its contents
//
//        // Attempt to apply rounded corners to the dialog
//        try {
//            dialog.setShape(new RoundRectangle2D.Double(0, 0, dialog.getWidth(), dialog.getHeight(), 30, 30));
//        } catch (UnsupportedOperationException ex) {
//            System.out.println("Rounded corners not supported on this platform");
//        }
//
//        // Center the dialog relative to the parent window
//        dialog.setLocationRelativeTo(parentWindow);
//
//        // Display the dialog
//        dialog.setVisible(true); // Show dialog
//    }

    /**
     * Method to dynamically resize components based on the size of the dialog.
     * @implNote This method is called by {@link #addNotify()} and by the {@link ComponentAdapter} added to this class.
     */
    protected void resizeComponents(){
//        this.addComponentListener(new ComponentAdapter() {
//            @Override
//            public void componentResized(ComponentEvent e) {
                int panelWidth = getWidth();
                int panelHeight = getHeight();


        // Resize level label font
                int levelFontSize = Math.min(getWidth() / 12, getHeight() / 12);
                levelFontSize = Math.max(25, levelFontSize);
                levelLabel.setFont(new Font("Arial", Font.BOLD, levelFontSize));

                // Resize coin icons and text areas
                int dimIconSize = Math.min(panelHeight / 6, panelWidth / 6);
                int dimFontSize = Math.min(panelHeight / 20, panelWidth / 20);
                int iconSize = Math.max(30, dimIconSize);
                int fontSize = Math.max(12, dimFontSize);

                for (int i = 0; i < 3; i++) {
                    Image scaled = originalImages[i].getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
                    iconLabels[i].setIcon(new ImageIcon(scaled));
                    textLabels[i].setFont(new Font("Arial", Font.PLAIN, fontSize));

                    // Resize the text area to fit nicely next to the icon
                    int textWidth = (panelWidth - iconSize) * 9 / 10;
                    textLabels[i].setPreferredSize(new Dimension(textWidth, fontSize * 4));
                    textLabels[i].revalidate();
                    textLabels[i].repaint();
                }

            //--BUTTONS-- //
                // BOTTOM AREA
                int w3Percent = (int) (getWidth() * 0.03);
                int h3Percent = (int) (getHeight() * 0.03);
                bottomArea.setBorder(BorderFactory.createEmptyBorder(h3Percent, w3Percent, h3Percent, w3Percent));

                // Resize close buttons
                int size = Math.min(getWidth(), getHeight()) / 10;
                size = Math.max(size, 30);
                closeButton.setPreferredSize(new Dimension(size, size));

                // Resize play buttons
                playButton.setPreferredSize(new Dimension(size *2 , size));
                revalidate();
                repaint();
            }
//        });
//    }



//---------------------------------------- ABSTRACT PARENTs OVERRIDE --------------------------------------
    @Override
    protected BufferedImage loadBackgroundImage() {
        return Resources.getBestImage("/imgs/panels/levels/level-info-dialog-BG.jpg", getWidth(), getHeight());
    }

    @Override
    public void addNotify(){
        resizeComponents();
        super.addNotify();
    }

//---------------------------------------- CONTROLLER RELATED -----------------------------------------------------
    @Override
    protected void bindControllers(){
        playButton.addActionListener(controller::onPlay);
        closeButton.addActionListener(controller::onClose);
    }

    /**
     * Update the level index causing redrawing the entire component.
     * @param levelIndex
     */
    public void updateLevelIndex(int levelIndex) {
        this.levelIndex = levelIndex;

        controller.setLevelIndex(levelIndex);

        // reset title
        levelLabel.setText("Level " + levelIndex);

        // reset center text
        centerArea.removeAll();
        buildCenterArea();
    }

}