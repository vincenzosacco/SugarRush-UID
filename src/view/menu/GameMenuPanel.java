package view.menu;

import controller.ControllerObj;
import view.ViewComp;

import javax.swing.*;
import java.awt.*;

/**
 * Panel for game settings
 */
public class GameMenuPanel extends JPanel implements ViewComp {
    @Override
    public void bindController(ControllerObj controller) {

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

    /** add all components to this panel */
    private void addComponents() {
        // Use a BoxLayout for vertical alignment
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        Label languageLabel1 = new Label("Continue");
        Label languageLabel2 = new Label("Restart");
        Label languageLabel3 = new Label("Exit");

        // Align labels to the center horizontally
        languageLabel1.setAlignment(Label.CENTER);
        languageLabel2.setAlignment(Label.CENTER);
        languageLabel3.setAlignment(Label.CENTER);

        this.add(languageLabel1);
        this.add(languageLabel2);
        this.add(languageLabel3);
    }

}
