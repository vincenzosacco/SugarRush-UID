package model.game;

import controller.GameLoop;
import model.game.entities.Creature;
import model.game.utils.Cell;
import view.View;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static config.Model.COL_COUNT;
import static config.Model.ROW_COUNT;

public class Game {
    /**
     * @see GameMatrix
     */
    // package visibility needed for MapParser
    final GameMatrix gameMat = new GameMatrix();
    final ArrayList<Entity> entities = new ArrayList<>();


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

    // experimental approach
    private final List<Entity> entitiesRO = Collections.unmodifiableList(entities);

    public Game() {
        // LOAD MAP FROM RESOURCE
        //MapParser.loadMap(MapParser.MAP_1, this); // update map related fields
    }
    //set the current map of the level
    private int currentLevel ;
    public void setLevel(int index){
        if (index==1){
            MapParser.loadMap(MapParser.MAP_1, this);
        } else if (index==2){
            MapParser.loadMap(MapParser.MAP_2, this);
        } else if (index==3){
            MapParser.loadMap(MapParser.MAP_3, this);
        } else if (index==4){
            MapParser.loadMap(MapParser.MAP_4, this);
        } else if (index==5){
            MapParser.loadMap(MapParser.MAP_5, this);
        }else if (index==6){
            MapParser.loadMap(MapParser.MAP_6, this);
        }
        currentLevel  = index;
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

    public List<Entity> getEntities() {
        return entitiesRO;
    }

    /**
     * <b>CRITICAL METHOD</b> -> this method is called many times(based on FPS) per second.
     * <p>
     * Updates the current state of the game by performing several critical operations
     * in sequential order. This method represents the core logic for maintaining and
     * refreshing the game map and entities to reflect the latest game state.
     * </p>
     * The operations include:
     * <p>
     * 2. Executing actions for all active game entities. Each entity's specific action is
     * performed via its {@code performAction} method.
     * </p>
     * <p>
     * 3. Resolving any interactions or collisions between entities and the game environment.
     * </p>
     * <p>
     * 4. Updating the game matrix with the new positions and block types of all entities.
     * </p>
     * This method is central to the game's functionality and should be invoked regularly
     * to keep the game running smoothly. Modifications to this method should ensure the
     * preservation of each step's intended functionality.
     */
    public void updateState() {
        for (Entity ent : entities) {
            if (ent.shouldPerform()) {
                // CLEAN OLD MATRIX CELL OF THE ENTITY IN THE MATRIX//
                gameMat.setCell(ent.getCoord(), Constants.Block.SPACE); // remove the entity from the old position

                // 1- COMPUTE ENTITIES ACTION  //
                Cell toMove = ent.computeAction(); // tell the entity where he wants to move
                // 2 -MANAGE COLLISIONS //
                boolean canPerform = ent.manageCollision(gameMat.getCell(toMove));
                // 3 -PERFORM ACTION //
                if (canPerform) ent.performAction(toMove);

                // APPLY NEW COORDS IN THE GAME MATRIX //
                gameMat.setCell(ent.getCoord(), ent.blockType());
            }
        }
    }


    /**
     * Checks if in {@code cell} coordinates contains a block of type {@code blockType}.
     * @param cell the cell to check
     * @param blockType the type of block to compare against
     * @return true if the cell contains the specified block type, false otherwise
     */
    public boolean isBlock(Cell cell, Constants.Block blockType) {
        return gameMat.getCell(cell) == blockType;
    }

    /**@return the block at the specified cell in the game matrix*/
    public Constants.Block blockAt(Cell cell) {
        return gameMat.getCell(cell);
    }


    // GAME ACTIONS //
    private final GameLoop gameLoop = GameLoop.getInstance();

    public boolean isRunning(){
        return gameLoop.isRunning();
    }
    public void start(){
        gameLoop.start();
    }
    public void stop(){
        gameLoop.stop();
    }



    private Creature getCreature(){
        for (Entity entity : entities) {
            if (entity instanceof Creature creature) {
                return creature;
            }
        }
        throw new AssertionError("Creature not found in the game entities. This should never happen.");
    }

    /** Sets the direction of the creature. On the next {@link #updateState()} call,
     * the creature will move in the specified direction.
     * @param direction the direction to set for the creature
     */
    public void setCreatureDirection(Constants.Direction direction) {
        getCreature().setDirection(direction);
    }

    public void killCreature() {
        SwingUtilities.invokeLater(() -> {
            View.getInstance().getGamePanel().endGame();
        });
    }
    public void clearGameMatrix() {
        // Clear the game matrix
        for (int row = 0; row < ROW_COUNT; row++) {
        for (int col = 0; col < COL_COUNT; col++) {
                gameMat.setCell(new Cell(row, col), Constants.Block.SPACE);
            }
        }
        // Clear all entities
        entities.clear();
    }

    public void restart() {
        // Clear the game matrix
        clearGameMatrix();
        // Reset the game loop
        gameLoop.stop();
        gameLoop.start();

        // Reload the level (if needed)
        setLevel(currentLevel); // or any other level you want to start with
    }
}