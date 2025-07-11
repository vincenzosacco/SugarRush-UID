package controller.game;

import model.game.Game;
import view.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameMenuController  {

    public void onResume (ActionEvent e){
        assert e != null : "ActionEvent cannot be null. This method is intended to be called from a component's actionListener.";
        View.getInstance().getGamePanel().clickPause(); // unique behavior of PAUSE
    }

    public void onRestart (ActionEvent e){
        assert e != null : "ActionEvent cannot be null. This method is intended to be called from a component's actionListener.";
        GameLoop.getInstance().shutdown();
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