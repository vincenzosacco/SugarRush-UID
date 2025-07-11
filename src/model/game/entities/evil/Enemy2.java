package model.game.entities.evil;

import model.Model;
import model.game.GameConstants;
import model.game.GameConstants.Direction;
import model.game.utils.Cell;
import utils.audio.GameAudioController;

/**
 * Enemy2 is a stationary enemy that shoots projectiles in a fixed direction every few frames.
 */
public class Enemy2 extends Enemy {
    private int shootDelay = 15; // Number of frames between each shot
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

        // Compute the target cell based on direction
        switch (direction) {
            case LEFT -> projCoord.decrCol();
            case RIGHT -> projCoord.incrCol();
            case UP -> projCoord.decrRow();
            case DOWN -> projCoord.incrRow();
            default -> { return; }
        }

        // If the creature is directly in the next cell, it is killed immediately.
        GameConstants.Block block = Model.getInstance().getGame().blockAt(projCoord);
        if (block == GameConstants.Block.CREATURE) {
            Model.getInstance().getGame().end(false); // Kill
            GameAudioController.getInstance().playSfx("hitShot");
        }
        // Create and add the new projectile to the game
        Projectile proj = new Projectile(projCoord, direction);
//        Model.getInstance().getGame().addEntity(proj);
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
