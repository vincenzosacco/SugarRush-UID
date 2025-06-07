package view.menu;

import controller.ControllerObj;
import controller.GameLoop;
import model.Model;
import model.game.LevelData;
import model.game.MapParser;
import view.View;
import view.ViewComp;
import view.button.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

import static config.ViewConfig.BOARD_HEIGHT;
import static config.ViewConfig.BOARD_WIDTH;

/**
 * Panel for game settings
 */
public class GameMenuPanel extends JPanel implements ViewComp {

    // Background image for the panel
    private Image backgroundImage;

    // Buttons for closing and starting the level
    //private final RoundCloseButton closeButton;
    private final RoundPlayButton playButton;
    private final RestartButton restartButton;
    private final SettingsButton settingsButton;
    private final ExitButton exitButton;

    // Arrays to store coin icons and corresponding text
    private final JLabel[] iconLabels = new JLabel[3];
    private final JTextArea[] textLabels = new JTextArea[3];

    // Original images for resizing
    private final Image[] originalImages = new Image[3];

    private int currentLevel;

    private boolean open = false;

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public GameMenuPanel(){
        setPreferredSize(new Dimension(BOARD_WIDTH/2, BOARD_HEIGHT/2));
        setOpaque(false);

        String mapResourcePath = "/map" + currentLevel + ".txt";
        InputStream levelFile=getClass().getResourceAsStream(mapResourcePath);


        // Load level data (coin status and descriptive text)
        LevelData levelData = new LevelData(levelFile);
        boolean[] coinsCollected = levelData.getCoinsCollected();
        String[] textRequest = levelData.getTextRequest();

        // Use BorderLayout and transparency
        setLayout(new BorderLayout());

        // Load the background image
        try {
            // Gets the resource URL from the classpath.
            URL imageUrl = getClass().getResource("/imgs/panels/levels/level-dialog.jpg");

            if (imageUrl == null) {
                System.err.println("Error: Image resource not found in classpath: /resources/imgs/panels/levels/level-dialog.jpg");
            } else {
                backgroundImage = ImageIO.read(imageUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading image level-dialog.jpg: " + e.getMessage());
        }


        // Create and configure the play button
        playButton = new RoundPlayButton();
        playButton.addActionListener(e -> {
            View.getInstance().getGamePanel().getPauseButton().setEnabled(true);
            open=false;
            this.setVisible(false);
            View.getInstance().getGamePanel().startGameTimer();
            GameLoop.getInstance().start();
            View.getInstance().getGamePanel().requestFocusInWindow();
        });
        // Create and configure the restart button
        restartButton=new RestartButton();
        restartButton.addActionListener(e -> {
            View.getInstance().getGamePanel().getPauseButton().setEnabled(true);
            int levelToRestart = Model.getInstance().getGame().getCurrLevel();
            open=false;
            this.setVisible(false);
//            Start the level showing the GamePanel
            View.getInstance().getGamePanel().endGame();
            View.getInstance().getGamePanel().resetGameTimer();
            View.getInstance().getGamePanel().resetPanelForNewLevel();
            Model.getInstance().getGame().setLevel(levelToRestart);
            View.getInstance().showPanel(View.PanelName.GAME.getName());
            View.getInstance().getGamePanel().startGameTimer();
            GameLoop.getInstance().start();
        });

        // Create and configure the EXIT button
        exitButton=new ExitButton();
        exitButton.addActionListener(e ->{
            open=false;
            this.setVisible(false);
            GameLoop.getInstance().stop();
            View.getInstance().getGamePanel().resetGameTimer();
            Model.getInstance().getGame().clearGameMatrix();
            View.getInstance().showPanel(View.PanelName.CUSTOM_TABBED_PANE.getName());
        });

        // Create and configure the settings button
        settingsButton=new SettingsButton();
        settingsButton.addActionListener(e ->{
            open=false;
            this.setVisible(false);
            View.getInstance().showPanel(View.PanelName.SETTINGS.getName());
        });



        // --------------------- TOP PANEL ---------------------

        JPanel topPanel = new JPanel(new GridLayout(1, 3)); // 1 row, 3 columns
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // 10 pixels of space at the top
        topPanel.setOpaque(false);

        // Left placeholder (empty panel)
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setOpaque(false);
        leftPanel.add(settingsButton);
        topPanel.add(leftPanel);

        // Centered level label
        JLabel levelLabel = new JLabel("Level " + currentLevel, SwingConstants.CENTER);
        levelLabel.setForeground(Color.BLACK);
        topPanel.add(levelLabel);

        // Right placeholder (empty panel)
        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        topPanel.add(rightPanel);


        add(topPanel, BorderLayout.PAGE_START);

        // --------------------- CENTER PANEL ---------------------

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS)); // Vertical layout

        // For each coin (3 total), create a row with an icon and a description
        for (int i = 0; i < 3; i++) {
            JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            rowPanel.setOpaque(false);

            // Choose correct image based on coin collected or not
            String imgPath = coinsCollected[i] ? "/imgs/panels/levels/coin.jpg" : "/imgs/panels/levels/missingCoin.jpg";
            try {
                originalImages[i] = ImageIO.read(getClass().getResource(imgPath));
            } catch (Exception e) {
                e.printStackTrace();
                originalImages[i] = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB); // Fallback
            }

            iconLabels[i] = new JLabel();
            rowPanel.add(iconLabels[i]);

            // Text description for each coin objective
            JTextArea textArea = new JTextArea(textRequest[i]);
            textArea.setFont(new Font("Arial", Font.PLAIN, 14));
            textArea.setForeground(coinsCollected[i] ? Color.GREEN : Color.RED);
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
            centerPanel.add(rowPanel);
        }

