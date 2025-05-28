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

    @Override
    public Constants.Block blockType() {
        return Constants.Block.ENEMY1;
    }

    public Enemy1(int row, int col) {

        super(row,col);
        setActionDelay(10);
        DIRECTION = Direction.LEFT;
    }
    public Enemy1(Cell coord) {
        super(coord);
    }

    @Override
    public Cell computeAction() {
        Cell newCoord = getCoord();

        // Try to move in the current direction
        switch (DIRECTION) {
            case LEFT -> newCoord.decrCol();
            case RIGHT -> newCoord.incrCol();
            default -> throw new IllegalStateException("Unexpected value: " + DIRECTION);
        }

        // If it hits a wall or is out of bounds, change direction

        // Stay in the same place if no valid move is possible
        return newCoord;
    }


    @Override
    public boolean manageCollision(Constants.Block block) {
//        if (DIRECTION == Direction.NONE) return false;

        // CAN MOVE ONLY IF THE BLOCK IS SPACE or CREATURE//
        if (block == Constants.Block.SPACE || block == Constants.Block.CREATURE) {
            return true;
        }
        // ELSE CHANGE TO OPPOSITE DIRECTION //
        else {
            assert DIRECTION != Direction.NONE : "Enemy1 cannot have NONE direction";
            DIRECTION = DIRECTION.opposite();
            return false;
        }

    }

    @Override
    public void performAction(Cell newCoord) {
        this.coord = newCoord;
    }






}
