package Config;

import static Config.Model.COL_COUNT;
import static Config.Model.ROW_COUNT;

/**
 * All common constants in the application
 */
public interface View {
    int TILE_SIZE =32;
    int BOARD_WIDTH = COL_COUNT * TILE_SIZE;
    int BOARD_HEIGHT = ROW_COUNT * TILE_SIZE;

}
