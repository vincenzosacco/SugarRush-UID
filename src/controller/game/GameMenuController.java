package controller.game;

import model.game.Game;
import view.View;
import view.impl.game.GameMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameMenuController  {
    /* Using a static inner class instead of method references to avoid issues with wrong calls
    to the onResume method. In this way, nobody can call "onResume()" directly, you can just create a
    new onResume object and pass it as ActionListener to the button.
    !This is explicit, declarative and safer.!
    (By the way, this "class approach" isn't different from "method approach" in terms of semantic and performance .
     */
    public static class onResume implements ActionListener{
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
//            View.getInstance().getGamePanel().getController().onPause();
        }
    }

    public static class onRestart implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            GameLoop.getInstance().shutdown();
            Game.getInstance().restart();

            View.getInstance().getGamePanel().toggleMenu();
            View.getInstance().getGamePanel().repaintBackground();
            View.getInstance().showGame();
        }
    }

    public static class onExit implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
//            View.getInstance().getGamePanel().getController().onExit(null);
            View.getInstance().getGamePanel().toggleMenu();
            View.getInstance().showHome();
        }
    }

    public static class onPause implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
//            View.getInstance().getGamePanel().getController().onPause();
        }
    }


}