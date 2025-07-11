package controller.game;

import config.ModelConfig;
import model.game.Game;
import model.game.GameConstants;
import model.game.utils.Cell;
import persistance.profile.ProfileManager;
import view.View;
import view.impl.game.GameMenu;
import view.impl.game.GamePanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static model.game.GameConstants.Direction.*;


// MOVEMENT:
//

/**
 * Controller for the game, handling user input and game state changes.
 * <p>Game state changes (like win,lose,ecc...) are handled thanks to {@link PropertyChangeListener} interface.
 */
public class GameController extends KeyAdapter implements PropertyChangeListener {
    private final GamePanel gamePanel;
    public GameController(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        // Add this as observer to the Game model. This allows the controller to listen for Game Events.
        Game.getInstance().addPropertyChangeListener(this);
    }

    private boolean isStarted = false;
    // Use keyPressed instead of keyTyped because we want to capture key events -> https://docs.oracle.com/javase/tutorial/uiswing/events/keylistener.html
    @Override
    public void keyPressed(KeyEvent e) {
        GameLoop gl = GameLoop.getInstance();
        Game game = Game.getInstance();

        // START GameLoop and Timer at the first key press
        if (!isStarted && e.getKeyCode()!= KeyEvent.VK_ESCAPE){ // ESCAPE is excluded because it is used to pause the game.
            gl.start();
            game.start();
            isStarted = true;
        }
        if (gl.isRunning() && game.isRunning()) {
            //--MOVEMENT //
            GameConstants.Direction direction = null;
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP -> direction = UP;
                case KeyEvent.VK_DOWN -> direction = DOWN;
                case KeyEvent.VK_LEFT -> direction = LEFT;
                case KeyEvent.VK_RIGHT -> direction = RIGHT;
            }

            if (direction != null) {
                // If a direction was set, we update the game state
                game.setCreatureDirection(direction);
            }
        }
    }


    // PROPERTY CHANGE LISTENER (modern Observer) //

    public enum PropertyName{
        EXIT,RESTART
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        // Handle property changes from the Game model
        SwingUtilities.invokeLater(() -> {
            String name = e.getPropertyName();
            if (name.equals(PropertyName.EXIT.toString())) {
                onExitProperty((Boolean) e.getNewValue());
            } else if (name.equals(PropertyName.RESTART.toString())) {
                onRestartProperty();
            } else throw new IllegalStateException("Unexpected value: " + name);
        });
    }

    // EVENTS HANDLING //
    /*
        method(ActionEvent e) is an explicit way to indicate that a method is intended to be
        added as an actionListener to a component.
     */

    public void onPause(ActionEvent e){
        if (e==null){
            throw new IllegalArgumentException("ActionEvent cannot be null. This method is intended to be called from a component's actionListener.");
        }

        // GameLoop is paused when the game is paused. See GameLoop.run()
        Game.getInstance().pause();
        gamePanel.openMenu();
    }


    /**
     * Cause the game to end and show the appropriate result.
     * @param isWin whether the game was won or not. If {@code null}, just EXIT.
     * @apiNote This method is called from {@link #propertyChange(PropertyChangeEvent) propertyChange}
     */
    private void onExitProperty(Boolean isWin) {
        // GAMELOOP //
        GameLoop gl = GameLoop.getInstance();
        if (gl.isRunning())
            gl.shutdown();

        // VIEW //
        if (isWin != null) {
            // WIN
            if (isWin) {
                ProfileManager.getLastProfile().sumCoins(Game.getInstance().getStarCount() * ModelConfig.COINS_PER_STAR); // 10 coins per star
                gamePanel.endLevel(true);
            // LOSE
            } else {
                gamePanel.endLevel(false);
            }
        }
        else { // JUST EXIT when isWin==null //
            View.getInstance().showHome();
        }
    }


    private void onRestartProperty() {
        GameLoop gl = GameLoop.getInstance();
        if (gl.isRunning()) { // when restart is pressed from EndLevelDialog, gl is no longer running due onExitProperty
            gl.shutdown();
        }

        isStarted = false;
        gamePanel.setElapsedTime(0L);
        gamePanel.repaintBackground();
        gamePanel.repaint();
    }

}
