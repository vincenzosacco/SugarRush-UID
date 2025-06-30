package model.profile;
import config.ModelConfig;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Profile implements Serializable {
    private static final long serialVersionUID = 1L;
    String name;
    private int coins;

    /**
     * <p>Map of start collected for each level.</p>
     *
     * KEY -> level number (int)<p>
     * VALUE -> List of 3 booleans representing the stars collected in that level.
     *
     *<p>Default values are [False,False,False]</p>
     */
    private final HashMap<Integer, Boolean[]> levelStarsCount;


    public Profile(){
        this.name = "Player";
        this.coins = 0;

        levelStarsCount = new HashMap<>();
        for (int i = 1; i <= ModelConfig.NUM_LEVELS; i++) {
            levelStarsCount.put(i, new Boolean[]{false, false, false}); // Initialize with no stars collected
        }
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getCoins() {
        return coins;
    }
    public void setCoins(int c) {
        coins = c;
    }


    public void setLevelStarsCount(int level, Boolean[] stars) {
        // CHECKS
        if (level< 1 || level > ModelConfig.NUM_LEVELS ){
            throw new IllegalArgumentException("Invalid level number: " + level);
        }

        if (stars == null || stars.length != 3) {
            throw new IllegalArgumentException("Stars list must contain exactly 3 boolean values.");
        }

        // //
        levelStarsCount.put(level, stars); // TODO, check if this can cause bugs
    }


    /**
     * Returns the stars collected for a specific level.
     * @param levelNum the level number (1 to ModelConfig.NUM_LEVELS)
     * @return a Boolean (clone) array of size 3, where each element represents whether a star was collected (true) or not (false).
     */
    public Boolean[] getLevelStarsCount(int levelNum) {
        // CHECKS
        if (levelNum < 1 || levelNum > ModelConfig.NUM_LEVELS) {
            throw new IllegalArgumentException("Invalid level number: " + levelNum);
        }

        return levelStarsCount.get(levelNum).clone(); // Return a clone to prevent external modification
    }
}
