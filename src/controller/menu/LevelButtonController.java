package controller.menu;

import view.impl.home.levelsMap.LevelsMap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LevelButtonController implements ActionListener {
    private final LevelsMap levelsMap;
    private final int levelIndex;

    public LevelButtonController(LevelsMap levelsMap, int levelIndex) {
        this.levelsMap = levelsMap;
        this.levelIndex = levelIndex;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        levelsMap.showLevelDialog(levelIndex);
    }
}
