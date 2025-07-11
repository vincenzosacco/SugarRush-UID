package controller.game;

import model.game.Game;
import view.View;
import view.impl.game.GameMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameMenuController  {

    public void onResume (ActionEvent e){
        assert e != null : "ActionEvent cannot be null. This method is intended to be called from a component's actionListener.";
        View.getInstance().getGamePanel().closeMenu();
        Game.getInstance().resume();
    }

    public void onRestart (ActionEvent e){
        assert e != null : "ActionEvent cannot be null. This method is intended to be called from a component's actionListener.";
        // trigger RESTART event in Game Controller
        Game.getInstance().restart();
        onResume(e);
    }



    public void onExit(ActionEvent e) {
        // trigger EXIT event in Game Controller
        Game.getInstance().end(null);
    }


    public void onSettings(ActionEvent e){
        assert e != null : "ActionEvent cannot be null. This method is intended to be called from a component's actionListener.";
        View.getInstance().getHome().showSettings();
    }

}