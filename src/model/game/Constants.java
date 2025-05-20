package model.game;

/**
 * The {@code Constants} interface contains fundamental constants and enumerations
 * that define key aspects of the game's domain, such as directions for movement
 * and block types in the game's map.
 */
public interface Constants {
    /**
     * <p>
     * The {@code Direction} enum defines the possible movement directions within the game.
     * It is used to represent directional movement, typically for navigating the game map
     * or controlling entities such as a creature.
     * </p>
     * <p>
     * The enum consists of four directions:
     * - {@code UP}: Represents upward movement.
     * - {@code DOWN}: Represents downward movement.
     * - {@code LEFT}: Represents leftward movement.
     * - {@code RIGHT}: Represents rightward movement.|
     * </p>
     */
    enum Direction{UP, DOWN, LEFT, RIGHT}

    /**
     * Enum representing different types of blocks that can exist within the game map.
     * Each block type corresponds to a specific element within the game's environment:
     * - {@code SPACE}: An empty space where the creature can move freely.
     * - {@code WALL}: An impassable block representing a boundary or obstacle within the map.
     * - {@code CREATURE}: The current position of the creature in the game.
     * - {@code SUGAR}: A collectible item in the game that can be consumed by the creature.
     */
    enum Block{SPACE, WALL, CREATURE, SUGAR, ENEMY1}
}
