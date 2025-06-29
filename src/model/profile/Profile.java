package model.profile;
import config.ModelConfig;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Profile implements Serializable {
    private static final long serialVersionUID = 1L;
    String name;
    private static int coins;

    /**
     * <p>Map of start collected for each level.</p>
     *
     * KEY -> level number (int)<p>
     * VALUE -> List of 3 booleans representing the stars collected in that level.
     *
     *<p>Default values are [False,False,False]</p>
     */
    private final HashMap<Integer, List<Boolean>> levelStarsCount;


    public Profile(){
        this.name = "Player";
        this.coins = 0;

        levelStarsCount = new HashMap<>();
        for (int i = 1; i <= ModelConfig.NUM_LEVELS; i++) {
            levelStarsCount.put(i, List.of(false, false, false)); // Initialize with no stars collected
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


    public void setLevelStarsCount(int level, List<Boolean> stars) {
        // CHECKS
        if (level< 1 || level > ModelConfig.NUM_LEVELS ){
            throw new IllegalArgumentException("Invalid level number: " + level);
        }

        if (stars == null || stars.size() != 3) {
            throw new IllegalArgumentException("Stars list must contain exactly 3 boolean values.");
        }

        // //
        levelStarsCount.put(level, stars); // TODO, check if this can cause bugs
    }


    public Boolean[] getLevelStarsCount(int levelNum) {
        // CHECKS
        if (levelNum < 1 || levelNum > ModelConfig.NUM_LEVELS) {
            throw new IllegalArgumentException("Invalid level number: " + levelNum);
        }

        List<Boolean> stars = levelStarsCount.get(levelNum);
        return stars.toArray(new Boolean[0]); // Convert List to boolean[]
    }
}
