package model.game.entities.evil;

import model.Model;
import model.game.Constants;
import model.game.Game;
import model.game.utils.Cell;
import model.game.Constants.Direction;

import java.util.List;

/**
 * The Enemy1 has the following behavior:
 * <p>
 * on each game state update:
 *     - move one cell forward
 * </p>
 */
public class Enemy1 extends Enemy {
    private static Direction DIRECTION = Direction.LEFT; // remove this line when the enemy is implemented

    @Override
    public Constants.Block blockType() {
        return Constants.Block.ENEMY1;
    }

    public Enemy1(int row, int col) {
        super(row,col);
        setActionDelay(10);
    }
    public Enemy1(Cell coord) {
        super(coord);
    }

    @Override
    public Cell computeAction() {
        Game game = Model.getInstance().getGame();
        List<List<Constants.Block>> state = game.getState(); // current game map

        int newRow = coord.getRow();
        int newCol = coord.getCol();

        // Try to move in the current direction
        switch (DIRECTION) {
            case LEFT -> newCol--;
            case RIGHT -> newCol++;
            default -> DIRECTION = Direction.LEFT; // Default direction if NONE
        }

        // Check if out of bounds
        boolean outOfBounds = newRow < 0 || newCol < 0 ||
                newRow >= state.size() ||
                newCol >= state.get(0).size();

        // Check the block ahead
        if (!outOfBounds) {
            Constants.Block nextBlock = state.get(newRow).get(newCol);

            // Check if it's passable
            if (nextBlock == Constants.Block.SPACE || nextBlock == Constants.Block.CREATURE) {
                // Valid target cell: proceed to move
                return new Cell(newRow, newCol);
            }
        }

        // If it hits a wall or is out of bounds, change direction
        if (DIRECTION == Direction.LEFT) {
            DIRECTION = Direction.RIGHT;
        } else if (DIRECTION == Direction.RIGHT) {
            DIRECTION = Direction.LEFT;
        }

        // Stay in the same place if no valid move is possible
        return getCoord();
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
