package view.game;

import model.Model;
import model.game.Constants;
import model.game.Entity;
import model.game.Game;
import model.game.utils.Cell;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static config.ViewConfig.*;
import static config.ViewConfig.TILE_SIZE;

// Draw the map and dynamic entities (game graphics).
class _GameContent extends JPanel {
    // BufferedImage to cache static background and reduce graphics load.
    BufferedImage staticBackground = null;

    public _GameContent() {}

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
    }

    // Draws the game's static background (walls, obstacles, etc.) only once at startup
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
                        game.blockAt(new Cell(row, col)) == Constants.Block.SUGAR ||
                        game.blockAt(new Cell(row, col)) == Constants.Block.CANDY) // static blocks
                {

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
    // Draw dynamic objects (like the player or enemies) with every screen update
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

            Objects.requireNonNull(image, "The image cannot be null");
            g2d.drawImage(image, x, y, TILE_SIZE, TILE_SIZE, null);
        }
    }
}
