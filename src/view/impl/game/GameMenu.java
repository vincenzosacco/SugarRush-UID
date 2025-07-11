package view.impl.game;

import controller.game.GameMenuController;
import view.impl._common.buttons.CustomButton;
import view.impl._common.buttons.CustomLogoButton;
import view.impl._common.buttons.CustomRoundLogoButton;
import view.impl.home.levelsMap.LevelInfoDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Panel for game settings
 */
public class GameMenu extends LevelInfoDialog {
    protected final CustomLogoButton restartButton = new CustomLogoButton("restart",new Color(255, 193, 7));
    protected final CustomRoundLogoButton settingsButton=new CustomRoundLogoButton("settings",new Color(119, 136, 153));
    protected final CustomButton exitButton =new CustomButton("EXIT",Color.WHITE,new Color(220, 53, 69));

    public GameMenu(int currentLevel){
        super(currentLevel);

    }

    //--------------------------------- PARENT METHODS ---------------------------------------------------------------------------

    @Override
    protected void buildBottomArea(){
//        super.buildBottomArea(); dont call this. i need to override buttons position
        bottomArea.setLayout(new GridLayout(1, 3));

        bottomArea.setOpaque(false);
        bottomArea.add(exitButton);
        bottomArea.add(playButton); // play button from parent
        bottomArea.add(restartButton);

    }

    @Override
    protected void buildTopArea(){
        super.buildTopArea(); // i want super in this case to add the level label
        topArea.removeAll(); // remove all to re-add components in right order (GridLayout)
        JPanel topLeftArea = new JPanel(new BorderLayout());
        topLeftArea.add(settingsButton, BorderLayout.WEST);
        topLeftArea.setOpaque(false);

        topArea.add(topLeftArea);
        topArea.add(levelLabel);
        topArea.add(Box.createHorizontalGlue());

    }

    @Override
    protected void resizeComponents(){
        try {
            setPreferredSize(new Dimension(getParent().getWidth()/2, getParent().getWidth()/2));
        }
        catch (NullPointerException e){
            System.err.println("Parent of GameMenu is null.");
        }

        // BUTTONS //
        int size = Math.min(getWidth(), getHeight()) / 10;
        size = Math.max(size, 30);

        // Resize the buttons
        settingsButton.setPreferredSize(new Dimension(size * 2, size));
        restartButton.setPreferredSize(new Dimension(size * 2, size));

        // set gap between bottom buttons
        try {
            GridLayout layout = (GridLayout) bottomArea.getLayout();
            layout.setVgap(size);
        }
        catch (ClassCastException e){
            throw new AssertionError("Bottom area layout is not a GridLayout.");
        }

        super.resizeComponents();// <-- calls repaint() and revalidate()
    }

    /**
     * Setup key bindings for the menu.
     * The 'ESC' key will trigger the play(resume) button.
     */
    @Override
    public void setupKeyBindings(){
        // Get the InputMap for when the component is focused or one of its children is focused
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        // Get the ActionMap to associate keys with actions
        ActionMap actionMap = this.getActionMap();

        // --- Bind ESCAPE key to Play buttons ---
        // Create an InputStroke for the ENTER key
        KeyStroke enterKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
        // Put the KeyStroke and an identifier (e.g., "pressPlay") into the InputMap
        inputMap.put(enterKeyStroke, "pressPlay");
        // Associate the identifier with an AbstractAction in the ActionMap
        actionMap.put("pressPlay", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Programmatically trigger the action listener of the playButton
                playButton.doClick();
            }
        });

    }

    //--------------------------------- CONTROLLER -----------------------------------------------------------------------------------
    @Override
    protected void bindControllers(){
//        super.bindControllers(); dont call this. i need to override buttons behavior
        GameMenuController controller = new GameMenuController();
        playButton.addActionListener(controller::onResume);
        settingsButton.addActionListener(controller::onSettings);
        restartButton.addActionListener(controller::onRestart);
        exitButton.addActionListener(controller::onExit);
    }
}
