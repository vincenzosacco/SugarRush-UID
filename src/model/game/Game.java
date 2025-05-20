package model.game;

import model.IModelObj;
import model.game.entities.Creature;
import model.game.entities.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Core game model that manages the game state and logic.
 * This singleton class handles the game map, creature movement, and provides thread-safe access
 * to the game state through synchronized methods. It serves as the central model for maintaining
 * game data integrity during concurrent operations like rendering and state updates.
 */
public class Game implements IModelObj {
    /**
     * @see GameMatrix
     */
    // package visibility needed for MapParser
    final GameMatrix gameMat = new GameMatrix();
    final ArrayList<Entity> entities = new ArrayList<>() ;

    /**
     * <p>
     * Contains a read-only view of the game's map matrix.
     * This variable is a two-dimensional, unmodifiable structure backed by the internal {@code GameMatrix},
     * where each element represents a {@code Block} type.
     * The {@code Block} represents game-specific elements like walls, empty spaces, the creature, and sugar pieces.
     * </p>
     *
     * @implNote The list is immutable, ensuring external code cannot modify the game's map. This is essential to maintain
     * the integrity of the game state during gameplay. Changes to the map are managed internally within the
     * {@code Game} class logic.
     * @see Constants.Block
     * @see GameMatrix#makeReadOnly()
     */
    private final List<List<Constants.Block>> gameMatRO = gameMat.makeReadOnly();

    public Game() {
        // LOAD MAP FROM RESOURCE
        MapParser.loadMap(MapParser.MAP_1, this); // update map related fields
    }

    // MODEL //
    /**
     * Retrieves the current state of the game as a two-dimensional, read-only list of blocks.
     * Each block represents a specific element within the game's map, such as walls, spaces,
     * the creature, or collectible items. Access is synchronized to ensure thread-safe state retrieval.
     *
     * @return a two-dimensional unmodifiable list where each element is a {@code Constants.Block}
     * representing the current state of the game map.
     * @see Constants.Block
     */
    public List<List<Constants.Block>> getState() {
        return gameMatRO;
    }

    public void updateState(){
        // PERFORM ENTITIES ACTION  //
        entities.forEach(Entity::performAction);

        // DETECT COLLISIONS //
        //TODO
        // RESOLVE COLLISIONS //

        // APPLY NEW COORDS IN THE GAME MATRIX //
        for (Entity entity : entities) {
            int row = entity.getCoord().getRow();
            int col = entity.getCoord().getCol();
            gameMat.get(row).set(col, entity.)
        }
    }

    // GAME ACTIONS //
    void restart(){
        MapParser.loadMap(MapParser.MAP_1, this);
    }

    /**
     * Moves the creature in the specified direction until it reaches an invalid position.
     * The movement is synchronized using stateLock to prevent concurrent modifications to the game state.
     * The creature will continue moving in the given direction until it encounters a wall or map boundary.
     *
     * @param direction The direction to move the creature (UP, DOWN, LEFT, RIGHT)
     * @throws IllegalArgumentException if an invalid direction is provided
     */
    public void performMove(Constants.Direction direction) {
        // Move until creature is on a valid position //
        Constants.Block newPosBlock = Constants.Block.SPACE; // just to init, no semantic reason

        // even if newPosBlock is never null,
        // this loop cannot be infinite because the creature can't move out of the map
        while (newPosBlock != null) {
            newPosBlock = gameMat.moveCreature(direction);
//            View.getInstance().notifyView(); // notify that the model changed
        }

    }

    /**
     * Opens the game settings panel.
     * This method triggers the settings interface and will eventually handle game state
     * changes related to settings like pausing the game timer.
     */
    public void openSetting() {
        System.out.println("Settings opened");
        // stop time
    }


}