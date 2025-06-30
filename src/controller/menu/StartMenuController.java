package controller.menu;

import config.ModelConfig;
import controller.ControllerObj;
import utils.Resources;
import view.button.LevelButton;
import view.menu.LevelPanel;
import view.menu.StartMenuPanel;
import java.io.InputStream;

import java.io.File;

public class StartMenuController implements ControllerObj {
    // Reference to the start menu panel
    private final StartMenuPanel startMenuPanel;

    // Constructor initializes the controller with the start menu panel and sets up event listeners
    public StartMenuController(StartMenuPanel startMenuPanel) {
        this.startMenuPanel = startMenuPanel;
        initListeners(); // Attach button listeners
    }

    // Adds ActionListeners to all level selection buttons
    private void initListeners() {
        for (int i = 0; i < ModelConfig.NUM_LEVELS; i++) {
            final int levelIndex = i + 1; // Level numbers start from 1
            LevelButton button = startMenuPanel.getLevelButton(i);

            // Add a listener to open the level window when the button is clicked
            button.addActionListener(e -> {
                InputStream levelFile = getClass().getResourceAsStream("/maps/map" + levelIndex + ".txt");
                LevelPanel levelPanel = new LevelPanel(levelFile, levelIndex);
                startMenuPanel.showLevelDialog(levelPanel);
            });
        }
    }

}

