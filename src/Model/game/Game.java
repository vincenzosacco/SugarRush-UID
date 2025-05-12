package Model.game;

import Model.IModel;

import java.util.List;

/**
 * Singleton class represents the Map game instance
 */
public class Game implements IModel {
    private final GameMatrix gameMat = new GameMatrix();

    /**
     * <p>
     * Contains a read-only view of the game's map matrix.
     * This variable is a two-dimensional, unmodifiable structure backed by the internal {@code GameMatrix},
     * where each element represents a {@code Block} type.
     * The {@code Block} represents game-specific elements like walls, empty spaces, the creature, and sugar pieces.
     * </p>
     * <p>
     * The list is immutable, ensuring external code cannot modify the game's map. This is essential to maintain
     * the integrity of the game state during gameplay. Changes to the map are managed internally within the
     * {@code Game} class logic.
     * </p>
     * @see Constants.Block
     * @see GameMatrix#makeReadOnly()
     */
    public final List<List<Constants.Block>> gameMatView = gameMat.makeReadOnly();

    // Singleton //
    private static Game instance = null;
    public static Game getInstance(){
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    private Game(){
        // LOAD MAP FROM RESOURCE
        MapParser.loadMap(MapParser.MAP_1, gameMat); // update map related fields
    }

    // GAME ACTIONS //
     /**
     * @see GameMatrix#moveCreature(int)
     */
    public boolean moveCreature(Constants.Direction direction){
        return gameMat.moveCreature(direction);
    }

    // GETTERS
    // Map
//    final GameMatrix gameMat = new GameMatrix();
//
//    /**
//     * Provides a read-only view of the game's map matrix. This variable is a two-dimensional, unmodifiable structure
//     * backed by the game's internal {@code GameMatrix}, where each element represents a {@code Block} type.
//     * The {@code Block} enumeration is used to specify the nature of each position in the map, such as walls, spaces,
//     * the creature's location, or the sugar piece.
//     *
//     * This list is immutable and is designed to prevent modifications to the game's map matrix outside the
//     * {@code Game} class logic. It ensures that the map can be inspected but not altered, maintaining data integrity during gameplay.
//     *
//     * @see Block
//     * @see Game
//     * @implNote unmodifiable reference to {@code gameMat} field, created using {@link GameMatrix#readOnly()}
//     * */
//    public final List<List<Block>> gameMatReadOnly = gameMat.readOnly();


}
