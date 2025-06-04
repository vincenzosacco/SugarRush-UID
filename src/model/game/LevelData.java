package model.game;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LevelData {
    // Stores whether each of the 3 coins has been collected (true = collected)
    private boolean[] coinsCollected;

    // Stores the request or challenge text associated with each coin/star
    private String[] textRequest;


    // Constructor: loads coin and request data from the provided level file
    public LevelData(InputStream levelFileStream) {

        this.coinsCollected = new boolean[3]; // Default: tutti false
        this.textRequest = new String[]{"Error: Text not loaded.", "Error: Text not loaded.", "Error: Text not loaded."};

        if (levelFileStream == null) {
            return;
        }

        List<String> allLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(levelFileStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                allLines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Errore durante la lettura di tutte le righe del file del livello.");
            return;
        }

        processAllLevelData(allLines);

    }

    private void processAllLevelData(List<String> lines) {

        this.coinsCollected = new boolean[3];
        this.textRequest = new String[3];

        // Initialize the prompt text with default values to avoid null if not found
        for (int i = 0; i < 3; i++) {
            this.textRequest[i] = "";
        }

        boolean readingMap = false;

        for (String line : lines) {
            if (line.startsWith("map=")) {
                readingMap = true;
                continue;
            } else if (readingMap) {
                // If we encounter "coins=" or "textRequest=", we have finished reading the map section
                if (line.startsWith("coins=") || line.startsWith("textRequest=")) {
                    readingMap = false;
                }
            }
            //"coins="
            if (line.startsWith("coins=")) {
                String[] parts = line.substring("coins=".length()).split(",");
                for (int i = 0; i < parts.length && i < this.coinsCollected.length; i++) {
                    this.coinsCollected[i] = parts[i].trim().equals("1");
                }
            }
            // "textRequest="
            else if (line.startsWith("textRequest=")) {
                String[] parts = line.substring("textRequest=".length()).split(",");
                for (int i = 0; i < parts.length && i < this.textRequest.length; i++) {
                    this.textRequest[i] = parts[i].trim();
                }
            }

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

