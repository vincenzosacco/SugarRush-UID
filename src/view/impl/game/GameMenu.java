package view.impl.game;

import controller.game.GameMenuController;
import view.impl._common.buttons.*;
import view.impl.home.levelsMap.LevelInfoDialog;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

/**
 * Panel for game settings
 */
public class GameMenu extends LevelInfoDialog {
//    private final LevelInfoDialog levelInfoDialog;

    // Buttons for closing and starting the level
    //private final RoundCloseButton closeButton;
//    private final CustomLogoButton playButton;
    private CustomLogoButton restartButton;
    private CustomRoundLogoButton settingsButton;
    private CustomButton exitButton;

//    private int currentLevel=1; // Current level number, cannot be < 1 or > ModelConfig.NUM_LEVELS


//    public void setCurrentLevel(int currentLevel) {
//        this.currentLevel = currentLevel;
//    }

    public GameMenu(int currentLevel){
        super(currentLevel);

        // LEVEL INFO DIALOG//

//        add(levelInfoDialog);

//        GameMenuController controller=new GameMenuController(this);

        // Create and configure the play buttons
//        playButton = new CustomLogoButton("play",new Color(50, 205, 50));
//        playButton.addActionListener(e -> {
//            controller.onResume();
//        });
//        // Create and configure the restart buttons
//
//        // Create and configure the EXIT buttons

//
        // BUTTONS
        // not needed anymore
        if (closeButton != null && closeButton.getParent() != null){
            closeButton.getParent().remove(closeButton);
        }

        // --------------------- TOP PANEL ---------------------

////        JPanel topPanel = new JPanel(new GridLayout(1, 3)); // 1 row, 3 columns
////        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // 10 pixels of space at the top
////        topPanel.setOpaque(false);
//
//        // Left placeholder (empty panel)
//        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
//        leftPanel.setOpaque(false);
     //        topPanel.add(leftPanel);
//
//        // Centered level label
//        JLabel levelLabel = new JLabel("Level " + currentLevel, SwingConstants.CENTER);
//        levelLabel.setForeground(Color.BLACK);
//        topPanel.add(levelLabel);
//
//        // Right placeholder (empty panel)
//        JPanel rightPanel = new JPanel();
//        rightPanel.setOpaque(false);
//        topPanel.add(rightPanel);
//
//        add(topPanel, BorderLayout.PAGE_START);
//
//
//        // --------------------- CENTER PANEL ---------------------
//
//        JPanel centerPanel = new JPanel();
//        centerPanel.setOpaque(false);
//        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS)); // Vertical layout
//
//        // For each coin (3 total), create a row with an icon and a description
//        for (int i = 0; i < 3; i++) {
//            JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//            rowPanel.setOpaque(false);
//
//            // Choose correct image based on coin collected or not
//            String imgPath = coinsCollected[i] ? "/imgs/icons/star.jpg" : "/imgs/panels/levels/missingStar.jpg";
//            try {
//                originalImages[i] = ImageIO.read(Objects.requireNonNull(getClass().getResource(imgPath)));
//            } catch (Exception e) {
//                e.printStackTrace();
//                originalImages[i] = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB); // Fallback
//            }
//
//            iconLabels[i] = new JLabel();
//            rowPanel.add(iconLabels[i]);
//
//            // Text description for each coin objective
//            JTextArea textArea = new JTextArea(textRequest[i]);
//            textArea.setFont(new Font("Arial", Font.PLAIN, 14));
//            textArea.setForeground(coinsCollected[i] ? new Color(76, 175, 80) : new Color(220, 53, 69));
//            textArea.setBackground(new Color(255, 255, 255, 220)); // Semi-transparent white
//            // Enables automatic line wrapping when text exceeds the width of the text area.
//            textArea.setLineWrap(true);
//            // Ensures lines wrap at word boundaries (not in the middle of a word), for better readability.
//            textArea.setWrapStyleWord(true);
//            textArea.setEditable(false);
//            // Prevents the text area from receiving focus (e.g., when tabbing through components).
//            textArea.setFocusable(false);
//            // Makes the background of the text area visible (not transparent).
//            textArea.setOpaque(true);
//            // Adds empty padding around the text (top, left, bottom, right) for visual spacing.
//            textArea.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
//
//            textLabels[i] = textArea;
//            rowPanel.add(textArea);
//            centerPanel.add(rowPanel);
//        }
//
//        add(centerPanel, BorderLayout.CENTER);
//
//
//        // --------------------- BOTTOM PANEL ---------------------
//
//        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
//        bottomPanel.setOpaque(false);
//        bottomPanel.add(exitButton);
//        bottomPanel.add(playButton);
//
//        add(bottomPanel, BorderLayout.PAGE_END);
//
//
//        // --------------------- RESIZE LISTENER ---------------------
//
//        this.addComponentListener(new ComponentAdapter() {
//            @Override
//            public void componentResized(ComponentEvent e) {
//                applyScalingBasedOnCurrentDimensions();
//            }
//        });
//
//        // Force an initial resize event to apply layout immediately
//        SwingUtilities.invokeLater(this::applyScalingBasedOnCurrentDimensions);
//
//
//        this.setFocusable(true);
//        this.setVisible(false);
    }

//    public void updateContent(){
//        String mapResourcePath;
//        switch (currentLevel) {
//            case 1: mapResourcePath = MapParser.MAP_1; break;
//            case 2: mapResourcePath = MapParser.MAP_2; break;
//            case 3: mapResourcePath = MapParser.MAP_3; break;
//            case 4: mapResourcePath = MapParser.MAP_4; break;
//            case 5: mapResourcePath = MapParser.MAP_5; break;
//            case 6: mapResourcePath = MapParser.MAP_6; break;
//            default:
//                System.err.println("Livello non supportato: " + currentLevel + ". Usando map1.txt come fallback.");
//                mapResourcePath = MapParser.MAP_1;
//                break;
//        }
//        InputStream levelFileStream = null;
//        LevelData levelData = null;
//        try {
//            // Get a new InputStream for LevelData
//            levelFileStream = getClass().getResourceAsStream(mapResourcePath);
//            if (levelFileStream == null) {
//                System.err.println("ERROR: LevelData resource file NOT found in classpath: " + mapResourcePath);
//                levelData = new LevelData(null); //To enable fallback handling in the constructor
//            } else {
//                levelData = new LevelData(levelFileStream);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//
//        if (levelData == null) {
//            System.err.println("Unable to load LevelData for level " + currentLevel);
//            Boolean[] fallbackCoins = new Boolean[3];
//            String[] fallbackTexts = {"Error loading.", "Error loading.", "Error loading."};
//            updateLabels(currentLevel, fallbackCoins, fallbackTexts);
//            return;
//        }
//
//        Boolean[] coinsCollected = ProfileManager.getLastProfile().getLevelStarsCount(currentLevel);
//        String[] textRequest = levelData.getTextRequest();
//
//        updateLabels(currentLevel, coinsCollected, textRequest);
//
//        revalidate();
//        repaint();
//    }
//
//    // Helper method to update panels (text and icons)
//    private void updateLabels(int level, Boolean[] coinsCollected, String[] textRequest) {
//        // Update the level label
//        JLabel levelLabel = (JLabel) ((JPanel) getComponent(0)).getComponent(1); // Access the level label in the topPanel
//        levelLabel.setText("Level " + level);
//
//        // Update coin icons and texts
//        for (int i = 0; i < 3; i++) {
//            String imgPath;
//            if(coinsCollected[i]){
//                imgPath = "/imgs/icons/star.jpg";
//            }else{
//                imgPath = "/imgs/panels/levels/missingStar.jpg";
//            }
//
//            try {
//                // Upload the original image. applyScalingBasedOnCurrentDimensions will scale it.
//                originalImages[i] = ImageIO.read(Objects.requireNonNull(getClass().getResource(imgPath)));
//            } catch (Exception e) {
//                e.printStackTrace();
//                originalImages[i] = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB); // Fallback
//            }
//
//            iconLabels[i].setForeground(coinsCollected[i] ? new Color(76, 175, 80) : new Color(220, 53, 69)); // Color the icon for consistency
//
//            textLabels[i].setText(textRequest[i]);
//            textLabels[i].setForeground(coinsCollected[i] ? new Color(76, 175, 80) : new Color(220, 53, 69));
//        }
//        applyScalingBasedOnCurrentDimensions();
//    }
//
//    private void applyScalingBasedOnCurrentDimensions() {
//        int panelWidth = getWidth();
//        int panelHeight = getHeight();
//
//        // Resize the layer label font
//        JLabel levelLabel = (JLabel) ((JPanel) getComponent(0)).getComponent(1);
//        int dimensionFontSizeLevel = Math.min(panelWidth / 15, panelHeight / 15);
//        int levelFontSize = Math.max(12, dimensionFontSizeLevel);
//        levelLabel.setFont(new Font("Arial", Font.BOLD, levelFontSize));
//        levelLabel.revalidate();
//        levelLabel.repaint();
//
//        // Resize coin icons and text areas
//        int dimIconSize = Math.min(panelHeight / 6, panelWidth / 6);
//        int dimFontSize = Math.min(panelHeight / 20, panelWidth / 20);
//        int iconSize = Math.max(30, dimIconSize);
//        int fontSize = Math.max(12, dimFontSize);
//
//        for (int i = 0; i < 3; i++) {
//            if (originalImages[i] != null) {
//                Image scaled = originalImages[i].getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
//                iconLabels[i].setIcon(new ImageIcon(scaled));
//            } else {
//                // Fallback
//                iconLabels[i].setIcon(new ImageIcon(new BufferedImage(iconSize, iconSize, BufferedImage.TYPE_INT_ARGB)));
//            }
//
//            textLabels[i].setFont(new Font("Arial", Font.PLAIN, fontSize));
//
//            // Resize the text area to fit the icon
//            int textWidth = (panelWidth - iconSize) * 9 / 10;
//            textLabels[i].setPreferredSize(new Dimension(textWidth, fontSize * 4));
//            textLabels[i].revalidate();
//            textLabels[i].repaint();
//        }
//
//        int size = Math.min(getWidth(), getHeight()) / 10;
//        size = Math.max(size, 30);
//
//        // Resize the buttons
//        settingsButton.setPreferredSize(new Dimension(size * 2, size));
//        settingsButton.revalidate();
//        settingsButton.repaint();

//        playButton.setPreferredSize(new Dimension(size * 2, size));
//        playButton.revalidate();
//        playButton.repaint();
//
//        exitButton.setPreferredSize(new Dimension(size * 2, size));
//        exitButton.revalidate();
//        exitButton.repaint();
//
//        restartButton.setPreferredSize(new Dimension(size * 2, size));
//        restartButton.revalidate();
//        restartButton.repaint();
//
//        revalidate(); // Revalidate the entire panel after all changes
//        repaint();    // Redraw the entire panel
//    }


// ----------------------------------------SWING's METHODS-------------------------------------------------------------

