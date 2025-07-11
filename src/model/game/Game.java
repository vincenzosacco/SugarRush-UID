package model.game;

import controller.GameLoop;
import model.game.entities.Creature;
import model.game.entities.evil.Projectile;
import model.game.utils.Cell;
import model.profile.ProfileManager;
import view.View;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static config.ModelConfig.COL_COUNT;
import static config.ModelConfig.ROW_COUNT;

public class Game {
    /**
     * @see GameMatrix
     */
    // package visibility needed for MapParser
    final GameMatrix gameMat = new GameMatrix();
    final ArrayList<Entity> entities = new ArrayList<>();

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
     * @see GameConstants.Block
     * @see GameMatrix#makeReadOnly()
     */
    private final List<List<GameConstants.Block>> gameMatRO = gameMat.makeReadOnly();

    // experimental approach
    private final List<Entity> entitiesRO = Collections.unmodifiableList(entities);
    // Coins of the current level
    private Boolean[] coinsCollected;

    public void setCoinsCollected(int pos){
        if(!this.coinsCollected[pos]){
            this.coinsCollected[pos]=true;
            addStar();
        }
    }

    private int currLevel;

    public int getCurrLevel() {
        return currLevel;
    }

    public Game() {
        // LOAD MAP FROM RESOURCE
        //default
        currLevel=1;
        coinsCollected=ProfileManager.getLastProfile().getLevelStarsCount(currLevel);
    }
    //set the current map of the level
    public void setLevel(int index){
        currLevel=index;
        coinsCollected=ProfileManager.getLastProfile().getLevelStarsCount(currLevel);
        this.starCount=0;

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
    public void addStar(){
        starCount++;
        if (starCount > 3) {
            throw new IllegalStateException("Star count cannot exceed 3. Current count: " + starCount);
        }

    }

    public int getStarCount(){
        return this.starCount;
    }

    // MODEL //

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
        return gameMatRO;
    }

    public List<Entity> getEntities() {
        return entitiesRO;
    }

    // Method to add the Projectile
    public void addEntity(Entity e){
        entities.add(e);
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
            if (ent.shouldPerform()) {
                // CLEAN OLD MATRIX CELL OF THE ENTITY IN THE MATRIX//
                Cell oldCoord = ent.getCoord();
                gameMat.setCell(oldCoord, GameConstants.Block.SPACE);

                // 1- COMPUTE ENTITIES ACTION  //
                Cell toMove = ent.computeAction(); // tell the entity where he wants to move

                // 2- MANAGE COLLISIONS //
                boolean canPerform = ent.manageCollision(gameMat.getCell(toMove), toMove );

                // 3- PERFORM ACTION //
                if (canPerform) ent.performAction(toMove); // if the entity moves, this will update its coordinates

                // APPLY NEW COORDS IN THE GAME MATRIX //
                Cell newCoord = ent.getCoord();
                gameMat.setCell(newCoord, ent.blockType());

                if (!canPerform && ent instanceof Projectile){
                    // Free the cell and remove the projectile
                    gameMat.setCell(ent.getCoord(), GameConstants.Block.SPACE);
                    entities.remove(ent);
                }
            }
        }
    }


    /**@return the block at the specified cell in the game matrix*/
    public GameConstants.Block blockAt(Cell cell) {
        if (cell.getRow() < 0 || cell.getRow() >= ROW_COUNT ||
                cell.getCol() < 0 || cell.getCol() >= COL_COUNT) {
            return GameConstants.Block.SPACE;
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


    /** Sets the direction of the creature. On the next {@link #updateState()} call,
     * the creature will move in the specified direction.
     * @param direction the direction to set for the creature
     */
    public void setCreatureDirection(GameConstants.Direction direction) {
        Creature creature = null;
        for (Entity entity : entities) {
            if (entity instanceof Creature) {
                creature = (Creature) entity;
            }
        }
        assert creature != null : "Creature should not be null";

        if (! creature.isMoving()) {
            creature.setDirection(direction);
        }
    }

    public void killCreature() {
        SwingUtilities.invokeLater(() -> {
            // Reset if lost
            this.coinsCollected=ProfileManager.getLastProfile().getLevelStarsCount(currLevel);
            starCount=0;

            GameLoop.getInstance().pauseGameTimer();
            View.getInstance().getGamePanel().endGame();
            View.getInstance().getGamePanel().loseLevel();
        });
    }


    public void clearGameMatrix() {
        // Clear the game matrix
        gameMat.clear();
        for (int r = 0; r < ROW_COUNT; r++) {
            ArrayList<GameConstants.Block> row = new ArrayList<>(COL_COUNT);
            for (int c = 0; c < COL_COUNT; c++) {
                row.add(GameConstants.Block.SPACE);
            }
            gameMat.add(row);
        }
        // Clear all entities
        entities.clear();
    }


    public void win(){
        SwingUtilities.invokeLater(() -> {
            GameLoop.getInstance().pauseGameTimer();

            // Set the coins taken in this level and save them
            GameLoop.getInstance().controlTime();
            ProfileManager.getLastProfile().setLevelStarsCount(this.currLevel,this.coinsCollected);

            View.getInstance().getGamePanel().endGame();
            View.getInstance().getGamePanel().winLevel();

            // Adds the coins
            ProfileManager.getLastProfile().sumCoins(getStarCount()*100); // 100 coins for each star
            ProfileManager.getLastProfile().sumCoins(10); //10 coins every time the creature reaches the candy (each victory)
            View.getInstance().getCustomTabbedPane().getShopPanel().updateCoins(ProfileManager.getLastProfile().getCoins());
        });
    }
}