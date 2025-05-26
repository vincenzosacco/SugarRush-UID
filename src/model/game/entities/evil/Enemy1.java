package model.game.entities.evil;

import model.Model;
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

    // CONSTRUCTORS //
    @Override
    public Constants.Block blockType() {
        return Constants.Block.ENEMY1;
    }

    public Enemy1(int row, int col) {
        super(row,col);
        setActionDelay(20);
    }

    public Enemy1(Cell coord) {
        super(coord);
    }

    @Override
    public boolean shouldPerform() {
        if (DIRECTION == Direction.NONE) return false; // no movement
        return super.shouldPerform();
    }


    // GAME ACTIONS //

    /**
     * <p>The bee moves itself one cell in direction {@link #DIRECTION}.</p>
     * This method calculates the new position based on the current direction, but
     * <b>does not</b> update the entity's position neither the game state.
     * @return the new position where the entity would move if the action is performed.
     */
    @Override
    public Cell computeAction() {
        assert ! (DIRECTION == Direction.NONE) : "Mustn't call computeAction() when DIRECTION is NONE";

        // CALCULATE (possible) NEW POSITION //
        Cell newCoord = computeNewPos();

        // CHECK IF NEW POSITION IS VALID //
        Constants.Block toMoveBlock = Model.getInstance().getGame().blockAt(newCoord);
        if (! manageCollision(toMoveBlock)) {
            Enemy1.DIRECTION = Enemy1.DIRECTION.opposite();
            newCoord = computeNewPos();
            // the above lines
            assert newCoord != getCoord() : "New position must be different from the current position." +
                    "\n This is a bug in game logic or malformed map.";
        }

        return newCoord;
    }

    /** @return where the entity would move if the action is performed */
    private Cell computeNewPos() {
        Cell newCoord = getCoord();
        switch (DIRECTION) {
            case UP -> newCoord.incrRow();
            case DOWN -> newCoord.decrRow();
            case LEFT -> newCoord.decrCol();
            case RIGHT -> newCoord.incrCol();
            default -> throw new IllegalStateException("Unexpected value: " + DIRECTION);
        }
        return newCoord;
    }

    @Override
    public boolean manageCollision(Constants.Block block) {
        return block == Constants.Block.SPACE || block == Constants.Block.CREATURE;
    }

    @Override
    public void performAction(Cell newCoord) {
        this.coord = newCoord;
    }



}
