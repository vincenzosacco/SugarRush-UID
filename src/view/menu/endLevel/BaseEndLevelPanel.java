package view.menu.endLevel;

import controller.ControllerObj;
import controller.menu.endLevel.BaseEndLevelController;
import view.View;
import view.ViewComp;
import view.button.CustomButton;
import view.button.CustomLogoButton;
import view.button.CustomRoundLogoButton;

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

public abstract class BaseEndLevelPanel extends JPanel implements ViewComp {

    protected Image backgroundImage;
    protected CustomLogoButton restartButton;
    protected CustomButton exitButton;
    protected CustomRoundLogoButton settingsButton;

    protected JLabel levelLabel;
    private int currentLevel;

    // Sets the current level and updates the label if already created
    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
        if (levelLabel != null) {
            levelLabel.setText("Level " + currentLevel);
        }
    }

    public BaseEndLevelPanel() {
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
                System.err.println("Error: Image resource not found: /imgs/panels/levels/endLevelImage.jpg");
            } else {
                backgroundImage = ImageIO.read(imageUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading image imgs/panels/levels/endLevelImage.jpg: " + e.getMessage());
        }
    }

    // Initializes the control buttons and binds actions
    private void createButtons() {
        BaseEndLevelController controller = new BaseEndLevelController(this);

        restartButton = new CustomLogoButton("restart",new Color(255, 193, 7)); // Amber / Golden Yellow
        restartButton.addActionListener(e -> controller.onRestart());

        exitButton = new CustomButton("EXIT",Color.WHITE,new Color(220, 53, 69)); // Bootstrap's "Danger" Red
        exitButton.addActionListener(e -> controller.onExit());

        settingsButton = new CustomRoundLogoButton("settings",new Color(119, 136, 153)); // Light Slate Gray
        settingsButton.addActionListener(e -> View.getInstance().showPanel(View.PanelName.SETTINGS.getName()));
    }

    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

    // Sets up top and bottom layout areas
    private void setupCommonLayoutElements() {
        // --- TOP PANEL ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Left side: Settings button
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
    protected void applyScalingBasedOnCurrentDimensions() {
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        int buttonSize = Math.min(panelWidth, panelHeight) / 10;
        buttonSize = Math.max(buttonSize, 30);

        restartButton.setPreferredSize(new Dimension(buttonSize * 2, buttonSize));
        exitButton.setPreferredSize(new Dimension(buttonSize * 2, buttonSize));
        settingsButton.setPreferredSize(new Dimension(buttonSize * 2, buttonSize));

        if (levelLabel != null) {
            int levelFontSize = Math.min(getWidth() / 8, getHeight() / 8);
            levelFontSize = Math.max(25, levelFontSize);
            levelLabel.setFont(new Font("Arial", Font.BOLD, levelFontSize));
        }

        revalidate();
        repaint();
    }

    // Method to set up key bindings
    private void setupKeyBindings() {
        // Get the InputMap for when the component is focused or one of its children is focused
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        // Get the ActionMap to associate keys with actions
        ActionMap actionMap = this.getActionMap();

        // --- Bind ESCAPE key to Exit button ---
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

    @Override
    public void bindController(ControllerObj controller) {
        // Optional override if needed
    }
}
