package view.game;

import controller.GameController;
import controller.ControllerObj;
import controller.GameLoop;
import model.Model;
import model.game.Constants;
import model.game.Entity;
import model.game.Game;
import model.game.utils.Cell;
import utils.audio.GameAudioController;
import view.ViewComp;
import view.button.PauseButton;
import view.menu.GameMenuPanel;
import view.menu.LosePanel;
import view.menu.WinPanel;

import javax.swing.*;

import java.awt.*;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static config.ViewConfig.*;


/**
 * Main game panel
 */
public class GamePanel extends JPanel implements ViewComp {

    private Timer gameTimer;
    private int elapsedSeconds = 0;
    private int oldElapsedSeconds;

    private BufferedImage staticBackground = null;

    private final GameMenuPanel gameSettingsPanel;
    private final LosePanel losePanel;
    private final WinPanel winPanel;


    private PauseButton pauseButton;

    private JLayeredPane layeredPane;

    private JPanel gameContentDrawingPanel;

    private GameLoop gameLoop=GameLoop.getInstance();

    public PauseButton getPauseButton() {
        return pauseButton;
    }

    public JPanel getGameContentDrawingPanel() {
        return gameContentDrawingPanel;
    }

    public GamePanel() {
        setLayout(new GridLayout(1,1));
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        Color skyblue = new Color(0, 188, 250);
        setBackground(skyblue);

        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        this.add(layeredPane);

        gameContentDrawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                // Draw the background of this panel
                super.paintComponent(g);

                // Draw the static background of the game (blocks, thorns, sugar)
                if (staticBackground == null) {
                    drawOnStartUp();
                }
                if (staticBackground != null) {
                    g.drawImage(staticBackground, 0, 0, getWidth(), getHeight(), null);
                }
                drawOnUpdate((Graphics2D) g); // Draw dynamic entities

                // Draw the elapsed time
                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.BOLD, 20));
                FontMetrics fm = g.getFontMetrics();
                int textWidth = fm.stringWidth("Time: " + elapsedSeconds + "s");
                // Place the time at the top left
                g.drawString("Time: " + elapsedSeconds + "s", 10, 25);
            }
        };

        gameContentDrawingPanel.setOpaque(true); //Opaque to cover the background underneath
        gameContentDrawingPanel.setBackground(skyblue);
        layeredPane.add(gameContentDrawingPanel, JLayeredPane.DEFAULT_LAYER); // Added to the lowest level

        pauseButton=new PauseButton();
        pauseButton.setEnabled(true);
        int minSizePanel=Math.min(BOARD_WIDTH,BOARD_HEIGHT);
        int buttonSize=Math.max(30,minSizePanel/15);
        pauseButton.setPreferredSize(new Dimension(buttonSize, buttonSize));
        pauseButton.setMinimumSize(new Dimension(buttonSize, buttonSize));
        pauseButton.setMaximumSize(new Dimension(buttonSize, buttonSize));

        pauseButton.addActionListener(e -> {
            // PAUSE GAME when the game menu is opened
            if (gameLoop.isRunning()) {
                gameLoop.stop();
                stopGameTimer();
            }
            // RESTORE GAME when the game menu is closed
            else {
                startGameTimer();
                gameLoop.start();
            }

            toggleSettingsPanel();
        });

        // Container panel for the pause button and aligned to the RIGHT
        JPanel buttonContainerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10)); //with padding
        buttonContainerPanel.setOpaque(false);
        buttonContainerPanel.add(pauseButton);
        layeredPane.add(buttonContainerPanel, JLayeredPane.PALETTE_LAYER); // Added to a higher level of the game


        //Menu panels (overlays)
        gameSettingsPanel = new GameMenuPanel();
        losePanel = new LosePanel();
        winPanel = new WinPanel();

        // Add menu panels to the MODAL_LAYER layer (the top one, for overlay)
        layeredPane.add(gameSettingsPanel, JLayeredPane.MODAL_LAYER);
        layeredPane.add(losePanel, JLayeredPane.MODAL_LAYER);
        layeredPane.add(winPanel, JLayeredPane.MODAL_LAYER);

        gameSettingsPanel.setVisible(false);
        losePanel.setVisible(false);
        winPanel.setVisible(false);

        //Add a ComponentListener to the main GamePanel to handle resizing
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // When the GamePanel is resized, it also resizes the layeredPane
                layeredPane.setBounds(0, 0, getWidth(), getHeight());

                // Resize and position the game drawing panel
                gameContentDrawingPanel.setBounds(0, 0, getWidth(), getHeight());

                // Resize and reposition the panel containing the pause button
                int currentButtonSize = Math.min(getWidth(), getHeight());
                currentButtonSize = Math.max(30, currentButtonSize/15);
                pauseButton.setPreferredSize(new Dimension(currentButtonSize, currentButtonSize));
                pauseButton.setMinimumSize(new Dimension(currentButtonSize, currentButtonSize));
                pauseButton.setMaximumSize(new Dimension(currentButtonSize, currentButtonSize));
                // The height of the button container should fit the size of the button plus the padding
                buttonContainerPanel.setBounds(0, 0, getWidth(), currentButtonSize + 20);
                buttonContainerPanel.revalidate();

                // Update the position of the menu panels (centered)
                applyPanelBounds(gameSettingsPanel);
                applyPanelBounds(losePanel);
                applyPanelBounds(winPanel);

                layeredPane.revalidate();
                layeredPane.repaint();
            }
        });
    }

    // Method to calculate and apply centered bounds to menu panels
    private void applyPanelBounds(JPanel panel) {
        if (panel == null) return;

        int parentWidth = getWidth();
        int parentHeight = getHeight();

        int panelWidth = parentWidth / 2;
        int panelHeight = parentHeight / 2;

        // Set a minimum
        panelWidth = Math.max(panelWidth, 300);
        panelHeight = Math.max(panelHeight, 200);

        // Coordinates to center the panel
        int panelX = (parentWidth - panelWidth) / 2;
        int panelY = (parentHeight - panelHeight) / 2;

        panel.setBounds(panelX, panelY, panelWidth, panelHeight);
        panel.revalidate();
    }

    public void startGameTimer() {
        if (gameTimer == null) {
            gameTimer = new Timer(1000, e -> {
                elapsedSeconds++;
                repaint();
            });
        }
        if (!gameTimer.isRunning()) {
            gameTimer.start();
        }
    }

    public void stopGameTimer() {
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
        }
    }

    public Timer getGameTimer() {
        return gameTimer;
    }

    public void resetGameTimer() {
        stopGameTimer();
        oldElapsedSeconds=elapsedSeconds;
        elapsedSeconds = 0;
        repaint();
    }

    /**
     * Binds the controller to the view component.
     * Only one controller can be bound to this view component.
     * @param controller the controller to be bound to the view component, must be a {@link GameController}
     */
    @Override
    public void bindController(ControllerObj controller) {
        if (!(controller instanceof GameController)) {
            throw new IllegalArgumentException("controller must be instance of GameController");
        }
        if (this.getKeyListeners().length != 0){
            throw new IllegalStateException("Cannot have more than one key listener");
        }

        this.addKeyListener((GameController) controller);

        this.setFocusable(true);
    }

    /**
     * Calls {@code super().setVisible(aFlag)} and {@code if aFlag == true}:
     * <p>
     *     - calls {@code this.requestFocusInWindow()} -> so this panel can get input from user
     * </p>
     * <p>
     *     - show 'start new game' messageDialog.
     * </p>
     */

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    private void drawOnStartUp(){
        staticBackground = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D bg = (Graphics2D) staticBackground.getGraphics();
        // best quality rendering hints
        bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        bg.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        bg.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // DRAW in BUFFERED IMAGE //
        Game game = Model.getInstance().getGame();
        List<List<Constants.Block>> gameMatrix = game.getState();
        assert gameMatrix != null && !gameMatrix.isEmpty();

        for (int row = 0; row < gameMatrix.size(); row++) {
            int y = row * TILE_SIZE; // iterating row in model matrix corresponds to moving on y-axis(from top to bottom) on graphics coordinates.
            for (int col = 0; col < gameMatrix.get(row).size(); col++) {
                int x = col * TILE_SIZE; // iterating col in model matrix corresponds to moving on x-axis(from left to right) on graphics coordinates.

                // DRAW ONLY STATIC BLOCKS //
                if (game.blockAt(new Cell(row, col)) == Constants.Block.WALL ||
                        game.blockAt(new Cell(row, col)) == Constants.Block.THORNS ||
                        game.blockAt(new Cell(row, col)) == Constants.Block.SUGAR) {

                    Constants.Block block = gameMatrix.get(row).get(col);
                    Image image = _BlocksImage.getInstance().getStaticBlockImg(block); // get the image for the block type
                    if (!(image == null))
                        bg.drawImage(image, x, y, TILE_SIZE, TILE_SIZE, null);
                }
            }
        }

        // Draw the static background
        bg.dispose();
    }

    private void drawOnUpdate(Graphics2D g2d) {
        Game game = Model.getInstance().getGame();
        List<Entity> entities = new ArrayList<>(game.getEntities());

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        // DRAW ENTITIES //
        for (Entity entity : entities) {
            int row = entity.getCoord().getRow();
            int col = entity.getCoord().getCol();
            int x = col * TILE_SIZE;
            int y = row * TILE_SIZE;

            Constants.Block blockType = entity.blockType();
            Image image = _BlocksImage.getInstance().getDynamicBlockImg(blockType, entity.getDirection());

            assert image != null;
            g2d.drawImage(image, x, y, TILE_SIZE, TILE_SIZE, null);
        }
    }

    public void resetPanelForNewLevel() {
        this.staticBackground = null; // Force background to redraw on next paintComponent
        this.repaint(); // Requires the panel to redraw itself
    }


    // SETTINGS
    public GameMenuPanel toggleSettingsPanel(){
        int currentLevel = Model.getInstance().getGame().getCurrLevel();
        gameSettingsPanel.setCurrentLevel(currentLevel); // Update the level in the settings panel
        gameSettingsPanel.updateContent(); // Update texts/coins

        gameSettingsPanel.setOpen(!gameSettingsPanel.isOpen()); // Invert the opening state
        gameSettingsPanel.setVisible(gameSettingsPanel.isOpen()); // Make visible/invisible
        if(gameSettingsPanel.isOpen()){
            pauseButton.setEnabled(false);
            GameAudioController.getInstance().stopBackgroundMusic(); // Stop game music when menu is open
        }
        else{
            pauseButton.setEnabled(true);
            GameAudioController.getInstance().playGameMusic(); // Resume game music when menu is closed
        }

        this.revalidate();
        this.repaint();
        return gameSettingsPanel;
    }

    //END LEVEL
    public LosePanel loseLevel(){
        int currentLevel = Model.getInstance().getGame().getCurrLevel();
        losePanel.setCurrentLevel(currentLevel);
        losePanel.updateLabels(currentLevel);
        losePanel.setElapsedTime(oldElapsedSeconds); // Time spent
        losePanel.setVisible(true);
        applyPanelBounds(losePanel);
        losePanel.requestFocusInWindow();
        pauseButton.setEnabled(false);
        GameAudioController.getInstance().playSfx("death"); // Play Death SFX
        GameAudioController.getInstance().stopBackgroundMusic(); // Stop game music
        this.revalidate();
        this.repaint();
        return losePanel;
    }

    public WinPanel winLevel(){
        int currentLevel = Model.getInstance().getGame().getCurrLevel();
        winPanel.setCurrentLevel(currentLevel);
        winPanel.updateLabels(currentLevel);
        winPanel.setElapsedTime(oldElapsedSeconds); // Time spent
        winPanel.setVisible(true);
        applyPanelBounds(winPanel);
        winPanel.requestFocusInWindow();
        pauseButton.setEnabled(false);
        GameAudioController.getInstance().playSfx("win"); // Play Victory SFX
        GameAudioController.getInstance().stopBackgroundMusic(); // Stop game music
        this.revalidate();
        this.repaint();
        return winPanel;
    }

    public void endGame(){
        GameLoop.getInstance().stop();
        resetGameTimer();
        GameAudioController.getInstance().stopBackgroundMusic();
    }


}