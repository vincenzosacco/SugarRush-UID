package model.game;


import model.game.utils.Cell;
import model.game.Constants.Direction;
import model.game.Constants.Block;

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
 * @see Block
 * @see GameMatrix
 */
class GameMatrix extends ArrayList<ArrayList<Block>> {

    // MOVEMENT //
    final Cell creatureCell = new Cell(); // coord are setted to null

    /**
     * Moves the creature by 1 block in the specified direction if the movement is valid.
     * A move is considered valid if the new position is within the bounds of the game matrix
     * and the destination block type is either SPACE or SUGAR.
     * If the move is successful, the creature's position is updated in the game matrix.
     *
     * @param direction the direction in which the creature should move, which must be one of the predefined constants
     *                  (UP, DOWN, LEFT, RIGHT)
     * @return The Block type at the new position if the move was successful, null otherwise
     * @throws IllegalArgumentException if the provided direction is invalid
     */
     Block moveCreature(Direction direction){
        int newRow = creatureCell.getRow();
        int newCol = creatureCell.getCol();

        switch (direction) {
            case Direction.UP -> --newRow; // going up means decrementing the row index by 1
            case Direction.DOWN -> ++newRow; // going down means incrementing the row index by 1
            case Direction.LEFT -> --newCol; // going left means decrementing the col index by 1
            case Direction.RIGHT -> ++newCol; // going right means incrementing the col index by 1
            default -> throw new IllegalArgumentException("Invalid direction: " + direction);
        }

        // CHECK IF MOVEMENT IS LEGAL //
        // if it's in the matrix range:
        if (newRow >= 0 && newRow < this.size() && newCol >= 0 && newCol < this.get(newRow).size()){
            // if it's a THORNS:
            if(this.get(newRow).get(newCol) == Block.THORNS){
                //you die
                //temporarily if you touch it the application closes
                System.exit(0);
            }
            // if it's a SPACE or SUGAR :
            if (this.get(newRow).get(newCol) != Block.WALL){
                // here move is legal
                // MOVE CREATURE //
                this.get(creatureCell.getRow()).set(creatureCell.getCol(), Block.SPACE); // reset the old creature coordinates
                this.get(newRow).set(newCol, Block.CREATURE); // set the new creature coordinates
                creatureCell.setCoord(newRow, newCol); // update the fast reference to creature's position

                return this.get(newRow).get(newCol) ; // return the new block type
            }
        }
        return null;
    }


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
}

