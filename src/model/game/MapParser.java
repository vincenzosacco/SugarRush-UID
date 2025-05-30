package model.game;

import model.game.Constants.Block;
import model.game.entities.Creature;
import model.game.entities.evil.Enemy1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static config.Model.COL_COUNT;
import static config.Model.ROW_COUNT;


/**
 * <p>
 * The {@code MapParser} class provides utility methods to parse map files and populate the game board with blocks.
 * This class is designed to read map configurations from resource files, parse them into a structured format,
 * and update the game map with appropriate elements, such as walls, spaces, the creature, and sugar pieces.
 * </p>
 * <p>
 * It supports reading textual representations of maps stored in `.txt` files and translating each character
 * into the corresponding {@code Block} element, which can then be used in the game.
 * </p>
 * @apiNote This class is intended for internal use within the game's infrastructure to load and initialize map data.
 */
class MapParser {
    final static String MAP_1 = "/map1.txt";
    final static String MAP_2 = "/map2.txt";
    final static String MAP_3 = "/map3.txt";
    final static String MAP_4 = "/map4.txt";
    final static String MAP_5 = "/map5.txt";
    final static String MAP_6 = "/map6.txt";
    /**
     * Read a .txt file a convert each line to a String element of the returned Array.
     * @return a String[] containing lines
     */
    private static String[] readMapResource(String mapResourcePath) {
        try {
            // Get the resource as a stream
            InputStream inputStream = MapParser.class.getResourceAsStream(mapResourcePath);
            if (inputStream == null) {
                throw new IllegalArgumentException("Resource not found: " + mapResourcePath);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                List<String> mapLines = new ArrayList<>();
                boolean readingMap = false;

                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("map=")) {
                        readingMap = true;
                        // The actual content of the map starts on the next line.
                        continue; // Move to next line immediately
                    } else if (readingMap) {
                        if (line.startsWith("coins=") || line.startsWith("textRequest=")) {
                            break; // fine della sezione mappa
                        }
                        // Here you add the lines that are *actually* part of the map.
                        // If a blank line within the map section is to represent a blank line in the grid,
                        // then `mapLines.add(line)` is correct for those.
                        mapLines.add(line);
                    }
                }

                return mapLines.toArray(new String[0]);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading map file: " + e.getMessage(), e);
        }
    }

    /**
     * Parses a given map file and updates the game board elements in the SugarPanel object based on the map configuration.
     * The method translates the textual representation of the map into game board blocks such as walls, spaces,
     * the creature, and the sugar piece. The map file is read, and each character is converted into a block or an object
     * placed in the corresponding position on the board.
     *
     * @param map the path to the map resource file to be loaded
     * @throws IllegalArgumentException if the map file contains invalid characters or is malformed
     * @apiNote
     */
    static void loadMap(String map, Game game ){
        GameMatrix mat = game.gameMat;

        mat.clear();

        String[] tileMap = readMapResource(map);

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
                        game.entities.add(new Creature(row, col));
                    }
                    case 's'-> mat.get(row).add(Block.SUGAR);
                    case 'e' -> {
                        mat.get(row).add(Block.ENEMY1);
                        game.entities.add(new Enemy1(row,col));
                    }
                    case 't' -> mat.get(row).add(Block.THORNS);
                    case ' '-> mat.get(row).add(Block.SPACE);
                    default -> throw new IllegalArgumentException("Invalid char in map file '" + map + "': "
                            + tileMapChar+ " at row: " + row + " col: " + col);
                }
            }
        }
    }

}
