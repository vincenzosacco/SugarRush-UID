package model.game;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LevelData {
    // Stores the request or challenge text associated with each coin/star
    private final String[] textRequest = new String[]{"Error: Text not loaded.", "Error: Text not loaded.", "Error: Text not loaded."};;


    // Constructor: loads coin and request data from the provided level file
    public LevelData(InputStream levelFileStream) {
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
        for (String line : lines) {
            // "textRequest="
            if (line.startsWith("textRequest=")) {
                String[] parts = line.substring("textRequest=".length()).split(",");
                for (int i = 0; i < parts.length && i < this.textRequest.length; i++) {
                    this.textRequest[i] = parts[i].trim();
                }
            }
        }
    }

    // Returns the array of request texts for each coin/star
    public String[] getTextRequest() {
        return textRequest;
    }
}