        add(centerPanel, BorderLayout.CENTER);

        // --------------------- BOTTOM PANEL ---------------------

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.add(exitButton);
        bottomPanel.add(restartButton);
        bottomPanel.add(playButton);

        add(bottomPanel, BorderLayout.PAGE_END);


        // --------------------- RESIZE LISTENER ---------------------

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                applyScalingBasedOnCurrentDimensions();
            }
        });

        // Force an initial resize event to apply layout immediately
        SwingUtilities.invokeLater(() -> {
            applyScalingBasedOnCurrentDimensions();
        });


        this.setFocusable(true);
        this.setVisible(false);

        // --- KEY BINDINGS ---
        setupKeyBindings();
    }


    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    // Custom painting to render the rounded background and border
    @Override
    protected void paintComponent(Graphics g) {
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

        // Draw background image or fallback to white
        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, width, height, this);
        } else {
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, width, height);
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

    public void updateContent(){
        String mapResourcePath;
        switch (currentLevel) {
            case 1: mapResourcePath = MapParser.MAP_1; break;
            case 2: mapResourcePath = MapParser.MAP_2; break;
            case 3: mapResourcePath = MapParser.MAP_3; break;
            case 4: mapResourcePath = MapParser.MAP_4; break;
            case 5: mapResourcePath = MapParser.MAP_5; break;
            case 6: mapResourcePath = MapParser.MAP_6; break;
            default:
                System.err.println("Livello non supportato: " + currentLevel + ". Usando map1.txt come fallback.");
                mapResourcePath = MapParser.MAP_1;
                break;
        }
        InputStream levelFileStream = null;
        LevelData levelData = null;
        try {
            // Get a new InputStream for LevelData
            levelFileStream = getClass().getResourceAsStream(mapResourcePath);
            if (levelFileStream == null) {
                System.err.println("ERROR: LevelData resource file NOT found in classpath: " + mapResourcePath);
                levelData = new LevelData(null); //To enable fallback handling in the constructor
            } else {
                levelData = new LevelData(levelFileStream);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        if (levelData == null) {
            System.err.println("Unable to load LevelData for level " + currentLevel);
            boolean[] fallbackCoins = new boolean[3];
            String[] fallbackTexts = {"Error loading.", "Error loading.", "Error loading."};
            updateLabels(currentLevel, fallbackCoins, fallbackTexts);
            return;
        }

        boolean[] coinsCollected = levelData.getCoinsCollected();
        String[] textRequest = levelData.getTextRequest();

        updateLabels(currentLevel, coinsCollected, textRequest);

        revalidate();
        repaint();
    }

    // Helper method to update labels (text and icons)
    private void updateLabels(int level, boolean[] coinsCollected, String[] textRequest) {
        // Update the level label
        JLabel levelLabel = (JLabel) ((JPanel) getComponent(0)).getComponent(1); // Access the level label in the topPanel
        levelLabel.setText("Level " + level);

        // Update coin icons and texts
        for (int i = 0; i < 3; i++) {
            String imgPath;
            if(coinsCollected[i]){
                imgPath = "/imgs/panels/levels/coin.jpg";
            }else{
                imgPath = "/imgs/panels/levels/missingCoin.jpg";
            }

            try {
                // Upload the original image. applyScalingBasedOnCurrentDimensions will scale it.
                originalImages[i] = ImageIO.read(Objects.requireNonNull(getClass().getResource(imgPath)));
            } catch (Exception e) {
                e.printStackTrace();
                originalImages[i] = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB); // Fallback
            }

            iconLabels[i].setForeground(coinsCollected[i] ? Color.GREEN : Color.RED); // Colora l'icona per coerenza

            textLabels[i].setText(textRequest[i]);
            textLabels[i].setForeground(coinsCollected[i] ? Color.GREEN : Color.RED);
        }
        applyScalingBasedOnCurrentDimensions();
    }

    private void applyScalingBasedOnCurrentDimensions() {
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Resize the layer label font
        JLabel levelLabel = (JLabel) ((JPanel) getComponent(0)).getComponent(1);
        int dimensionFontSizeLevel = Math.min(panelWidth / 15, panelHeight / 15);
        int levelFontSize = Math.max(12, dimensionFontSizeLevel);
        levelLabel.setFont(new Font("Arial", Font.BOLD, levelFontSize));
        levelLabel.revalidate();
        levelLabel.repaint();

        // Resize coin icons and text areas
        int dimIconSize = Math.min(panelHeight / 6, panelWidth / 6);
        int dimFontSize = Math.min(panelHeight / 20, panelWidth / 20);
        int iconSize = Math.max(30, dimIconSize);
        int fontSize = Math.max(12, dimFontSize);

        for (int i = 0; i < 3; i++) {
            if (originalImages[i] != null) {
                Image scaled = originalImages[i].getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
                iconLabels[i].setIcon(new ImageIcon(scaled));
            } else {
                // Fallback
                iconLabels[i].setIcon(new ImageIcon(new BufferedImage(iconSize, iconSize, BufferedImage.TYPE_INT_ARGB)));
            }

            textLabels[i].setFont(new Font("Arial", Font.PLAIN, fontSize));

            // Resize the text area to fit the icon
            int textWidth = (panelWidth - iconSize) * 9 / 10;
            textLabels[i].setPreferredSize(new Dimension(textWidth, fontSize * 4));
            textLabels[i].revalidate();
            textLabels[i].repaint();
        }

        int size = Math.min(getWidth(), getHeight()) / 10;
        size = Math.max(size, 30);

        // Resize the buttons
        settingsButton.setPreferredSize(new Dimension(size * 2, size));
        settingsButton.revalidate();
        settingsButton.repaint();

        playButton.setPreferredSize(new Dimension(size * 2, size));
        playButton.revalidate();
        playButton.repaint();

        exitButton.setPreferredSize(new Dimension(size * 2, size));
        exitButton.revalidate();
        exitButton.repaint();

        restartButton.setPreferredSize(new Dimension(size * 2, size));
        restartButton.revalidate();
        restartButton.repaint();

        revalidate(); // Revalidate the entire panel after all changes
        repaint();    // Redraw the entire panel
    }

    // Method to set up key bindings
    private void setupKeyBindings() {
        // Get the InputMap for when the component is focused or one of its children is focused
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        // Get the ActionMap to associate keys with actions
        ActionMap actionMap = this.getActionMap();

        // --- Bind ENTER key to Play button ---
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

    }

    @Override
    public void bindController(ControllerObj controller) {

    }

}
