package model.game;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LevelData {
    // Stores whether each of the 3 coins has been collected (true = collected)
    private boolean[] coinsCollected;

    // Stores the request or challenge text associated with each coin/star
    private String[] textRequest;

    // Reference to the level file
    File levelFile;

    // Constructor: loads coin and request data from the provided level file
    public LevelData(File levelFile) {
        loadCoins(levelFile);         // Load saved coin collection status
        loadTextRequest(levelFile);   // Load associated challenge texts
    }

    // Loads the challenge text for each coin/star from the file
    public void loadTextRequest(File file) {
        textRequest = new String[3]; // Assume always 3 requests (one per star/coin)

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Check for a line starting with "textRequest="
                if (line.startsWith("textRequest=")) {
                    // Split the comma-separated list of texts
                    String[] parts = line.substring("textRequest=".length()).split(",");
                    for (int i = 0; i < parts.length && i < textRequest.length; i++) {
                        textRequest[i] = parts[i].trim(); // Trim and assign each text
                    }
                    break; // Exit loop once found
                }
            }

        } catch (IOException e) {
            e.printStackTrace(); // Print any file reading errors
        }
    }

    // Loads the coin collection status (boolean values) from the level file
    public void loadCoins(File file) {
        coinsCollected = new boolean[3]; // Assume always 3 coins per level

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Check for a line starting with "coins="
                if (line.startsWith("coins=")) {
                    // Split the comma-separated list of coin flags
                    String[] parts = line.substring("coins=".length()).split(",");
                    for (int i = 0; i < parts.length && i < coinsCollected.length; i++) {
                        coinsCollected[i] = parts[i].trim().equals("1"); // "1" = collected
                    }
                    break; // Exit loop once found
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Print any file reading errors
        }
    }

    // Saves the updated coin collection status back to the level file
    public void saveCoins(File file) {
        try {
            List<String> lines = new ArrayList<>();

            // Read the entire file line by line into a list
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    // Replace the "coins=" line with updated values
                    if (line.startsWith("coins=")) {
                        line = "coins=" + (coinsCollected[0] ? "1" : "0") + "," +
                                (coinsCollected[1] ? "1" : "0") + "," +
                                (coinsCollected[2] ? "1" : "0");
                    }
                    lines.add(line);
                }
            }

            // Overwrite the file with the updated content
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                for (String l : lines) {
                    writer.println(l);
                }
            }

        } catch (IOException e) {
            e.printStackTrace(); // Print any I/O errors
        }
    }

    // Marks a specific coin as collected (by index: 0, 1, or 2)
    public void setCoinsCollected(int coinIndex) {
        if (coinIndex >= 0 && coinIndex < 3) {
            coinsCollected[coinIndex] = true;
        }
    }

    // Returns the array indicating which coins have been collected
    public boolean[] getCoinsCollected() {
        return coinsCollected;
    }

    // Returns the array of request texts for each coin/star
    public String[] getTextRequest() {
        return textRequest;
    }
}

