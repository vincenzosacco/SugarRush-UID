package View;

import Model.Block;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;

/**
 * This class act as parse
 */
public class MapParser {
    private final SugarPanel sugarPanel;

    final static String MAP_1 = "/map1.txt";
    final static String MAP_2 = "/map2.txt";


    MapParser(SugarPanel sugarPanel) {
        this.sugarPanel = sugarPanel;
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
     */
    public void loadMap(String map){
        sugarPanel.walls = new HashSet<>();
        sugarPanel.spaces.clear();
        String[] tileMap = readMapResource(map);


        for (int r = 0; r < sugarPanel.rowCount; r++) {
            for (int c = 0; c < sugarPanel.columnCount; c++) {
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                int x = c * sugarPanel.tileSize;
                int y = r * sugarPanel.tileSize;

                if (tileMapChar == 'x') {
                    Block wall = new Block(sugarPanel.wallImage, x, y, sugarPanel.tileSize, sugarPanel.tileSize);
                    sugarPanel.walls.add(wall);
                }
                else if (tileMapChar == 'c') {
                    sugarPanel.creature = new Block(sugarPanel.creatureImage, x, y, sugarPanel.tileSize, sugarPanel.tileSize);
                }
                else if (tileMapChar == 's') {
                    sugarPanel.sugar = new Block(sugarPanel.sugarImage, x, y, sugarPanel.tileSize, sugarPanel.tileSize);
                }
                else if (tileMapChar == ' '){
                    Block space = new Block(null, x, y, 0, 0);
                    sugarPanel.spaces.add(space);
                }
            }
        }
    }

}
