package view;

import controller.ControllerObj;

import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.Color;
import java.awt.event.ActionListener;

public class StartMenuPanel extends JPanel implements ViewComp {

    private final JButton startGameButton = new JButton("Start Game");

    StartMenuPanel(){
        this.setBackground(Color.GRAY);
        this.add(startGameButton);
    }

    /**
     * Registers an action listener to the start game button.
     * This allows the provided listener to handle action events triggered when the button is pressed.
     *
     * @param actionListener the {@code ActionListener} to be added to the start game button, defining the action to be executed upon interaction.
     */
    @Override
    public void bindController(ControllerObj actionListener) {
        if ( startGameButton.getActionListeners().length != 0){
            throw new IllegalStateException("Cannot have more than one controller");
        }
        if (! (actionListener instanceof ActionListener) ){
            throw new IllegalArgumentException("Controller must be of type ActionListener");
        }

        startGameButton.addActionListener((ActionListener) actionListener);
    }


}
