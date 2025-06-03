package view.menu;

import controller.ControllerObj;
import controller.menu.GameMenuController;
import model.Model;
import view.ViewComp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel for game settings
 */
public class GameMenuPanel extends JPanel implements ViewComp {
    private GameMenuController controller;

    @Override
    public void bindController(ControllerObj controller) {
        if (!(controller instanceof GameMenuController)) {
            throw new IllegalArgumentException("controller must be instance of GameMenuController");
        }
        this.controller = (GameMenuController) controller;
    }

    private boolean isOpen = false;

    public GameMenuPanel(){

        setPreferredSize(new Dimension(100, 100));
        setBackground(Color.WHITE);

        addComponents();

        this.setFocusable(true);
        this.setVisible(false);
        this.revalidate();
    }

//    public boolean isOpen() {
//        return isOpen;
//    }
//
//    public void setOpen(boolean open) {
//        isOpen = open;
//    }
    public void toggle(){
        isOpen = !isOpen;
        this.setVisible(this.isOpen); // if isOpen == false set gameSettingsPanel invisible
    }

    /** add all components to this panel */
    private void addComponents() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JButton choice1 = new JButton("Continue");
        JButton choice2 = new JButton("Restart");
        JButton choice3 = new JButton("Exit");

        int buttonWidth = getPreferredSize().width;
        int buttonHeight = 34; // or any preferred height

        Dimension buttonSize = new Dimension(buttonWidth, buttonHeight);

        choice1.addActionListener(e -> {
            controller.onContinue();
        });
        choice2.addActionListener(e -> {
            controller.onRestart();
        });
        choice3.addActionListener(e -> {
            controller.onExit();
        });

        for (JButton btn : new JButton[]{choice1, choice2, choice3}) {
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(buttonSize);
            btn.setMinimumSize(buttonSize);
            btn.setPreferredSize(buttonSize);
        }

        this.add(choice1);
        this.add(choice2);
        this.add(choice3);
    }

}
