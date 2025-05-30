package controller;


import controller.menu.StartMenuController;
import view.View;
import view.menu.StartMenuPanel;

/**
 *  Interface to the Model.
 */
public class Controller {

    /**
     * Binds the controllers to the view components.
     * This method simply calls the {@link view.ViewComp#bindController(ControllerObj)} method
     * passing the appropriate controller as parameter.
     * <i><b>Example</b></i>
     * <p>For the game panel calls :</p>
     * <p>{@code View.getInstance().getGamePanel().bindController(new GameController());}</p>
     * and so on for all the view components.
      */
   public static void bind(){
       View view = View.getInstance();
       view.getGamePanel().bindController(new GameController());
       StartMenuPanel panel = view.getStartMenuPanel();
       StartMenuController controller = new StartMenuController(panel);
       panel.bindController(controller);
       //view.getLevelEditorPanel().bindController(new LevelEditorController());
       //view.getShopPanel().bindController(new ShopController());
       //view.getSettingsPanel().bindController(new SettingsController());
   }

}
