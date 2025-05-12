package Model.game;

import Model.IModel;
import utils.Cell;

import java.util.List;


/**
 * Singleton class represents the Map game instance
 */
public class Game implements IModel {


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
        mapParser= new MapParser(this);
        mapParser.loadMap(MapParser.MAP_1); // update map related fields
    }

    // MOVEMENT //
    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;

    /**
     * Moves the creature in the specified direction if the movement is valid.
     * A move is considered valid if the new position is within the bounds of the game matrix
     * and the destination block type is either SPACE or SUGAR.
     * If the move is successful, the creature's position is updated in the game matrix.
     *
     * @param direction the direction in which the creature should move, which must be one of the predefined constants
     *                  (UP, DOWN, LEFT, RIGHT)
     * @return true if the creature successfully moves to the new position, otherwise false
     * @throws IllegalArgumentException if the provided direction is invalid
     */
    public boolean moveCreature(int direction){
        int newRow = creatureCell.getRow();
        int newCol = creatureCell.getCol();

        switch (direction) {
            case UP -> --newRow; // going up means decrementing row index by 1
            case DOWN -> ++newRow; // going down means incrementing row index by 1
            case LEFT -> --newCol; // going left means decrementing col index by 1
            case RIGHT -> ++newCol; // going right means incrementing col index by 1
            default -> throw new IllegalArgumentException("Invalid direction: " + direction);
        }

        // CHECK IF MOVEMENT IS LEGAL //
        // if it's in matrix range :
        if (newRow > 0 && newRow < gameMat.size() && newCol > 0 && newCol < gameMat.get(newRow).size()){
            // if it's a SPACE or SUGAR :
            if (gameMat.get(newRow).get(newCol) == Block.SPACE || gameMat.get(newRow).get(newCol) == Block.SUGAR){
                // HERE MOVE IS LEGAL

                // MOVE CREATURE //
                gameMat.get(creatureCell.getRow()).set(creatureCell.getCol(), Block.SPACE); // reset the old creature coordinates
                gameMat.get(newRow).set(newCol, Block.CREATURE); // set the new creature coordinates
                creatureCell.setCoord(newRow, newCol); // update fast reference to creature's position'

                return true;
            }
        }
        return false;
    }

    // Map
    final MapParser mapParser;
    public enum Block {SPACE, WALL, CREATURE, SUGAR}
    final GameMatrix gameMat = new GameMatrix();

    /**
     * Provides a read-only view of the game's map matrix. This variable is a two-dimensional, unmodifiable structure
     * backed by the game's internal {@code GameMatrix}, where each element represents a {@code Block} type.
     * The {@code Block} enumeration is used to specify the nature of each position in the map, such as walls, spaces,
     * the creature's location, or the sugar piece.
     *
     * This list is immutable and is designed to prevent modifications to the game's map matrix outside the
     * {@code Game} class logic. It ensures that the map can be inspected but not altered, maintaining data integrity during gameplay.
     *
     * @see Game.Block
     * @see Game
     * @implNote unmodifiable reference to {@code gameMat} field, created using {@link GameMatrix#readOnly()}
     * */
    public final List<List<Block>> gameMatReadOnly = gameMat.readOnly();

    /**
     * Just a fast reference to Creature's position within the game grid.
     */
    final Cell creatureCell = new Cell();

    /**
     * Returns the type of the block at the specified coordinates in the game matrix.
     * The returned value corresponds to the {@code Block} enumeration.
     *
     * @apiNote the value returned by this method should be compared with the {@code Block} enumeration values.
     * <p></p><i>EXAMPLE<i/>: {@code if (game.isBlock(i,j) == Block.CREATURE) ...}
     *
     * @param i the row index of the block in the game matrix
     * @param j the column index of the block in the game matrix
     * @return the ordinal value of the block type at the specified location
     */
    public int isBlock(int i, int j){
        return gameMat.get(i).get(j).ordinal(); // ordinal() returns the index of the element in the enumerated type.
    }


}
