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

    //list of the entities to remove
    private final List<Entity> entitiesToRemove = new ArrayList<>();

    // count star
    private int starCount = 0;

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
     * @see Constants.Block
     * @see GameMatrix#makeReadOnly()
     */
    private final List<List<Constants.Block>> gameMatRO = gameMat.makeReadOnly();

    // experimental approach
    private final List<Entity> entitiesRO = Collections.unmodifiableList(entities);

    private int currLevel;

    public int getCurrLevel() {
        return currLevel;
    }

    public Game() {
        // LOAD MAP FROM RESOURCE
        //MapParser.loadMap(MapParser.MAP_1, this); // update map related fields
        //default
        currLevel=1;

    }
    //set the current map of the level
    public void setLevel(int index){
        currLevel=index;

        switch (index) {
            case 1: MapParser.loadMap(MapParser.MAP_1, this); break;
            case 2: MapParser.loadMap(MapParser.MAP_2, this); break;
            case 3: MapParser.loadMap(MapParser.MAP_3, this); break;
            case 4: MapParser.loadMap(MapParser.MAP_4, this); break;
            case 5: MapParser.loadMap(MapParser.MAP_5, this); break;
            case 6: MapParser.loadMap(MapParser.MAP_6, this); break;
            default:
                System.err.println("Level not supported: " + index + ". Loading MAP_1 as fallback.");
                MapParser.loadMap(MapParser.MAP_1, this);
                break;
        }
    }

    /**
     * This method updates the star count
     */
    public void addstar(){
        starCount++;
    }

    public int getStarCount(){
        return starCount;
    }

    // MODEL //

    /**
     * Retrieves the current state of the game as a two-dimensional, read-only list of blocks.
     * Each block represents a specific element within the game's game, such as walls, spaces,
     * the creature, or collectible items. Access is synchronized to ensure thread-safe state retrieval.
     *
     * @return a two-dimensional unmodifiable list where each element is a {@code Constants.Block}
     * representing the current state of the game game.
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
     * refreshing the game game and blocks to reflect the latest game state.
     * </p>
     * The operations include:
     * <p>
     * 2. Executing actions for all active game blocks. Each entity's specific action is
     * performed via its {@code performAction} method.
     * </p>
     * <p>
     * 3. Resolving any interactions or collisions between blocks and the game environment.
     * </p>
     * <p>
     * 4. Updating the game matrix with the new positions and block types of all blocks.
     * </p>
     * This method is central to the game's functionality and should be invoked regularly
     * to keep the game running smoothly. Modifications to this method should ensure the
     * preservation of each step's intended functionality.
     */
    public void updateState() {
        List<Entity> currentEntities = new ArrayList<>(entities);
        for (Entity ent : currentEntities) {
            // If the entity has already been marked for removal, skip it in this update cycle.
            if (entitiesToRemove.contains(ent)) {
                continue;
            }
            if (ent.shouldPerform()) {
                // CLEAN OLD MATRIX CELL OF THE ENTITY IN THE MATRIX//
                Cell oldCoord = ent.getCoord();
                if (oldCoord.getRow() >= 0 && oldCoord.getRow() < ROW_COUNT &&
                        oldCoord.getCol() >= 0 && oldCoord.getCol() < COL_COUNT) {
                    gameMat.setCell(oldCoord, Constants.Block.SPACE);
                } else {
                    System.err.println("Warning: Entity with previous coordinate invalid. Skipped cleaning old cell:" + oldCoord);
                }

                // 1- COMPUTE ENTITIES ACTION  //
                Cell toMove = ent.computeAction(); // tell the entity where he wants to move
                // LIMIT CHECK FOR toMove
                if (toMove.getRow() < 0 || toMove.getRow() >= ROW_COUNT ||
                        toMove.getCol() < 0 || toMove.getCol() >= COL_COUNT) {
                    if (ent instanceof Creature) {
                        System.out.println("Creature is attempting to move out of bounds. Creature kill.");
                        killCreature();
                    }
                    continue; // Move to the next entity in the loop
                }
                // 2 -MANAGE COLLISIONS //
                boolean canPerform = ent.manageCollision(gameMat.getCell(toMove), toMove );
                // 3 -PERFORM ACTION //
                if (canPerform) ent.performAction(toMove);

                // APPLY NEW COORDS IN THE GAME MATRIX //
                Cell newCoord = ent.getCoord();
                if (newCoord.getRow() >= 0 && newCoord.getRow() < ROW_COUNT &&
                        newCoord.getCol() >= 0 && newCoord.getCol() < COL_COUNT) {
                    gameMat.setCell(newCoord, ent.blockType());
                } else {
                    System.err.println("Error: The entity has moved to an invalid coordinate:" + newCoord);
                    if (ent instanceof Creature) {
                        killCreature();
                    }
                }
            }
        }
        // Perform actual removals after iteration
        if (!entitiesToRemove.isEmpty()) {
            entities.removeAll(entitiesToRemove);
            entitiesToRemove.clear();
        }
    }


    /**
     * Checks if in {@code cell} coordinates contains a block of type {@code blockType}.
     * @param cell the cell to check
     * @param blockType the type of block to compare against
     * @return true if the cell contains the specified block type, false otherwise
     */
    public boolean isBlock(Cell cell, Constants.Block blockType) {
        if (cell.getRow() < 0 || cell.getRow() >= ROW_COUNT ||
                cell.getCol() < 0 || cell.getCol() >= COL_COUNT) {
            return false;
        }
        return gameMat.getCell(cell) == blockType;
    }

    /**@return the block at the specified cell in the game matrix*/
    public Constants.Block blockAt(Cell cell) {
        if (cell.getRow() < 0 || cell.getRow() >= ROW_COUNT ||
                cell.getCol() < 0 || cell.getCol() >= COL_COUNT) {
            return Constants.Block.SPACE;
        }
        return gameMat.getCell(cell);
    }


    // GAME ACTIONS //
    private boolean isStarted = false;
    public boolean isStarted() {
        return isStarted;
    }
    public void start(){
        if (!isStarted) isStarted = true;
    }


    private Creature getCreature(){
        for (Entity entity : entities) {
            if (entity instanceof Creature creature) {
                return creature;
            }
        }
        throw new AssertionError("Creature not found in the game blocks. This should never happen.");
    }
    /** Sets the direction of the creature. On the next {@link #updateState()} call,
     * the creature will move in the specified direction.
     * @param direction the direction to set for the creature
     */
    public void setCreatureDirection(Constants.Direction direction) {
        Creature creature = getCreature();
        if (creature != null) {
            creature.setDirection(direction);
        }
    }

    public void killCreature() {
        Creature creature = getCreature();
        removeEntity(creature);
        SwingUtilities.invokeLater(() -> {
            GameLoop.getInstance().pauseGameTimer();
            View.getInstance().getGamePanel().endGame();
            View.getInstance().getGamePanel().loseLevel();
        });
    }

    public void removeEntity(Entity entity) {
        if (entity != null && !entitiesToRemove.contains(entity)) {
            entitiesToRemove.add(entity); // Mark for removal
        }
    }

    public void clearGameMatrix() {

        // Clear the game matrix
        gameMat.clear();
        for (int r = 0; r < ROW_COUNT; r++) {
            ArrayList<Constants.Block> row = new ArrayList<>(COL_COUNT);
            for (int c = 0; c < COL_COUNT; c++) {
                row.add(Constants.Block.SPACE);
            }
            gameMat.add(row);
        }
        // Clear all entities
        entities.clear();
        entitiesToRemove.clear(); // Also clears the list of entities to remove
    }
    public void win(){
        SwingUtilities.invokeLater(() -> {

            // get time from timer
            int elapsedSeconds = GameLoop.getInstance().getElapsedSeconds();
            if (elapsedSeconds < 30) {
                addstar();
            }

            GameLoop.getInstance().pauseGameTimer();
            View.getInstance().getGamePanel().endGame();
            View.getInstance().getGamePanel().winLevel();
        });
    }

    public void setBlockAt(Cell coord, Constants.Block block) {
        gameMat.setCell(coord, block);
    }
}