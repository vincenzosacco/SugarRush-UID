package model.game;

import model.game.GameConstants.Block;
import model.game.entities.Creature;
import model.game.entities.evil.Enemy1;
import utils.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static config.ModelConfig.COL_COUNT;
import static config.ModelConfig.ROW_COUNT;


/**
 * <p>
 * The {@code MapParser} class provides utility methods to parse game files and populate the game board with blocks.
 * This class is designed to read game configurations from resource files, parse them into a structured format,
 * and update the game game with appropriate elements, such as walls, spaces, the creature, and sugar pieces.
 * </p>
 * <p>
 * It supports reading textual representations of maps stored in `.txt` files and translating each character
 * into the corresponding {@code Block} element, which can then be used in the game.
 * </p>
 * @apiNote This class is intended for internal use within the game's infrastructure to load and initialize game data.
 */
public class MapParser {
    public final static String MAP_1 = "/maps/map1.txt";
    public final static String MAP_2 = "/maps/map2.txt";
    public final static String MAP_3 = "/maps/map3.txt";
    public final static String MAP_4 = "/maps/map4.txt";
    public final static String MAP_5 = "/maps/map5.txt";
    public final static String MAP_6 = "/maps/map6.txt";
    /**
     * Read a .txt file a convert each line to a String element of the returned Array.
     * @return a String[] containing lines
     */
    private static String[] loadMapResource(String mapResourcePath) {
        List<String> mapLines = new ArrayList<>();

        try (InputStream inputStream = Resources.getResourceAsStream(mapResourcePath)) { // this closes automatically
            try {
                // Use BufferedReader to read the input stream line by line
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    boolean readingMap = false;

                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("map=")) {
                            readingMap = true;
                            // The actual content of the game starts on the next line.
                            continue; // Move to next line immediately
                        } else if (readingMap) {
                            if (line.startsWith("coins=") || line.startsWith("textRequest=")) {
                                break; // fine della sezione mappa
                            }
                            // Here you add the lines that are *actually* part of the game.
                            // If a blank line within the game section is to represent a blank line in the grid,
                            // then `mapLines.add(line)` is correct for those.
                            mapLines.add(line);
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("Error reading game file: " + e.getMessage(), e);
            }
        } catch (IOException e) {
            System.err.println("Error closing InputStream: " + e.getMessage());
        }

        return mapLines.toArray(new String[0]);

    }

    /**
     * Parses a given map file and updates the game board elements in the {@link Game} based on the map configuration.
     * The method translates the textual representation of the map into game board blocks such as walls, spaces,
     * the creature, ect.
     * @param map the path to the map resource file to be loaded
     * @throws IllegalArgumentException if the map file contains invalid characters or is malformed
     * @apiNote
     */
    static void loadMap(String map, GameBoard gameBoard ){
        gameBoard.clear(); // clear matrix and entities
        _GameMatrix mat = gameBoard.matrix;

        // assert matrix is cleared
        assert mat.isEmpty() : "Game matrix should be empty before loading a new levelsMap.";

        String[] tileMap = loadMapResource(map);
        assert tileMap.length > 0 : "Game file '" + map + "' is empty or not found.";


        for (int row = 0; row < ROW_COUNT; row++) {
            // add row to game matrix
            mat.add(new ArrayList<>());

            for (int col = 0; col < COL_COUNT; col++) {
                char tileMapChar = tileMap[row].charAt(col);

                // add the corresponding block to mat[r]
                switch (tileMapChar) {
                    case 'x'-> mat.get(row).add(Block.WALL);
                    case 'c' ->{
                        mat.get(row).add(Block.CREATURE);
                        gameBoard.entities.add(new Creature(row, col));
                    }
                    case 's'-> mat.get(row).add(Block.SUGAR);
                    case 'S' -> mat.get(row).add(Block.CANDY);
                    case 'e' -> {
                        mat.get(row).add(Block.ENEMY1);
                        gameBoard.entities.add(new Enemy1(row,col));
                    }
                    case 't' -> mat.get(row).add(Block.THORNS);
                    case ' '-> mat.get(row).add(Block.SPACE);
                    default -> throw new IllegalArgumentException("Invalid char in game file '" + map + "': "
                            + tileMapChar+ " at row: " + row + " col: " + col);
                }
            }
        }
    }

}