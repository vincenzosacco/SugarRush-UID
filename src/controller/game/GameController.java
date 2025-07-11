package controller.game;

import model.game.Game;
import model.game.GameConstants;
import persistance.profile.ProfileManager;
import view.View;
import view.impl.game.GamePanel;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    public GameController() {
        // Add this as observer to the Game model. This allows the controller to listen for Game Events.
        Game.getInstance().addPropertyChangeListener(this);
    }

    // Use keyPressed instead of keyTyped because we want to capture key events -> https://docs.oracle.com/javase/tutorial/uiswing/events/keylistener.html
    @Override
    public void keyPressed(KeyEvent e) {
        GameLoop gl = GameLoop.getInstance();
        Game game = Game.getInstance();

        // START GameLoop and Timer at the first key press
        if (!gl.isRunning()){
            gl.start();
            game.start();
        }
        else {
            assert gl.isRunning() : "GameLoop should be running when the game is started. If not, there is a bug somewhere";
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
        EXIT
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        // Handle property changes from the Game model
        SwingUtilities.invokeLater(() -> {
            String propertyName = e.getPropertyName();
            if (propertyName.equals(PropertyName.EXIT.toString())) {
                Game.Event cause = (Game.Event) e.getNewValue();
                assert (cause == Game.Event.WIN || cause == Game.Event.LOSE);
                onExit(cause == Game.Event.WIN);
            }
            else throw new IllegalStateException("Unexpected value: " + propertyName);
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
        Game.getInstance().togglePause();
        View.getInstance().getGamePanel().toggleMenu();
    }


    public void onExit(Boolean isWin) {
        // GAMELOOP //
        GameLoop gl = GameLoop.getInstance();

        if (gl.isRunning())
            gl.shutdown();


        // VIEW //
        GamePanel panel = View.getInstance().getGamePanel();
        if (isWin != null) {   // JUST EXIT when isWin==null //
            // WIN
            if (isWin) {
                ProfileManager.getLastProfile().sumCoins(Game.getInstance().getStarCount() * 10); // 10 coins per star
                panel.winLevel();
            // LOSE
            } else {
                panel.loseLevel();
            }
        }


        // At the end, i can reset the game
//        game.clear(); // Clear the game model
    }

}
