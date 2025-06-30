package model.game.entities;

import model.Model;
import model.game.Constants;
import model.game.Entity;
import model.game.utils.Cell;
import model.game.Constants.Direction;
import utils.audio.GameAudioController;
import view.View;


/**
 * Model for Creature entity
 */
public class Creature extends Entity {
    private int candyCount = 0;

    public Creature(int row, int col) {
        super(row, col);
        setActionDelay(1); // at the moment, the fastest entity in the game
    }


    @Override
    public Constants.Block blockType() {
        return Constants.Block.CREATURE;
    }

    @Override
    protected boolean shouldPerform() {
        // Just to avoid unnecessary computations
        if (this.direction == Direction.NONE) return false;
        return super.shouldPerform();
    }

    @Override
    public Cell computeAction() {
        // Direction.NONE case is handled in shouldPerform

        Cell newCoord = getCoord();

        switch (this.direction) {
            case Direction.UP -> newCoord.decrRow(); // going up means decrementing the row index by 1
            case Direction.DOWN -> newCoord.incrRow(); // going down means incrementing the row index by 1
            case Direction.LEFT -> newCoord.decrCol(); // going left means decrementing the col index by 1
            case Direction.RIGHT -> newCoord.incrCol(); // going right means incrementing the col index by 1
            default -> throw new IllegalStateException("Unknown direction: " + this.direction);
        }

        return newCoord;
    }

    @Override
    public boolean manageCollision(Constants.Block block, Cell cell ) {
        // Direction.NONE case is handled in shouldPerform

        boolean canMove = true;

        switch (block) {
            case SUGAR ->  {
                GameAudioController.getInstance().playSfx("bite");
                View.getInstance().getGamePanel().repaintBackground(); // repaint the static background

                Model.getInstance().getGame().addstar();
                Model.getInstance().getGame().win();
            }
            case CANDY -> {
                GameAudioController.getInstance().playSfx("bite");
                View.getInstance().getGamePanel().repaintBackground(); // repaint the static background

                addCandy();
                Model.getInstance().getGame().addstar();

            }
            case SPACE -> {/*do nothing*/}
            case WALL -> {
                GameAudioController.getInstance().playSfx("wall");
                this.direction = Direction.NONE; // stop moving
                return false;
            }
            // DIE if collides with an enemy or thorns //
            case ENEMY1 -> {
                GameAudioController.getInstance().playSfx("killBee");
                Model.getInstance().getGame().killCreature();
            }
            case THORNS -> {
                GameAudioController.getInstance().playSfx("thorns");
                Model.getInstance().getGame().killCreature();
            }

            case CREATURE -> throw new AssertionError("Creature cannot collide with creature, there is a bug somewhere");

            default -> throw new IllegalStateException("Unexpected value: " + block);
        }

        return canMove;
    }


    @Override
    public void performAction(Cell newCoord) {
        // Direction.NONE case is handled in shouldPerform

        this.coord.setCoord(newCoord);
        // Collision detection is done in <manageCollision> so we don't need to check it here
    }


    // CREATURE BEHAVIOR //

    private void addCandy() {
        candyCount++;
    }
    public int getCandyCount() {
        return candyCount;
    }


    /**Checks if the creature is currently moving.
     * A creature is considered moving if its direction is not NONE.
     * @return true if the creature is moving, false otherwise
     */
    public boolean isMoving() {
        return this.direction != Direction.NONE; // this.direction is set to NONE when the creature collides with a wall
    }
}