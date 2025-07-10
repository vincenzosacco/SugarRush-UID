package persistance.profile;

import config.ModelConfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Profile implements Serializable {
    private static final long serialVersionUID = 1L;
    String name = "DefaultPlayer";
    /** * The number of coins collected by the player. Min 0 and Max {@link ModelConfig#MAX_COINS} */
    private int coins = 0; // Default to half of the maximum coins

    //a list of booleans
    private final ArrayList<Boolean> characters; // Represents available characters, true if available, false if locked
    private int currentCharacterIndex;


    /**
     * <p>Map of start collected for each level.</p>
     *
     * KEY -> level number (int)<p>
     * VALUE -> List of 3 booleans representing the stars collected in that level.
     *
     *<p>Default values are [False,False,False]</p>
     */
    private final HashMap<Integer, Boolean[]> levelStarsCount;

    // Keep it package-private. Only the ProfileManager should be able to create a profile.
    Profile(){
        characters = new ArrayList<>(List.of(true, false, false, false, false, false)); // Default: only first character is available
        currentCharacterIndex = 0; // Default to the first character

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

    /**
     * Adds or subtracts coins from the current total. Coins cannot go below 0 or above {@link ModelConfig#MAX_COINS}.
     @param c the number of coins to add (can be negative to subtract)
     */
    public void sumCoins(int c){
        int sum = coins + c;
        assert sum >= 0;

        // Cap the coins to ModelConfig.MAX_COINS
        if (sum <= ModelConfig.MAX_COINS){
            coins = sum;
            ProfileManager.saveProfile(this); // Save the profile after updating coins
        }

    }

    public List<Boolean> getCharacters() {
        return characters;
    }
    public int getCurrentCharacterIndex() {
        return currentCharacterIndex;
    }
    public void setCurrentCharacterIndex(int index) {
        if (index < 0 || index >= characters.size()) {
            throw new IllegalArgumentException("Invalid character index: " + index);
        }
        currentCharacterIndex = index;
        ProfileManager.saveProfile(this); // Save the profile after updating coins
    }
    public void setCharacters(int index) {
        if (characters == null || characters.size() != this.characters.size()) {
            throw new IllegalArgumentException("Characters array must have length " + this.characters.size());
        }
        characters.set(index, true); // Unlock the character at the given index
        ProfileManager.saveProfile(this); // Save the profile after updating coins
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
        ProfileManager.saveProfile(this); // Save the profile after updating coins
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
