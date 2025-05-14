package Controller;

import Controller._abstract.AbsKeyController;
import Model.game.Game;
import View.GamePanel;

import java.awt.event.KeyEvent;

import static Model.game.Constants.Direction.*;


// MOVEMENT:
//

/**
 * Controller of 'SugarPanel'
 */
public class GameController extends AbsKeyController {

    public GameController(Game model, GamePanel gamePanel){
        super(model, gamePanel);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Game gameModel = (Game) this.model;
        GamePanel view = (GamePanel) this.view;
        // NOTIFY MODEL
        try {

            switch (e.getKeyCode()){
                case KeyEvent.VK_UP -> gameModel.moveCreature(UP);
                case KeyEvent.VK_DOWN -> gameModel.moveCreature(DOWN);
                case KeyEvent.VK_LEFT -> gameModel.moveCreature(LEFT);
                case KeyEvent.VK_RIGHT -> gameModel.moveCreature(RIGHT);
                case KeyEvent.VK_ESCAPE ->{
                    gameModel.openSetting();
                    view.toggleSettingsPanel();
                }
            }
        }
        catch (InterruptedException ie){
            ie.printStackTrace();
        }

        // NOTIFY VIEW
//        view.repaint();
    }


    // DOESN'T WORK with arrows -> https://docs.oracle.com/javase/tutorial/uiswing/events/keylistener.html
//    public void keyTyped(KeyEvent e) {
//        System.out.println("Key typed: " + e.getKeyCode());
//
//        // UP
//        switch (e.getKeyCode()){
//            case KeyEvent.VK_UP -> this.model.moveCreature(GameMap.UP);
//            case KeyEvent.VK_DOWN -> this.model.moveCreature(GameMap.DOWN);
//            case KeyEvent.VK_LEFT -> this.model.moveCreature(GameMap.LEFT);
//            case KeyEvent.VK_RIGHT -> this.model.moveCreature(GameMap.RIGHT);
//        }
//        view.repaint();
//    }
}
