package controller.menu;

import controller.IControllerObj;
import view.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartMenuController  implements ActionListener, IControllerObj {
    // when start game is pressed
    @Override
    public void actionPerformed(ActionEvent e) {

        View.getInstance().showPanel(View.PanelName.GAME.getName());
    }
}
