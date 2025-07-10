package model.game.entities.evil;

import model.Model;
import model.game.GameConstants;
import model.game.utils.Cell;
import model.game.GameConstants.Direction;
import utils.audio.GameAudioController;

/**
 * The Enemy1 has the following behavior:
 * <p>
 * on each game state update:
 *     - move one cell forward
 * </p>
 */
public class Enemy1 extends Enemy {

    @Override
    public GameConstants.Block blockType() {
        return GameConstants.Block.ENEMY1;
    }

    public Enemy1(int row, int col) {
        super(row,col);
        setActionDelay(10);
        this.direction = Direction.LEFT;
    }
    public Enemy1(Cell coord) {
        super(coord);
    }

    @Override
    public Cell computeAction() {
        Cell newCoord = getCoord();

        // Try to move in the current direction
        switch (this.direction) {
            case LEFT -> newCoord.decrCol();
            case RIGHT -> newCoord.incrCol();
            default -> throw new IllegalStateException("Unexpected value: " + this.direction);
        }

        // If it hits a wall or is out of bounds, change direction

        // Stay in the same place if no valid move is possible
        return newCoord;
    }


    @Override
    public boolean manageCollision(GameConstants.Block block, Cell cell) {
//        if (this.direction == Direction.NONE) return false;

        // CAN MOVE ONLY IF THE BLOCK IS SPACE, CREATURE or PROJECTILE//
        if (block == GameConstants.Block.SPACE || block == GameConstants.Block.PROJECTILE) {
            return true;
        }
        else if (block == GameConstants.Block.CREATURE){
            Model.getInstance().getGame().end(false);
            GameAudioController.getInstance().playSfx("killBee");
            return true;
        }
        // ELSE CHANGE TO OPPOSITE DIRECTION //
        assert this.direction != Direction.NONE : "Enemy1 cannot have NONE direction";
        this.direction = this.direction.opposite();
        return false;
    }

    @Override
    public void performAction(Cell newCoord) {
        this.coord = newCoord;
    }






}
