package controller.menu;

import controller.ControllerObj;
import model.Model;
import view.View;
import view.ViewComp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameMenuController implements ControllerObj {

    public GameMenuController() {
    }
    /**
     * Define what happens when continue button is pressed
     */
    public void onContinue() {
        View view = View.getInstance();
        view.getGamePanel().toggleSettingsPanel(); // close game settings panel
        view.getGamePanel().requestFocusInWindow(); // request focus to game panel

        // resume game loop
        Model.getInstance().getGame().start();
    }

//    public void onRestart() {
//        Model.getInstance().getGame().restart();
//        View.getInstance().getGamePanel().toggleSettingsPanel(); // close game settings panel
//        View.getInstance().getGamePanel().requestFocusInWindow(); // request focus to game panel
//    }
//
//    public void onExit() {
//        Model.getInstance().getGame().stop(); // stop game loop
//        View.getInstance().getGamePanel().toggleSettingsPanel(); // close game settings panel
//        View.getInstance().showPanel(View.PanelName.START_MENU.getName()); // show start menu panel
//    }
}
