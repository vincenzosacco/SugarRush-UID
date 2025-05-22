package model.game.entities;

import model.game.Constants;
import model.game.utils.Cell;


public abstract class Entity{
    protected Cell coord;

    /**
     * Retrieves the block type associated with the entity.
     * The block type represents the specific kind of block that the entity occupies or is represented as
     * within the game's environment.
     *
     * @return the block type of the entity, as an instance of {@link Constants.Block}
     */
    public abstract Constants.Block blockType();

    public Entity(int row, int col){
        this.coord = new Cell(row,col);
    }

    public Entity(Cell coord){
        this.coord = coord;
    }

    public Entity() {
        // A default cell is safe because it can be instantiated, but no operations can be done on it.
        // Using this allows declaring a final Cell field and setting its coordinates later,
        // but without the risk of making operations on it before coordinates are set.
        this.coord = new Cell();
    }

    /**
     * Retrieves a copy of the current coordinate.
     * @return a new {@code Cell} instance representing the current coordinates of the entity
     */
    public Cell getCoord() {
        return new Cell(coord);
    }

    /**
     * Computes but not executes the action or behavior specific to the entity.
     * This method is intended to be overridden by subclasses
     * @return
     */
    public abstract Cell computeAction();

    public abstract boolean manageCollision(Constants.Block block);

    /**
     * Executes the action or behavior specific to the entity
     * and then update his internal state (not the Game state).
     * This method must be defined by subclasses to provide their concrete implementation of the action.
     */
    public abstract void performAction(Cell newCoord);


}
