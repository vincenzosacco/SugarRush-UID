package view.impl.game.dialogs;

import model.game.Game;
import view.impl._common.buttons.CustomButton;
import view.impl._common.buttons.CustomLogoButton;
import view.impl._common.buttons.CustomRoundLogoButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.net.URL;

import static config.ViewConfig.BOARD_HEIGHT;
import static config.ViewConfig.BOARD_WIDTH;

public abstract class _BaseEndLevelDialog extends JPanel {

    protected Image backgroundImage;
    protected CustomLogoButton restartButton;
    protected CustomButton exitButton;
    protected CustomRoundLogoButton settingsButton;

    protected JLabel levelLabel;
    /** Label for the win/lose message*/
    protected JLabel messageLabel;

    /** Label to show the elapsed time */
    protected JLabel timerLabel;

    private int currentLevel;

    // Sets the current level and updates the label if already created
    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
        if (levelLabel != null) {
            levelLabel.setText("Level " + currentLevel);
        }
    }

    public _BaseEndLevelDialog() {
        setPreferredSize(new Dimension(BOARD_WIDTH / 2, BOARD_HEIGHT / 2));
        setOpaque(false);
        setLayout(new BorderLayout());

        loadBackgroundImage(); // Load panel background
        createButtons();       // Create common control buttons
        setupCommonLayoutElements(); // Set layout without center panel

        // Dynamically scale UI elements when resized
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                applyScalingBasedOnCurrentDimensions();
            }
        });

        setupKeyBindings();

        SwingUtilities.invokeLater(this::applyScalingBasedOnCurrentDimensions);

        this.setFocusable(true);
        this.setVisible(false); // Hidden by default
    }

    // Loads the background image from resources
    private void loadBackgroundImage() {
        try {
            URL imageUrl = getClass().getResource("/imgs/panels/levels/endLevelImage.jpg");
            if (imageUrl == null) {
                System.err.println("Error: Image resource not found: /imgs/panels/levelsMap/endLevelImage.jpg");
            } else {
                backgroundImage = ImageIO.read(imageUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading image imgs/panels/levelsMap/endLevelImage.jpg: " + e.getMessage());
        }
    }

    // Initializes the control buttons and binds actions
    private void createButtons() {

//        _BaseEndLevelController controller = new _BaseEndLevelController(this);

        restartButton = new CustomLogoButton("restart",new Color(255, 193, 7)); // Amber / Golden Yellow
//        restartButton.addActionListener(e -> controller.onRestart());

        exitButton = new CustomButton("EXIT",Color.WHITE,new Color(220, 53, 69)); // Bootstrap's "Danger" Red
//        exitButton.addActionListener(e -> controller.onExit());

        settingsButton = new CustomRoundLogoButton("settings",new Color(119, 136, 153)); // Light Slate Gray
//        settingsButton.addActionListener(e -> View.getInstance().showPanel(View.PanelName.SETTINGS.getName()));
    }

    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

    // Sets up top and bottom layout areas
    private void setupCommonLayoutElements() {
        // --- TOP PANEL ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Left side: Settings buttons
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setOpaque(false);
        leftPanel.add(settingsButton);
        topPanel.add(leftPanel, BorderLayout.LINE_START);

        // Center: Level label
        JLabel levelLabel = new JLabel("Level " + currentLevel, SwingConstants.CENTER);
        levelLabel.setForeground(Color.BLACK);
        int levelFontSize = Math.min(getWidth() / 8, getHeight() / 8);
        levelFontSize = Math.max(25, levelFontSize);
        levelLabel.setFont(new Font("Arial", Font.BOLD, levelFontSize));
        topPanel.add(levelLabel, BorderLayout.CENTER);

        timerLabel = new JLabel("Time: " + Game.getInstance().getElapsedTime() +"s",SwingConstants.CENTER);
        timerLabel.setForeground(Color.BLACK);
        timerLabel.setFont(new Font("Arial", Font.PLAIN, 24));


        // Right side: empty for spacing
        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        topPanel.add(rightPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.PAGE_START);

        // --- BOTTOM PANEL ---
        bottomPanel.setOpaque(false);
        bottomPanel.add(exitButton);
        bottomPanel.add(restartButton);
        add(bottomPanel, BorderLayout.PAGE_END);

        this.levelLabel = levelLabel; // Save reference for dynamic updates
    }

    // Abstract method to allow subclasses to define their own center panel
    protected abstract JPanel setupCenterPanel();

    // Resize and reposition components based on panel size
    public void applyScalingBasedOnCurrentDimensions() {
        if (levelLabel != null) {
            int levelFontSize = Math.min(getWidth() / 8, getHeight() / 8);
            levelFontSize = Math.max(25, levelFontSize);
            levelLabel.setFont(new Font("Arial", Font.BOLD, levelFontSize));
        }

        // Specific scaling for the defeat message
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int fontSize = Math.min(panelWidth / 8, panelHeight / 8);
        messageLabel.setFont(new Font("Arial", Font.BOLD, Math.max(24, fontSize)));
        messageLabel.revalidate();
        messageLabel.repaint(); // todo check if this is needed. Revalidate() and repaint at the end of this method should call all components revalidate and repaint

        // Specific scaling for the timer label
        int fontSizeTimer = Math.min(panelWidth / 15, panelHeight / 15);
        timerLabel.setFont(new Font("Arial", Font.PLAIN, Math.max(16, fontSizeTimer)));
        timerLabel.revalidate();
        timerLabel.repaint();

        revalidate();
        repaint();
    }

    // Method to set up key bindings
    private void setupKeyBindings() {
        // Get the InputMap for when the component is focused or one of its children is focused
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        // Get the ActionMap to associate keys with actions
        ActionMap actionMap = this.getActionMap();

        // --- Bind ESCAPE key to Exit buttons ---
        // Create an InputStroke for the ESCAPE key
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
        // Put the KeyStroke and an identifier (e.g., "pressExit") into the InputMap
        inputMap.put(escapeKeyStroke, "pressExit");
        // Associate the identifier with an AbstractAction in the ActionMap
        actionMap.put("pressExit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Programmatically trigger the action listener of the exitButton
                exitButton.doClick();
            }
        });
    }

    // ---------------------------------------- OVERRIDE METHODS -------------------------------------------------------

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int arc = 30;

        RoundRectangle2D roundedRect = new RoundRectangle2D.Float(0, 0, width, height, arc, arc);
        g2.setClip(roundedRect);

        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, width, height, this);
        } else {
            g2.setColor(Color.WHITE); // Fallback if no image
            g2.fillRect(0, 0, width, height);
        }

        g2.setClip(null);
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(Color.BLACK);
        g2.draw(roundedRect);

        g2.dispose();
        super.paintComponent(g);
    }

    // Dynamically updates the level label with a new level
    public void updateLabels(int level) {
        JLabel levelLabel = (JLabel) ((JPanel) getComponent(0)).getComponent(1);
        levelLabel.setText("Level " + level);
        applyScalingBasedOnCurrentDimensions();
    }

}
