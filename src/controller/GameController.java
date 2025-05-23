package controller;

import model.Model;
import model.game.Game;
import view.GamePanel;
import view.View;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static model.game.Constants.Direction.*;


// MOVEMENT:
//

/**
 * Controller of 'SugarPanel'
 */
public class GameController extends KeyAdapter implements ControllerObj {
    private final GameLoop gameLoop = new GameLoop();

    // Use keyPressed instead of keyTyped because we want to capture key events -> https://docs.oracle.com/javase/tutorial/uiswing/events/keylistener.html
    @Override
    public void keyPressed(KeyEvent e) {
        Game model = Model.getInstance().getGame();
        GamePanel view = View.getInstance().getGamePanel();


        // NOTIFY MODEL
        switch (e.getKeyCode()){
            case KeyEvent.VK_UP -> {
                gameLoop.start();
                model.performMove(UP);
            }
            case KeyEvent.VK_DOWN ->{
                gameLoop.start();
                model.performMove(DOWN);
            }
            case KeyEvent.VK_LEFT ->{
                gameLoop.start();
                model.performMove(LEFT);
            }
            case KeyEvent.VK_RIGHT ->{
                    gameLoop.start();
                    model.performMove(RIGHT);
            }
            case KeyEvent.VK_ESCAPE ->{
                model.openSetting();
                view.toggleSettingsPanel();
            }
        }

    }
}
