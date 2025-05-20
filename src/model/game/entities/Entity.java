package model.game.entities;

import model.game.Constants;
import model.game.utils.Cell;

public abstract class Entity {
    public abstract Constants.Block blockType();


    protected Cell coord;

    public Entity(int row, int col){
        this.coord = new Cell(row,col);
    }

    public Entity(Cell coord){
        this.coord = coord;
    }

    /**
     * Retrieves a copy of the current coordinate.
     * @return a new {@code Cell} instance representing the current coordinates of the entity
     */
    public Cell getCoord() {
        return new Cell(coord);
    }

    /**
     * Executes the action or behavior specific to the entity. This method must be
     * defined by subclasses to provide their concrete implementation of the action.
     */
    public abstract void performAction();

}
