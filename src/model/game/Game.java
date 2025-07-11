package model.game;

import controller.game.GameController;
import model.game.entities.Creature;
import model.game.utils.Cell;
import utils.audio.GameAudioController;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import static config.ModelConfig.NUM_LEVELS;

public class Game extends GameBoard {
    // SINGLETON because the application has only one game instance at a time.
    private static Game instance = null;
    private Game() {
        super();
    }
    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    // GAMELOOP  //

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
        // ASSERTIONS //
        assert !entities.isEmpty() : "Entities list should not be empty when updating state.";
        assert !matrix.isEmpty() : "Game matrix should not be null when updating state.";

        // UPDATE ENTITIES //
        List<Entity> currentEntities = new ArrayList<>(entities);
        for (Entity ent : currentEntities) {
            if (ent.shouldPerform()) {
                // CLEAN OLD MATRIX CELL OF THE ENTITY IN THE MATRIX//
                Cell oldCoord = ent.getCoord();
                matrix.setCell(oldCoord, GameConstants.Block.SPACE);

                // 1- COMPUTE ENTITIES ACTION  //
                Cell toMove = ent.computeAction(); // tell the entity where he wants to move

                // 2- MANAGE COLLISIONS //
                boolean canPerform = ent.manageCollision(matrix.getCell(toMove), toMove );

                // 3- PERFORM ACTION //
                if (canPerform) ent.performAction(toMove); // if the entity moves, this will update its coordinates

                // APPLY NEW COORDS IN THE GAME MATRIX //
                Cell newCoord = ent.getCoord();
                matrix.setCell(newCoord, ent.blockType());
            }
        }
    }


    //-------------------------------------- GAME STATE -----------------------------------------------//
    @Override
    void clear() {
        super.clear(); // clear matrix and entities
        // Reset all stuffs related to the game state
        _Timer.stopTimer(); // Stop the timer
        starCount = 0; // Reset star count
    }

    public void start(){
        assert currLevel > 0 && currLevel <= NUM_LEVELS : "Current level must be between 1 and " + NUM_LEVELS +
                ". Actual value: " + currLevel + ".";
        assert !entities.isEmpty() : "Entities list should not be empty when starting the game.";

        _Timer.start();
    }
    public void restart() {
        setLevel(currLevel); // Reset the level to the current one
    }
    public void togglePause() {
        _Timer.togglePause();
    }

    public boolean isRunning() {
        return _Timer.isRunning();
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

        // IF the creature is already moving, we do not change its direction
        if (! creature.isMoving()) {
            creature.setDirection(direction);
        }
    }


    //------------------------------ GAME EVENTS -----------------------------------------------//
    /* Game events such as win, lose, etc. are handled using the PropertyChangeListener interface
     * (which can be seen has java modern Observer pattern) in the GameController.
     * This allows for decoupling of the game logic from the UI, enabling a more modular design.
     * The GameController listens for property changes and updates the UI accordingly.
     *
     * For example, when the game is won, the GameController can listen for a "win" event
     * and then update the UI to show a win message or transition to the next level.
     */
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    public void addPropertyChangeListener(GameController gameController) {
        pcs.addPropertyChangeListener(gameController);
    }

    /**
     * Enum representing game events that can be observed.
     */
    public enum Event {
//        WIN,LOSE,
        PLAY
    }

    /**
     * Ends the game by firing a property change event {@link Event#PLAY}, passing {@code isWin} as new value.
     * @param isWin whether the game was won or not. If {@code null}, the event will be fired regardless of the win status (just EXIT).
     */
    public void end(Boolean isWin){
        assert pcs.getPropertyChangeListeners().length != 0 : "At least one listener is required";

        togglePause();

        if (isWin!= null && isWin) {
            // CHECK ADD STAR //
            if (getElapsedTime() < 60) {
                addstar(); // if the elapsed time is less than 60 seconds, add a star
            }
        }

        pcs.firePropertyChange(new PropertyChangeEvent(this,
                GameController.PropertyName.EXIT.toString(),
                Event.PLAY, isWin));
    }

    //------------------------------ GETTERS AND SETTERS -----------------------------------------------//

    // LEVELS //
    private int currLevel = 1;
    public int getCurrLevel() {
        return currLevel;
    }
    public void setLevel(int index){
        currLevel=index;
        String mapPath = null;
        switch (index) {
            case 1 -> mapPath = MapParser.MAP_1;
            case 2 -> mapPath = MapParser.MAP_2;
            case 3 -> mapPath = MapParser.MAP_3;
            case 4 -> mapPath = MapParser.MAP_4;
            case 5 -> mapPath = MapParser.MAP_5;
            case 6 -> mapPath = MapParser.MAP_6;
            default ->{
                System.err.println("Level not supported: " + index + ". Loading MAP_1 as fallback.");
                MapParser.loadMap(MapParser.MAP_1, this);
            }
        }
        assert mapPath != null : "Map path should not be null for level " + index;
        MapParser.loadMap(mapPath, this);
    }

    // STARS //
    private int starCount = 0;

    /**
     * This method updates the star count
     */
    public void addstar(){
        starCount++;
        if (starCount > 3) {
            throw new IllegalStateException("Star count cannot exceed 3. Current count: " + starCount);
        }

    }
    public int getStarCount(){
        return starCount;
    }
    // ELAPSED TIME //
    public long getElapsedTime() {
        return _Timer.elapsedTime;
    }



}