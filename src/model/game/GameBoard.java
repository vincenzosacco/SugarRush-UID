package model.game;

import model.game.utils.Cell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static config.ModelConfig.COL_COUNT;
import static config.ModelConfig.ROW_COUNT;

public class GameBoard {
    public GameBoard(){

    }

    // package visibility needed for MapParser
    final _GameMatrix matrix = new _GameMatrix();
    final ArrayList<Entity> entities = new ArrayList<>();


    // GETTERS //
    /**
     * <p>
     * Contains a read-only view of the game's game matrix.
     * This variable is a two-dimensional, unmodifiable structure backed by the internal {@code GameMatrix},
     * where each element represents a {@code Block} type.
     * The {@code Block} represents game-specific elements like walls, empty spaces, the creature, and sugar pieces.
     * </p>
     *
     * @implNote The list is immutable, ensuring external code cannot modify the game's game. This is essential to maintain
     * the integrity of the game state during gameplay. Changes to the game are managed internally within the
     * {@code Game} class logic.
     * @see GameConstants.Block
     * @see _GameMatrix#makeReadOnly()
     */
    private final List<List<GameConstants.Block>> matrixRO = matrix.makeReadOnly();
    /**
     * Retrieves the current state of the game as a two-dimensional, read-only list of blocks.
     * Each block represents a specific element within the game's game, such as walls, spaces,
     * the creature, or collectible items. Access is synchronized to ensure thread-safe state retrieval.
     *
     * @return a two-dimensional unmodifiable list where each element is a {@code Constants.Block}
     * representing the current state of the game game.
     * @see GameConstants.Block
     */
    public List<List<GameConstants.Block>> getState() {
        return matrixRO;
    }



    private final List<Entity> entitiesRO = Collections.unmodifiableList(entities);
    public List<Entity> getEntities() {return entitiesRO;}


    // UTILITY METHODS //
    /**@return the block at the specified cell in the game matrix*/
    public GameConstants.Block blockAt(Cell cell) {
        if (cell.getRow() < 0 || cell.getRow() >= ROW_COUNT ||
                cell.getCol() < 0 || cell.getCol() >= COL_COUNT) {
            return GameConstants.Block.SPACE;
        }
        return matrix.getCell(cell);
    }

    /** Clears the game board by resetting the game matrix and removing all entities.*/
    void clear() {
        matrix.clear();
        entities.clear();
    }
}