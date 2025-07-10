package controller.game;

import model.game.Game;
import view.View;
import view.impl.game.GameMenuPanel;

public class GameMenuController  {
    private final GameMenuPanel view;

    public GameMenuController(GameMenuPanel view) {
        this.view = view;
    }

    /**
     * Define what happens when continue buttons is pressed
     */
    public void onResume(){
        View.getInstance().getGamePanel().getController().onPause();
    }

    public void onRestart(){
        GameLoop.getInstance().shutdown();
        Game.getInstance().restart();

        View.getInstance().getGamePanel().toggleMenu();
        View.getInstance().getGamePanel().repaintBackground();
        View.getInstance().showGame();
        // Request focus to the game panel to capture user input
        View.getInstance().getGamePanel().requestFocusInWindow();
    }

    public void onExit(){
        View.getInstance().getGamePanel().getController().onExit(null);
        View.getInstance().getGamePanel().toggleMenu();
        View.getInstance().showHome();
    }

    public void onSettings(){
//        View.getInstance().getHome().showPanel(HomeContainer.PanelName.SETTINGS);
    }

}