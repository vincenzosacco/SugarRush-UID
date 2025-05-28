package model.game.entities;

import model.Model;
import model.game.Constants;
import model.game.Entity;
import model.game.utils.Cell;
import model.game.Constants.Direction;


/**
 * Model for Creature entity
 */
public class Creature extends Entity {
    private int sugarCount = 0;
    private Direction direction = Direction.NONE;

    public Creature(int row, int col) {
        super(row, col);
        setActionDelay(1); // at the moment, the fastest entity in the game
    }


    @Override
    public Constants.Block blockType() {
        return Constants.Block.CREATURE;
    }


    @Override
    public Cell computeAction() {
        Cell newCoord = getCoord();

        switch (this.direction) {
            case Direction.NONE ->{} // no movement
            case Direction.UP -> newCoord.decrRow(); // going up means decrementing the row index by 1
            case Direction.DOWN -> newCoord.incrRow(); // going down means incrementing the row index by 1
            case Direction.LEFT -> newCoord.decrCol(); // going left means decrementing the col index by 1
            case Direction.RIGHT -> newCoord.incrCol(); // going right means incrementing the col index by 1
            default -> throw new IllegalStateException("Unknown direction: " + this.direction);
        }

        return newCoord;
    }

    @Override
    public boolean manageCollision(Constants.Block block) {
        if (this.direction == Direction.NONE) return false;

        boolean canMove = true;

        switch (block) {
            case SUGAR -> addSugar();
            case SPACE -> {/*do nothing*/}
            case WALL -> {
                setDirection(Direction.NONE);
                return false;
            }
            // DIE if collides with an enemy or thorns //
            case ENEMY1, THORNS -> Model.getInstance().getGame().killCreature();

            case CREATURE -> throw new AssertionError("Creature cannot collide with creature, there is a bug somewhere");

            default -> throw new IllegalStateException("Unexpected value: " + block);
        }

        return canMove;
    }

    @Override
    public void performAction(Cell newCoord) {
        if (this.direction == Direction.NONE) return;

        this.coord.setCoord(newCoord);
        // Collision detection is done in <manageCollision> so we don't need to check it here
    }


    private void addSugar() {
        sugarCount++;
    }

    // GETTERS & SETTERS //
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getSugarCount() {
        return sugarCount;
    }
}