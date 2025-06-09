package view.menu;

import controller.ControllerObj;
import controller.GameLoop;
import model.Model;
import view.View;
import view.ViewComp;
import view.button.ExitButton;
import view.button.RestartButton;
import view.button.SettingsButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.net.URL;

import static config.ViewConfig.BOARD_HEIGHT;
import static config.ViewConfig.BOARD_WIDTH;

public abstract class BaseEndLevelPanel extends JPanel implements ViewComp {

    protected Image backgroundImage;
    protected RestartButton restartButton;
    protected ExitButton exitButton;
    protected SettingsButton settingsButton;

    protected JLabel levelLabel;

    private int currentLevel;


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

        loadBackgroundImage();
        createButtons();
        setupCommonLayoutElements();

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                applyScalingBasedOnCurrentDimensions();
            }
        });

        // Force an initial resize
        SwingUtilities.invokeLater(this::applyScalingBasedOnCurrentDimensions);

        this.setFocusable(true);
        this.setVisible(false); // Initially not visible
    }

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

    private void createButtons() {
        restartButton = new RestartButton();
        restartButton.addActionListener(e -> {
            int levelToRestart = Model.getInstance().getGame().getCurrLevel();
            this.setVisible(false);
//            Start the level showing the GamePanel
            View.getInstance().getGamePanel().endGame();
            Model.getInstance().getGame().clearGameMatrix();

            View.getInstance().getGamePanel().resetGameTimer();
            View.getInstance().getGamePanel().resetPanelForNewLevel();
            Model.getInstance().getGame().setLevel(levelToRestart);
            View.getInstance().showPanel(View.PanelName.GAME.getName());
            View.getInstance().getGamePanel().startGameTimer();
            View.getInstance().getGamePanel().getPauseButton().setEnabled(true);
            GameLoop.getInstance().start();
        });


        exitButton = new ExitButton();
        exitButton.addActionListener(e -> {
            this.setVisible(false);
            GameLoop.getInstance().stop();
            View.getInstance().getGamePanel().resetGameTimer();
            Model.getInstance().getGame().clearGameMatrix();
            View.getInstance().showPanel(View.PanelName.CUSTOM_TABBED_PANE.getName());
        });

        settingsButton=new SettingsButton();
        settingsButton.addActionListener(e ->{
            View.getInstance().showPanel(View.PanelName.SETTINGS.getName());
        });

    }
    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    // This method configures the common layout elements, but not the center panel
    private void setupCommonLayoutElements() {
        // --- TOP PANEL ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // 10 pixels of space at the top
        // Left placeholder for settings button
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setOpaque(false);
        leftPanel.add(settingsButton); // Add settings button here
        topPanel.add(leftPanel, BorderLayout.LINE_START);

        // Centered layer label
        JLabel levelLabel = new JLabel("Level " + currentLevel, SwingConstants.CENTER); // Initial text of the level, can be updated
        levelLabel.setForeground(Color.BLACK);
        // Layer label resizing
        if (levelLabel != null) {
            int levelFontSize = Math.min(getWidth() / 8, getHeight() / 8);
            levelFontSize = Math.max(25, levelFontSize);
            levelLabel.setFont(new Font("Arial", Font.BOLD, levelFontSize));
        }
        topPanel.add(levelLabel, BorderLayout.CENTER);

        // Pright placeholder (empty panel)
        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        topPanel.add(rightPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.PAGE_START);

        // --- BOTTOM PANEL (Buttons) ---
        bottomPanel.setOpaque(false);
        bottomPanel.add(exitButton);
        bottomPanel.add(restartButton);
        add(bottomPanel, BorderLayout.PAGE_END);
    }
    protected abstract JPanel setupCenterPanel(); // This method will be implemented by subclasses

    protected void applyScalingBasedOnCurrentDimensions() {
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Example: resizing buttons
        int buttonSize = Math.min(panelWidth, panelHeight) / 10;
        buttonSize = Math.max(buttonSize, 30); // Minimum size

        restartButton.setPreferredSize(new Dimension(buttonSize * 2, buttonSize));
        exitButton.setPreferredSize(new Dimension(buttonSize * 2, buttonSize));
        settingsButton.setPreferredSize(new Dimension(buttonSize*2, buttonSize));

        // Layer label resizing
        if (levelLabel != null) {
            int levelFontSize = Math.min(getWidth() / 8, getHeight() / 8);
            levelFontSize = Math.max(25, levelFontSize);
            levelLabel.setFont(new Font("Arial", Font.BOLD, levelFontSize));
        }

        revalidate();
        repaint();
    }

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
            g2.setColor(Color.WHITE); // Fallback
            g2.fillRect(0, 0, width, height);
        }

        g2.setClip(null);
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(Color.BLACK);
        g2.draw(roundedRect);

        g2.dispose();
        super.paintComponent(g);
    }
    public void updateLabels(int level) {
        // Update the level label
        JLabel levelLabel = (JLabel) ((JPanel) getComponent(0)).getComponent(1); // Access the level label in the topPanel
        levelLabel.setText("Level " + level);
        applyScalingBasedOnCurrentDimensions();
    }

    @Override
    public void bindController(ControllerObj controller) {
    }
}
