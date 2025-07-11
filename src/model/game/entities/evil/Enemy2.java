package model.game.entities.evil;

import config.ModelConfig;
import model.Model;
import model.game.GameConstants;
import model.game.utils.Cell;
import model.game.GameConstants.Direction;
import utils.audio.GameAudioController;

/**
 * Enemy2 is a stationary enemy that shoots projectiles in a fixed direction every few frames.
 */
public class Enemy2 extends Enemy {
    private final int shootDelay = 15; // Number of frames between each shot
    private int shootCounter = 0; // Counter to track when to shoot

    public Enemy2(int row, int col,Direction direction) {
        super(row, col);
        setActionDelay(15); // Delay between each action frame
        this.direction = direction; // Shooting direction
    }

     // The type of block this entity represents in the game matrix
    @Override
    public GameConstants.Block blockType() {
        return GameConstants.Block.ENEMY2;
    }
    // Called every frame when the entity is ready to act.
    @Override
    protected Cell computeAction() {
        shootCounter++;
        if (shootCounter >= shootDelay) {
            shootCounter = 0;
            shoot(); // Fire a projectile
        }
        return getCoord(); // Does not move
    }

    // Spawns a new projectile in the specified direction from the enemy's position.
    private void shoot() {
        Cell projCoord = new Cell(coord); // Start at current position

        // Compute the target cell based on a direction (if he would shoot outside the border, he doesn't shoot)
        switch (direction) {
            case LEFT ->{
                if(projCoord.getCol()==0){
                    return;
                }
                projCoord.decrCol();
            }
            case RIGHT ->{
                if(projCoord.getCol()== ModelConfig.COL_COUNT-1){
                    return;
                }
                projCoord.incrCol();
            }
            case UP ->{
                if(projCoord.getRow()==0){
                    return;
                }
                projCoord.decrRow();
            }
            case DOWN ->{
                if(projCoord.getRow()== ModelConfig.ROW_COUNT-1){
                    return;
                }
                projCoord.incrRow();
            }
            default -> { return; }
        }

        // If the creature is directly in the next cell, it is killed immediately.
        GameConstants.Block block = Model.getInstance().getGame().blockAt(projCoord);
        if (block == GameConstants.Block.CREATURE) {
            Model.getInstance().getGame().killCreature(); // Kill
            GameAudioController.getInstance().playSfx("hitShot");
        }
        if(block != GameConstants.Block.ENEMY2 && block != GameConstants.Block.THORNS && block != GameConstants.Block.WALL) {
            // Create and add the new projectile to the game
            Projectile proj = new Projectile(projCoord, direction);
            Model.getInstance().getGame().addEntity(proj);
        }
    }

    // Enemy2 never moves, so collisions are irrelevant.
    @Override
    protected boolean manageCollision(GameConstants.Block block, Cell cell) {
        return false;
    }

    // Enemy2 never performs a move, so this is empty.
    @Override
    protected void performAction(Cell newCoord) {
        // Enemy2 stays in place, no movement needed
    }
}
