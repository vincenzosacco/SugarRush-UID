package model.game.entities.evil;

import model.game.Constants;
import model.game.Entity;
import model.game.utils.Cell;

public abstract class Enemy extends Entity {
    public Enemy(Cell coord) {
        super(coord);
    }

    public Enemy(int row, int col) {
        super(row,col);
    }


}
