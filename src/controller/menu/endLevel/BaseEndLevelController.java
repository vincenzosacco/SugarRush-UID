package controller.menu.endLevel;

import controller.ControllerObj;
import controller.GameLoop;
import model.Model;
import view.View;
import view.menu.endLevel.BaseEndLevelPanel;

public class BaseEndLevelController implements ControllerObj {
    private final BaseEndLevelPanel panel;

    public BaseEndLevelController(BaseEndLevelPanel panel) {
        this.panel = panel;
    }
    // Restart
    public void onRestart(){
        int levelToRestart = Model.getInstance().getGame().getCurrLevel();
        panel.setVisible(false);
        // Start the level showing the GamePanel
        View.getInstance().getGamePanel().endGame();
        Model.getInstance().getGame().clearGameMatrix();

        View.getInstance().getGamePanel().repaintBackground();
        Model.getInstance().getGame().setLevel(levelToRestart);
        View.getInstance().showPanel(View.PanelName.GAME.getName());
        View.getInstance().getGamePanel().getPauseButton().setEnabled(true);
        GameLoop.getInstance().start();
    }
    // Exit
    public void onExit(){
        panel.setVisible(false);
        GameLoop.getInstance().shutdown();
        Model.getInstance().getGame().clearGameMatrix();
        View.getInstance().showPanel(View.PanelName.CUSTOM_TABBED_PANE.getName());
        //Update the StartMenuPanel button graphics
        View.getInstance().getCustomTabbedPane().getStartMenuPanel().refreshLevelButtons();
    }
}
