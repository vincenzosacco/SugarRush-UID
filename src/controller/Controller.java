package controller;


import controller.menu.StartMenuController;
import view.View;

/**
 *  Interface to the Model.
 */
public class Controller {
   public static void bind(){
       View view = View.getInstance();
       view.getGamePanel().bindController(new GameController());
       view.getStartMenuPanel().bindController(new StartMenuController());
   }

}
