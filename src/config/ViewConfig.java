package config;

import java.awt.*;

import static config.ModelConfig.COL_COUNT;
import static config.ModelConfig.ROW_COUNT;

/**
 * All common constants in the application
 */
public interface ViewConfig {
    int TILE_SIZE =32;
    int BOARD_WIDTH = COL_COUNT * TILE_SIZE;
    int BOARD_HEIGHT = ROW_COUNT * TILE_SIZE;

    // COLORS //
    Color GAME_BG = new Color(0, 188, 250);



}
