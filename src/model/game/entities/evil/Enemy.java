package model.game.entities.evil;

import model.game.Constants;
import model.game.Entity;
import model.game.utils.Cell;

abstract class Enemy extends Entity {
    /** Direction in which the enemy would move*/
    protected static Constants.Direction DIRECTION = Constants.Direction.NONE;

    public Enemy(Cell coord) {
        super(coord);
    }

    public Enemy(int row, int col) {
        super(row,col);
    }

    public Constants.Direction getDirection() {
        return DIRECTION;
    }
}
