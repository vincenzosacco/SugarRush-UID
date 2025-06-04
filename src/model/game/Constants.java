package model.game;

/**
 * The {@code Constants} interface contains fundamental constants and enumerations
 * that define key aspects of the game's domain, such as directions for movement
 * and block types in the game's game.
 */
public interface Constants {

    /**
     * <p>
     * The {@code Direction} enum defines the possible movement directions within the game.
     * It is used to represent directional movement, typically for navigating the game game
     * or controlling blocks such as a creature.
     */
    enum Direction{
        /**Represents upward movement.*/
        UP,
        /**Represents downward movement.*/
        DOWN,
        /**Represents leftward movement.*/
        LEFT,
        /**Represents rightward movement.*/
        RIGHT,
        /**Represents no movement, typically used when the creature is idle or not moving.*/
        NONE;

        /**
         * @return opposite direction.
         */
        public Direction opposite() { // just a utility
            return switch (this) {
                case UP -> DOWN;
                case DOWN -> UP;
                case LEFT -> RIGHT;
                case RIGHT -> LEFT;
                case NONE -> NONE; // no opposite for NONE
            };
        }
    }

    /**
     * Enum representing different types of blocks that can exist within the game game.
     * Each block type corresponds to a specific element within the game's environment:
     */
    enum Block{
        /** An empty space where the creature can move freely.*/
        SPACE,
        /**An impassable block representing a boundary or obstacle within the game.*/
        WALL,
        /**The current position of the creature in the game.*/
        CREATURE,
        /**A collectible item in the game that can be consumed by the creature.*/
        SUGAR,
        ENEMY1,
        /** A block that if you touch it you die.*/
        THORNS
    }
}
