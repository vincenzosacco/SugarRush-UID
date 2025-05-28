package model.game;

import model.game.utils.Cell;


public abstract class Entity{
    protected Cell coord;

    /**{@link model.game.Constants.Direction} in which the entity would move.
     * Defalut is NONE, which means idle. */
    protected static Constants.Direction DIRECTION = Constants.Direction.NONE;

    /**
     * @return the current direction where the entity is moving.
     * @apiNote {@link model.game.Constants.Direction} NONE means the entity is idle.
     */
    public Constants.Direction getDirection() {
        return DIRECTION;
    }

    /** Counter for the number of frames since the last move.
     * This is used to control the frequency of movement updates for the entity.
     * The entity will only move when this counter reaches the {@code moveDelay} value.
     * @see #actionDelay
     * @see #shouldPerform()
     * */
    private int frameCounter = 0;

    /**
     * This value determines how often the entity can perform its action.
     * For example, if {@code moveDelay} is set to 5, the entity will perform its action every 5 frames.
     * @apiNote  This is used to simulate different entity velocities.
     */
    private int actionDelay = 5; // Default: action every 5 frame (~24 times/sec if FPS = 120)

    // CONSTRUCTORS //

    public Entity(int row, int col){
        this.coord = new Cell(row,col);
    }

    public Entity(Cell coord){
        this.coord = coord;
    }

//    public Entity() {
//        // A default cell is safe because it can be instantiated, but no operations can be done on it.
//        // Using this allows declaring a final Cell field and setting its coordinates later,
//        // but without the risk of making operations on it before coordinates are set.
//        this.coord = new Cell();
//    }




    /**
     * Retrieves the block type associated with the entity.
     * The block type represents the specific kind of block that the entity occupies or is represented as
     * within the game's environment.
     *
     * @return the block type of the entity, as an instance of {@link Constants.Block}
     */
    public abstract Constants.Block blockType();


    /** @return a new {@code Cell} instance representing the current coordinates of the entity */
    public Cell getCoord() {
        return new Cell(coord);
    }


    // GAME MODEL UPDATE METHODS //
    /**
     * Computes but not executes the action or behavior specific to the entity.
     * This method is intended to be overridden by subclasses
     * @return a new {@code Cell} instance representing the next coordinates of the entity.
     */
    protected abstract Cell computeAction();

    /**
     * Manages the collision of the entity with a block in the game.
     * This method must implement <b>how the entity interacts with the block</b>,
     * such as whether it can pass through, destroy, or be affected by the block.
     * <p>
     * What happens in this method must affect <b>only</b> the entity's internal state.
     * </p>
     * @param block the block with which the entity is colliding
     * @return true if the entity can perform its action after the collision, false otherwise
     */
    protected abstract boolean manageCollision(Constants.Block block);

    /**
     * Executes the action or behavior specific to the entity
     * and then update his internal state (not the Game state).
     * This method must be defined by subclasses to provide their concrete implementation of the action.
     */
    protected abstract void performAction(Cell newCoord);

    /**
     * Indicates whether the entity's action is allowed or not based on the {@code frameCounter}
     * and other possible conditions strictly related to the type of entity.
     * */
    protected boolean shouldPerform() {
        frameCounter++;
        if (frameCounter >= actionDelay) {
            frameCounter = 0;
            return true;
        }
        return false;
    }
    /**
     * set {@code moveDelay}
     * */
    protected void setActionDelay(int delay) {
        if (delay <= 0) {
            throw new IllegalArgumentException("Action delay must be a positive integer.");
        }

        this.actionDelay = delay;
    }

}
