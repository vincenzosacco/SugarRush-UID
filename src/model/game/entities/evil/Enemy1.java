package model.game.entities.evil;

import model.game.Constants;
import model.game.utils.Cell;
import model.game.Constants.Direction;

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
    }
    public Enemy1(Cell coord) {
        super(coord);
    }

    @Override
    public Cell computeAction() {
        int newRow = this.coord.getRow();
        int newCol = this.coord.getCol();

        // randomly change direction ( TODO: remove this line when the enemy is implemented )
        if (Math.random() < 0.1) {
            DIRECTION = Direction.values()[(int) (Math.random() * Direction.values().length)];
        }

        switch (DIRECTION) {
            case Direction.NONE -> {return getCoord();} // no movement
            case Direction.UP -> --newRow; // going up means decrementing the row index by 1
            case Direction.DOWN -> ++newRow; // going down means incrementing the row index by 1
            case Direction.LEFT -> --newCol; // going left means decrementing the col index by 1
            case Direction.RIGHT -> ++newCol; // going right means incrementing the col index by 1
            default -> throw new IllegalStateException("Unknown direction: " + DIRECTION);
        }
        return new Cell(newRow, newCol);

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
