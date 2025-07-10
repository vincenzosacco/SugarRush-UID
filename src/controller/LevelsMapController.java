package controller;

import config.ModelConfig;
import utils.Resources;
import view.impl._common.buttons.LevelButton;
import view.impl.home.levelsMap.LevelInfoDialog;
import view.impl.home.levelsMap.LevelsMap;

import java.io.InputStream;

public class LevelsMapController {
    // Reference to the start menu panel
    private final LevelsMap levelsMap;

    // Constructor initializes the controller with the start menu panel and sets up event listeners
    public LevelsMapController(LevelsMap levelsMap) {
        this.levelsMap = levelsMap;
    }
    
    public void bindListenerToButton(LevelButton button, int levelIndex) {
        // Add a listener to open the level window when the button is clicked
        button.addActionListener(e -> {
            // TODO instead of recreating the dialog every time, create once and reuse it!!!
            InputStream levelFile = Resources.getResourceAsStream("/maps/map" + levelIndex + ".txt");
            LevelInfoDialog levelInfoDialog = new LevelInfoDialog(levelFile, levelIndex);
            levelsMap.showLevelDialog(levelInfoDialog);
        });
    }
}

