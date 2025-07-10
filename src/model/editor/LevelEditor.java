package model.editor;

import model.game.GameBoard;

/**
 * Model for the Level Editor Panel.
 */
public class LevelEditor extends GameBoard {
    // SINGLETON //
    private static LevelEditor instance = null;

    private LevelEditor() {
        super();
    }

    public static LevelEditor getInstance() {
        if (instance == null) {
            instance = new LevelEditor();
        }
        return instance;
    }




}