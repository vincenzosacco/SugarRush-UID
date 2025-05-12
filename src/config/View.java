package config;

import static config.Model.COL_COUNT;
import static config.Model.ROW_COUNT;

/**
 * All common constants in the application
 */
public interface View {
    int TILE_SIZE =32;
    int BOARD_WIDTH = COL_COUNT * TILE_SIZE;
    int BOARD_HEIGHT = ROW_COUNT * TILE_SIZE;

}