    // Custom painting to render the rounded background and border
//    @Override
//    protected void paintComponent(Graphics g) {
//
////        Graphics2D g2 = (Graphics2D) g.create();
////
////        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
////
////        int width = getWidth();
////        int height = getHeight();
////        int arc = 30;
////
////        // Rounded rectangle clipping for a smooth shape
////        RoundRectangle2D panelShape = new RoundRectangle2D.Float(0, 0, width, height, arc, arc);
////        // Save the original clip of the graphic context
////        Shape originalClip = g2.getClip();
////
////        g2.setClip(panelShape);
////
////        // Draw background image or fallback to white
////        if (backgroundImage != null) {
////            g2.drawImage(backgroundImage, 0, 0, width, height, this);
////        } else {
////            g2.setColor(Color.WHITE);
////            g2.fillRect(0, 0, width, height);
////        }
////
////        // Draw rounded border
////        //Restore the original clip BEFORE drawing the border and child components
////        //to prevent the edge and child components from being cut by the rounded clip
////        g2.setClip(originalClip);
////
////        float borderWidth = 2.0f;
////        g2.setStroke(new BasicStroke(borderWidth));
////        g2.setColor(Color.BLACK);
////
////        // Draw the border slightly indented to keep it within the bounds of the panel.
////        RoundRectangle2D borderOutline = new RoundRectangle2D.Float(
////                borderWidth / 2, borderWidth / 2,
////                width - borderWidth, height - borderWidth,
////                arc, arc
////        );
////        g2.draw(borderOutline);
////
////        g2.dispose();
////
////        // Paint child components
//        super.paintComponent(g);
//    }

