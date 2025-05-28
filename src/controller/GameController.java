package controller;

import model.Model;
import model.game.Game;
import view.game.GamePanel;
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
    private boolean started = false;


    // Use keyPressed instead of keyTyped because we want to capture key events -> https://docs.oracle.com/javase/tutorial/uiswing/events/keylistener.html
    @Override
    public void keyPressed(KeyEvent e) {
        Game gameModel = Model.getInstance().getGame();
        GamePanel view = View.getInstance().getGamePanel();

        if (!started) {
            // START GAME
            started = true;
            return;
        }

        // NOTIFY MODEL
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> {
                gameModel.performMove(UP);
            }
            case KeyEvent.VK_DOWN -> {
                gameModel.performMove(DOWN);
            }
            case KeyEvent.VK_LEFT -> {
                gameModel.performMove(LEFT);
            }
            case KeyEvent.VK_RIGHT -> {
                gameModel.performMove(RIGHT);
            }
            case KeyEvent.VK_ESCAPE -> {
                // PAUSE GAME when the game menu is opened
                if (gameModel.isRunning()) gameModel.stop();
                // RESTORE GAME when the game menu is closed
                else gameModel.start();

                view.toggleSettingsPanel();
            }
        }
    }
}
