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
////            View.getInstance().getGamePanel().getController().onExit(null);
//            View.getInstance().getGamePanel().toggleMenu();
//            View.getInstance().showHome();
        // trigger EXIT event
        Game.getInstance().end(null);
        View.getInstance().showHome();
    }


    public void onSettings(ActionEvent e){
        assert e != null : "ActionEvent cannot be null. This method is intended to be called from a component's actionListener.";
        View.getInstance().getHome().showSettings();
    }

}