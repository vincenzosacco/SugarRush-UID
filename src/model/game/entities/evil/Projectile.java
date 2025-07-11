package model.game.entities.evil;

import config.ModelConfig;
import model.Model;
import model.game.GameConstants;
import model.game.utils.Cell;
import model.game.Entity;
import model.game.GameConstants.Direction;
import utils.audio.GameAudioController;

/**
 * Represents a projectile fired by an enemy.
 * The projectile moves in a straight line in the given direction every few frames.
 */
public class Projectile extends Entity {

    public Projectile(Cell coord, Direction dir) {
        super(coord);
        this.direction = dir;
        setActionDelay(7); // Moves every 7 frames
    }

    // The block type representing this projectile in the game matrix
    @Override
    public GameConstants.Block blockType() {
        return GameConstants.Block.PROJECTILE;
    }

    // Return the cell in the direction of movement
    @Override
    protected Cell computeAction() {
        Cell newCoord = getCoord(); // Start from current position

        // Update coordinates based on a direction (returns null if the projectile touch the border)
        switch (direction) {
            case LEFT ->{
                if (newCoord.getCol()==0){
                    return null;
                }
                newCoord.decrCol();
            }
            case RIGHT ->{
                if (newCoord.getCol()==ModelConfig.COL_COUNT-1){
                    return null;
                }
                newCoord.incrCol();
            }
            case UP ->{
                if (newCoord.getRow()==0){
                    return null;
                }
                newCoord.decrRow();
            }
            case DOWN ->{
                if (newCoord.getRow()== ModelConfig.ROW_COUNT-1){
                    return null;
                }
                newCoord.incrRow();
            }
            default -> {}
        }
        return newCoord;
    }

    // Manages interactions when the projectile tries to move into a new cell.
    @Override
    protected boolean manageCollision(GameConstants.Block block, Cell cell) {
        if (block == GameConstants.Block.SPACE|| block==GameConstants.Block.ENEMY1) {
            // The projectile can move through empty space or over enemy1
            return true;
        } else if (block == GameConstants.Block.CREATURE) {
            // If the projectile hits the creature, kill it and stop
            Model.getInstance().getGame().killCreature();
            GameAudioController.getInstance().playSfx("hitShot");
            return false;
        }

        // Any other block (wall, obstacle, etc.) blocks the projectile
        return false;
    }

    // Updates the projectile's position to the new cell.
    @Override
    protected void performAction(Cell newCoord) {
        this.coord = newCoord;
    }
}
