package config;

public interface ModelConfig {
    int ROW_COUNT =21;
    int COL_COUNT =19;

    /** Number of levelsMap available in the game. Starts from 1 to NUM_LEVELS.*/
    int NUM_LEVELS = 6;

    int MAX_COINS = 99999999; // Maximum coins a player can have

    /** Multiplier for the number of coins collected per star.
     * This is used to calculate the total coins earned at the end of a level.
     * */
    int COINS_PER_STAR = 10;
}
