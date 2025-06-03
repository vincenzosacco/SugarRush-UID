package controller.menu;

import controller.ControllerObj;
import view.View;
import view.ViewComp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameMenuController implements ControllerObj {
    private final JPanel view;

    public GameMenuController(JPanel view) {
        this.view = view;
    }
    /**
     * Define what happens when continue button is pressed
     */
    public void onContinue() {
        this.view.setVisible(false);

    }
}
