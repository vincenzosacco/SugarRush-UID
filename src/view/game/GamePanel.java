package view.game;

import controller.GameController;
import controller.ControllerObj;
import model.Model;
import model.game.Constants;
import model.game.Entity;
import model.game.Game;
import model.game.entities.evil.Enemy;
import model.game.utils.Cell;
import view.ViewComp;
import view.menu.GameSettingsPanel;

import javax.swing.JPanel;
import javax.swing.JOptionPane;

import java.awt.*;

import java.awt.image.BufferedImage;
import java.util.List;

import static config.View.*;


/**
 * Main game panel
 */
public class GamePanel extends JPanel implements ViewComp {


    private BufferedImage staticBackground = null;

    public GamePanel() {
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        Color skyblue = new Color(0, 188, 250);
        setBackground(skyblue);

        this.add(gameSettings);

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
        drawOnUpdate(g);
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
                    Image image = EntitiesView.getImage(block, null); // get the image for the block type
                    if (!(image == null))
                        bg.drawImage(image, x, y, TILE_SIZE, TILE_SIZE, null);
                }
            }
        }

        // Draw the static background
        bg.dispose();
    }

    private void drawOnUpdate(Graphics g) {
        Game game = Model.getInstance().getGame();

        List<Entity> entities = game.getEntities();

        for (Entity entity : entities) {
            int row = entity.getCoord().getRow();
            int col = entity.getCoord().getCol();
            int x = col * TILE_SIZE;
            int y = row * TILE_SIZE;

            Constants.Block blockType = entity.blockType();
            Constants.Direction direction = entity instanceof Enemy enemy ? enemy.getDirection() : null;
            Image image = EntitiesView.getImage(blockType, direction);

            assert image != null;
            g.drawImage(image, x, y, TILE_SIZE, TILE_SIZE, null);
        }
    }



    // SETTINGS
    private final GameSettingsPanel gameSettings = new GameSettingsPanel();
    public GameSettingsPanel toggleSettingsPanel(){
        gameSettings.setOpen(!gameSettings.isOpen());
        gameSettings.setVisible(gameSettings.isOpen()); // if isOpen == false set gameSettingsPanel invisible
        this.revalidate();
        return gameSettings;
    }



}
