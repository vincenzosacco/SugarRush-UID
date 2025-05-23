package model.game.entities.evil;

import model.Model;
import model.game.Constants;
import model.game.Game;
import model.game.utils.Cell;
import model.game.Constants.Direction;

import java.util.List;

import static config.View.TILE_SIZE;

/**
 * The Enemy1 has the following behavior:
 * <p>
 * on each game state update:
 *     - move one cell forward
 * </p>
 */
public class Enemy1 extends Enemy {
    private static Direction DIRECTION = Direction.LEFT; // remove this line when the enemy is implemented
    private int pixelX;
    private int pixelY;
    private static final int PIXELS_PER_MOVE = 4;

    @Override
    public Constants.Block blockType() {
        return Constants.Block.ENEMY1;
    }

    public Enemy1(int row, int col) {
        super(row, col);
        setMoveDelay(10);

        // Initialize pixel position from cell position
        this.pixelX = col * TILE_SIZE;
        this.pixelY = row * TILE_SIZE;
    }
    public Enemy1(Cell coord) {
        super(coord);
        this.pixelX = coord.getCol() * TILE_SIZE;
        this.pixelY = coord.getRow() * TILE_SIZE;
    }


    @Override
    public Cell computeAction() {
        Game game = Model.getInstance().getGame();
        List<List<Constants.Block>> state = game.getState();

        // Determine the current cell
        int curRow = pixelY / TILE_SIZE;
        int curCol = pixelX / TILE_SIZE;

        // Calculate movement
        if (DIRECTION == Direction.LEFT) {
            pixelX -= PIXELS_PER_MOVE;
        } else if (DIRECTION == Direction.RIGHT) {
            pixelX += PIXELS_PER_MOVE;
        }

        // Determine the target cell after movement
        int newRow = pixelY / TILE_SIZE;
        int newCol = pixelX / TILE_SIZE;

        // Out of bounds or invalid movement?
        boolean outOfBounds = newCol < 0 || newCol >= state.get(0).size();
        boolean hitsWall = !outOfBounds && state.get(newRow).get(newCol) != Constants.Block.SPACE;

        // If out of bounds or hit a wall, reverse direction and adjust position
        if (outOfBounds || hitsWall) {
            if (DIRECTION == Direction.LEFT) {
                DIRECTION = Direction.RIGHT;
            } else if (DIRECTION == Direction.RIGHT) {
                DIRECTION = Direction.LEFT;
            }

            // Adjust pixel position to avoid overlap
            if (DIRECTION == Direction.LEFT) {
                pixelX = (curCol + 1) * TILE_SIZE - 1; // Snap to the right edge of the cell
            } else if (DIRECTION == Direction.RIGHT) {
                pixelX = curCol * TILE_SIZE; // Snap to the left edge of the cell
            }
        }

        // Return the current cell position for collision handling
        return new Cell(pixelY / TILE_SIZE, pixelX / TILE_SIZE);
    }



    @Override
    public boolean manageCollision(Constants.Block block) {
        if (DIRECTION == Direction.NONE) return false;

        return block == Constants.Block.SPACE;
    }

    @Override
    public void performAction(Cell newCoord) {
        this.coord = newCoord;
    }




}
