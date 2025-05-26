package model.game.entities.evil;

import model.game.Entity;
import model.game.utils.Cell;

abstract class Enemy extends Entity {
    public Enemy(Cell coord) {
        super(coord);
    }

    public Enemy(int row, int col) {
        super(row,col);
    }

//    /**
//     * Defines the movement behavior of an enemy entity.
//     * This method must be implemented by subclasses of the Enemy class
//     * to specify the actions the enemy takes to change its position or state.
//     */
//    public abstract void performMove();
}
