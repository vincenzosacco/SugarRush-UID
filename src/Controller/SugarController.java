package Controller;

import Model.Game;
import View.SugarPanel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

// MOVEMENT:
//

/**
 * Controller of 'SugarPanel'
 */
public class SugarController extends KeyAdapter implements IController{
    private final SugarPanel view;
    private final Game model;

    public SugarController(SugarPanel sugarPanel, Game model){
        this.view = sugarPanel;
//        this.view.setController(this);
        this.model = model;
    }

    public Game getModel(){
        return this.model;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_UP -> this.model.moveCreature(Game.UP);
            case KeyEvent.VK_DOWN -> this.model.moveCreature(Game.DOWN);
            case KeyEvent.VK_LEFT -> this.model.moveCreature(Game.LEFT);
            case KeyEvent.VK_RIGHT -> this.model.moveCreature(Game.RIGHT);
        }
        view.repaint();
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
