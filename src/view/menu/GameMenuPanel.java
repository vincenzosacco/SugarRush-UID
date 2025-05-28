package view.menu;

import controller.ControllerObj;
import controller.GameController;
import controller.menu.GameMenuController;
import view.ViewComp;

import javax.swing.*;
import java.awt.*;

/**
 * Panel for game settings
 */
public class GameMenuPanel extends JPanel implements ViewComp {
    @Override
    public void bindController(ControllerObj controller) {
        if (!(controller instanceof GameMenuController)) {
            throw new IllegalArgumentException("controller must be instance of GameMenuController");
        }
//        if (this.getKeyListeners().length != 0){
//            throw new IllegalStateException("Cannot have more than one key listener");
//        }

        GameMenuController gameMenuController = (GameMenuController) controller;

        Label1.addActionListener(e -> gameMenuController.onContinue());


    }

    private boolean isOpen = false;

    public GameMenuPanel(){
        setPreferredSize(new Dimension(100, 100));
        setBackground(Color.WHITE);

        addComponents();

        this.setFocusable(true);
        this.setVisible(false);
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }


    private JButton Label1, Label2, Label3;
    /** add all components to this panel */
    private void addComponents() {
        // Use a BoxLayout for vertical alignment
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        Label1 = new JButton("Continue");
        Label2 = new JButton("Restart");
        Label3 = new JButton("Exit");

//        // Align labels to the center horizontally
//        Label1.setAlignment(Label.CENTER);
//        Label2.setAlignment(Label.CENTER);
//        Label3.setAlignment(Label.CENTER);

        this.add(Label1);
        this.add(Label2);
        this.add(Label3);
    }

}
