package model.game;


import model.game.Constants.Block;
import model.game.utils.Cell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static config.ModelConfig.COL_COUNT;
import static config.ModelConfig.ROW_COUNT;

/**
 * Represents the game game matrix used to define the layout and structure of the game's environment.
 * It is an alias for a two-dimensional {@code ArrayList<ArrayList<Game.Block>>}, where each inner list
 * represents a row of {@code Block} elements. The {@code Block} type identifies specific elements in the game,
 * such as spaces, walls, the creature, or sugar.
 * <p>
 * This variable acts as a mutable representation of the game, which can be cleared or updated as needed (e.g.,
 * during game loading or game resets).
 * @see Block
 * @see GameMatrix
 */
class GameMatrix extends ArrayList<ArrayList<Block>> {
    /**
     * Instantiate and return a read-only view of the game matrix.
     * The returned list is an unmodifiable representation of the current game state,
     * where each element corresponds to a {@code Block} in the game's matrix.
     *
     * @return an unmodifiable list of lists containing {@code Block} elements, representing the game matrix
     * @apiNote This method ensures that the internal structure of the game matrix cannot be altered externally.
     * @see Collections#unmodifiableList(List)
     * @see <a href="https://docs.oracle.com/en/java/javase/11/core/creating-immutable-lists-sets-and-maps.html#GUID-DD066F67-9C9B-444E-A3CB-820503735951">Help Guide</a>
    */
     List<List<Block>> makeReadOnly(){
        return Collections.unmodifiableList(this);
    }


    /** Alias for {@code get(row).get(col)} */
    public Block getCell(Cell coord){
        return get(coord.getRow()).get(coord.getCol());
    }

    /** Alias for {@code get(row).set(col, block)} */
    public void setCell(Cell coord, Block block){
        get(coord.getRow()).set(coord.getCol(), block);
    }
    // Constructor to initialize the array with the correct dimensions
    public GameMatrix() {
        for (int r = 0; r < ROW_COUNT; r++) {
            ArrayList<Block> row = new ArrayList<>(COL_COUNT);
            for (int c = 0; c < COL_COUNT; c++) {
                row.add(Block.SPACE); // Initialize all cells as SPACE
            }
            this.add(row);
        }
    }
}

