package Model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static config.Model.COL_COUNT;
import static config.Model.ROW_COUNT;

/**
 * This class act as parse
 */
public class MapParser {
    private final Game game;

    final static String MAP_1 = "/map1.txt";
    final static String MAP_2 = "/map2.txt";


    MapParser(Game game) {
        this.game = game;
    }

    /**
     * Read a .txt file a convert each line to a String element of the returned Array.
     * @return a String[] containing lines
     */
    private String[] readMapResource(String mapResourcePath) {
        try {
            // Get the resource as a stream
            InputStream inputStream = getClass().getResourceAsStream(mapResourcePath);
            if (inputStream == null) {
                throw new IllegalArgumentException("Resource not found: " + mapResourcePath);
            }

            // Read all lines from the file
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                return reader.lines().toArray(String[]::new);
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
    public void loadMap(String map){
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
                    case 'x'-> mat.get(row).add(Game.Block.WALL);
                    case 'c' -> {
                        mat.get(row).add(Game.Block.CREATURE);
                        game.creatureCell.setCoord(row,col);
                    }
                    case 's'-> mat.get(row).add(Game.Block.SUGAR);
                    case ' '-> mat.get(row).add(Game.Block.SPACE);
                    default -> throw new IllegalArgumentException("Invalid char in map file '" + map + "': "
                            + tileMapChar+ " at row: " + row + " col: " + col);
                }
            }
        }
    }

}
