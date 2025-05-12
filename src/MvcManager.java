import Controller.GameController;
import Model.game.Game;
import View.GamePanel;
import View.MainFrame;

/**
 * <p>
 * Provides functionality for initializing and launching the Model-View-Controller (MVC) structure
 * for the "Sugar Rush" game application. This utility class sets up the game components, connects
 * the model, view, and controller, and starts the game interface.
 * </p>
 * Responsibilities include: <p>
 * - Instantiating and associating the panels with the main application frame. <P>
 * - Creating the game controller and linking it with the model and view. <p>
 *
 * @apiNote This class is the entry point for the game's MVC structure.
 */
public class MvcManager {
    public static void launchMVC(){
        GamePanel gamePanel =  MainFrame.getInstance().addGamePanel();
        GameController gameController = new GameController(Game.getInstance(), gamePanel);
        gamePanel.setController(gameController);

        MainFrame.getInstance().launch();
    }

}