    //--------------------------------- PARENT METHODS ---------------------------------------------------------------------------

    @Override
    protected void buildBottomArea(){
//        super.buildBottomArea(); dont call this. i need to override buttons position
        restartButton = new CustomLogoButton("restart",new Color(255, 193, 7));
        exitButton =new CustomButton("EXIT",Color.WHITE,new Color(220, 53, 69));

        bottomArea.setLayout(new GridLayout(1, 3));

        bottomArea.setOpaque(false);
        bottomArea.add(exitButton);
        bottomArea.add(playButton); // play button from parent
        bottomArea.add(restartButton);

    }

    @Override
    protected void buildTopArea(){
        super.buildTopArea();
        topArea.removeAll(); // remove all to re-add components in right order (GridLayout)

        settingsButton=new CustomRoundLogoButton("settings",new Color(119, 136, 153));

        JPanel topLeftArea = new JPanel(new BorderLayout());
        topLeftArea.add(settingsButton, BorderLayout.WEST);
        topLeftArea.setOpaque(false);

        topArea.add(topLeftArea);
        topArea.add(levelLabel);

    }

    @Override
    protected void resizeComponents(){
        try {
            setPreferredSize(new Dimension(getParent().getWidth()/2, getParent().getWidth()/2));
        }
        catch (NullPointerException e){
            System.err.println("Parent of GameMenu is null.");
        }

        // BUTTONS //
        int size = Math.min(getWidth(), getHeight()) / 10;
        size = Math.max(size, 30);

        // Resize the buttons
        settingsButton.setPreferredSize(new Dimension(size * 2, size));
        restartButton.setPreferredSize(new Dimension(size * 2, size));

        // set gap between bottom buttons
        try {
            GridLayout layout = (GridLayout) bottomArea.getLayout();
            layout.setVgap(size);
        }
        catch (ClassCastException e){
            throw new AssertionError("Bottom area layout is not a GridLayout.");
        }

        super.resizeComponents();// <-- calls repaint() and revalidate()
    }

    //--------------------------------- CONTROLLER -----------------------------------------------------------------------------------
    @Override
    protected void bindControllers(){
//        super.bindControllers(); dont call this. i need to override buttons behavior
        GameMenuController controller = new GameMenuController();
        playButton.addActionListener(controller::onResume);
        settingsButton.addActionListener(controller::onSettings);
        restartButton.addActionListener(controller::onRestart);
//        exitButton.addActionListener(controller::onExit);
    }

}
