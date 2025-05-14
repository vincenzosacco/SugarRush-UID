package model.game;

import model.IModelObj;
import view.View;

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
    private final GameMatrix gameMat = new GameMatrix();

    /**
     * A lock instance used to manage concurrent access to the game state.
     * Designed to be used for synchronizing game state modifications, <b>preventing race conditions</b>
     * during operations such as moving the creature, updating the game map, or retrieving game data.
     * <i>EXAMPLE</i>:
     * What happens if {@code gameMat} is updated when it has been reading?
     * This lock ensures that the game state remains consistent and prevents race conditions
     * by allowing only one thread to access or modify {@code gameMat} at a time.
     * <p>
     * How?
     * Simply all methods that either update or return {@code gameMat} ( or a reference to it)
     * must acquire the {@code stateLock} before proceeding. This guarantees exclusive access
     * during critical operations.
     * </p>

     */
    private final Object stateLock = new Object();

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
        MapParser.loadMap(MapParser.MAP_1, gameMat); // update map related fields
    }

    void restart(){
        MapParser.loadMap(MapParser.MAP_1, gameMat);
    }
    // GAME ACTIONS //

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
            // the caller Thread need to acquire lock to move creature
            synchronized (stateLock) {
                newPosBlock = gameMat.moveCreature(direction);
            }
            // Here RenderLoop can call <getGameState()> to update the view
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

    // GAME GETTERS //

    /**
     * Retrieves the current state of the game as a two-dimensional, read-only list of blocks.
     * Each block represents a specific element within the game's map, such as walls, spaces,
     * the creature, or collectible items. Access is synchronized to ensure thread-safe state retrieval.
     *
     * @return a two-dimensional unmodifiable list where each element is a {@code Constants.Block}
     * representing the current state of the game map.
     * @see Constants.Block
     */
    public List<List<Constants.Block>> getGameState() {
        synchronized (stateLock) {
            return gameMatRO;
        }
    }
}