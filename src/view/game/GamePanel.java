package view.game;

import controller.GameController;
import controller.ControllerObj;
import controller.GameLoop;
import model.Model;
import model.game.Constants;
import model.game.Entity;
import model.game.Game;
import model.game.utils.Cell;
import view.ViewComp;
import view.button.PauseButton;
import view.menu.GameMenuPanel;
import view.menu.LosePanel;
import view.menu.WinPanel;

import javax.swing.*;

import java.awt.*;

import java.awt.image.BufferedImage;
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

    GameLoop gameLoop=GameLoop.getInstance();

    PauseButton pauseButton=new PauseButton();

    public PauseButton getPauseButton() {
        return pauseButton;
    }

    public GamePanel() {
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        Color skyblue = new Color(0, 188, 250);
        setBackground(skyblue);

        pauseButton.setPreferredSize(new Dimension(30,30));
        add(pauseButton,BorderLayout.PAGE_START);
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


        gameSettingsPanel = new GameMenuPanel();
        this.add(gameSettingsPanel);
        gameSettingsPanel.setVisible(false);
//        this.add(gameSettings);
        losePanel=new LosePanel();
        this.add(losePanel,BorderLayout.CENTER);
        losePanel.setVisible(false);

        winPanel=new WinPanel();
        this.add(winPanel,BorderLayout.CENTER);
        winPanel.setVisible(false);
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
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
            this.requestFocusInWindow(); // needed to get user input
            assert this.getParent() != null ;
            JOptionPane.showMessageDialog(this.getParent(),"Try to reach the sugar piece",
                    "New Game",JOptionPane.INFORMATION_MESSAGE);
            resetPanelForNewLevel();
            startGameTimer();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (staticBackground == null) {
            // If static background is not initialized, draw it
            drawOnStartUp();
        }
        g.drawImage(staticBackground, 0, 0, null);
        drawOnUpdate((Graphics2D)g);

        // Draw the elapsed time
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Time: " + elapsedSeconds + "s", 10, 30);
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
        List<Entity> entities = game.getEntities();

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
            pauseButton.setVisible(false);
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
        this.revalidate();
        this.repaint();
        return winPanel;
    }

    public void endGame(){
        // Game model reset
        Model.getInstance().getGame().clearGameMatrix();
        GameLoop.getInstance().stop();
        resetGameTimer();
    }


}
