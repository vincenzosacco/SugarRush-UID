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
    private final GameLoop gameLoop = GameLoop.getInstance();
    private boolean started = false;


    // Use keyPressed instead of keyTyped because we want to capture key events -> https://docs.oracle.com/javase/tutorial/uiswing/events/keylistener.html
    @Override
    public void keyPressed(KeyEvent e) {
        Game model = Model.getInstance().getGame();
        GamePanel view = View.getInstance().getGamePanel();

        if (!started) {
            // START GAME
            gameLoop.start();
            started = true;
        }

        // NOTIFY MODEL
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> model.setCreatureDirection(UP);

            case KeyEvent.VK_DOWN -> model.setCreatureDirection(DOWN);

            case KeyEvent.VK_LEFT -> model.setCreatureDirection(LEFT);

            case KeyEvent.VK_RIGHT -> model.setCreatureDirection(RIGHT);

            case KeyEvent.VK_ESCAPE -> {
                // PAUSE GAME when the game menu is opened
                if (gameLoop.isRunning()) gameLoop.stop();
                // RESTORE GAME when the game menu is closed
                else gameLoop.start();

                view.toggleSettingsPanel();
            }
        }
    }
}
