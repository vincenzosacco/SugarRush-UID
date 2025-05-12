package Model.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the game map matrix used to define the layout and structure of the game's environment.
 * It is an alias for a two-dimensional {@code ArrayList<ArrayList<Game.Block>>}, where each inner list
 * represents a row of {@code Block} elements. The {@code Block} type identifies specific elements in the game,
 * such as spaces, walls, the creature, or sugar.
 * <p>
 * This variable acts as a mutable representation of the map, which can be cleared or updated as needed (e.g.,
 * during map loading or game resets).
 *
 * @see Game.Block
 * @see GameMatrix
 */
public class GameMatrix extends ArrayList<ArrayList<Game.Block>> {
    /**
     * @return an unmodifiable copy of the matrix
     * @see Collections#unmodifiableList(List)
     * @see <a href="https://docs.oracle.com/en/java/javase/11/core/creating-immutable-lists-sets-and-maps.html#GUID-DD066F67-9C9B-444E-A3CB-820503735951">Help Guide</a>
     */
    public List<List<Game.Block>> readOnly(){
        return Collections.unmodifiableList(this);
    }
}
