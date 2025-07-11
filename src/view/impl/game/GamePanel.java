package view.impl.game;

import controller.game.GameController;
import model.Model;
import model.game.Game;
import utils.audio.GameAudioController;
import view.base.BasePanel;
import view.impl._common.buttons.CustomRoundLogoButton;
import view.impl.game.dialogs.LoseDialog;
import view.impl.game.dialogs.WinDialog;
import view.impl.game.dialogs._EndLevelDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import static config.ViewConfig.*;

/**
 * Main game panel (Contains graphics, UI, menus, pause/win logic)
 */
public class GamePanel extends BasePanel {

    private final GameMenu gameMenu;

    private final LoseDialog losePanel;
    private final WinDialog winPanel;

    private final CustomRoundLogoButton pauseButton;

    private final JLayeredPane layeredPane;

    private final _GameContent gameContentDrawingPanel=new _GameContent();

    public CustomRoundLogoButton getPauseButton() {
        return pauseButton;
    }

    public JPanel getGameContentDrawingPanel() {
        return gameContentDrawingPanel;
    }

    public GamePanel() {
        setLayout(new GridLayout(1,1));
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        setBackground(GAME_BG);

        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        this.add(layeredPane);

        // Timer label
        JLabel timerLabel = new JLabel("Time: ");
        timerLabel.setForeground(Color.BLACK);
        Font timerFont = new Font("Arial", Font.BOLD, 20);
        timerLabel.setFont(timerFont);
        timerLabel.setBounds(10, 5, 100, 30); // Positioning the timer label
        layeredPane.add(timerLabel); // Add the timer label to the panel
        timerCountLabel.setBounds(80, 5, 100, 30); // Positioning the timer count label
        timerCountLabel.setFont(timerFont);
        timerCountLabel.setForeground(Color.BLACK);
        layeredPane.add(timerCountLabel);

        gameContentDrawingPanel.setOpaque(true); //Opaque to cover the background underneath
        gameContentDrawingPanel.setBackground(GAME_BG);
        layeredPane.add(gameContentDrawingPanel, JLayeredPane.DEFAULT_LAYER); // Added to the lowest level

        pauseButton=new CustomRoundLogoButton("pause",Color.WHITE);
        pauseButton.setEnabled(true);
        int minSizePanel=Math.min(BOARD_WIDTH,BOARD_HEIGHT);
        int buttonSize=Math.max(30,minSizePanel/15);
        pauseButton.setPreferredSize(new Dimension(buttonSize, buttonSize));
        pauseButton.setMinimumSize(new Dimension(buttonSize, buttonSize));
        pauseButton.setMaximumSize(new Dimension(buttonSize, buttonSize));


        // Container panel for the pause buttons and aligned to the RIGHT
        JPanel buttonContainerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10)); //with padding
        buttonContainerPanel.setOpaque(false);
        buttonContainerPanel.add(pauseButton);
        layeredPane.add(buttonContainerPanel, JLayeredPane.PALETTE_LAYER); // Added to a higher level of the game

        //Menu panels (overlays)
        int levelIndex = Game.getInstance().getCurrLevel();
        gameMenu = new GameMenu(levelIndex);
        gameMenu.setupKeyBindings(); // ESC -> close menu

        losePanel = new LoseDialog(levelIndex);
        winPanel = new WinDialog(levelIndex);

        // Add menu panels to the MODAL_LAYER layer (the top one, for overlay)
        layeredPane.add(gameMenu, JLayeredPane.MODAL_LAYER);
        layeredPane.add(losePanel, JLayeredPane.MODAL_LAYER);
        layeredPane.add(winPanel, JLayeredPane.MODAL_LAYER);

        gameMenu.setVisible(false);
        losePanel.setVisible(false);
        winPanel.setVisible(false);

        //Add a ComponentListener to the main GamePanel to handle resizing
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // When the GamePanel is resized, it also resizes the layeredPane
                layeredPane.setBounds(0, 0, getWidth(), getHeight());

                // Resize and position the game drawing panel
                gameContentDrawingPanel.setBounds(0, 0, getWidth(), getHeight());

                // Resize and reposition the panel containing the pause buttons
                int currentButtonSize = Math.min(getWidth(), getHeight());
                currentButtonSize = Math.max(30, currentButtonSize/15);
                pauseButton.setPreferredSize(new Dimension(currentButtonSize, currentButtonSize));
                pauseButton.setMinimumSize(new Dimension(currentButtonSize, currentButtonSize));
                pauseButton.setMaximumSize(new Dimension(currentButtonSize, currentButtonSize));
                // The height of the buttons container should fit the size of the buttons plus the padding
                buttonContainerPanel.setBounds(0, 0, getWidth(), currentButtonSize + 20);
                buttonContainerPanel.revalidate();

                // Update the position of the menu panels (centered)
                applyPanelBounds(gameMenu);
                applyPanelBounds(losePanel);
                applyPanelBounds(winPanel);

                layeredPane.revalidate();
                layeredPane.repaint();
            }
        });

        // KEY BINDINGS
        setupKeyBindings();
    }

    private void setupKeyBindings(){
    //--Make press "ESC" to virtual click on the pause button //
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = this.getActionMap();

        // Create an InputStroke for the ESCAPE key
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke("ESCAPE");
        // Put the KeyStroke and an identifier (e.g., "pressClose") into the InputMap
        inputMap.put(escapeKeyStroke, "pressClose");
        // Associate the identifier with an AbstractAction in the ActionMap
        actionMap.put("pressClose", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Programmatically trigger the action listener of the closeButton
                pauseButton.doClick();
            }
        });

    }

    // Method to calculate and apply centered bounds to menu panels
    private void applyPanelBounds(JPanel panel) {
        if (panel == null) return;

        int parentWidth = getWidth();
        int parentHeight = getHeight();

        int panelWidth = parentWidth / 2;
        int panelHeight = parentHeight / 2;

        // Set a minimum
        panelWidth = Math.max(panelWidth, 300);
        panelHeight = Math.max(panelHeight, 200);

        // Coordinates to center the panel
        int panelX = (parentWidth - panelWidth) / 2;
        int panelY = (parentHeight - panelHeight) / 2;

        panel.setBounds(panelX, panelY, panelWidth, panelHeight);
        panel.revalidate();
    }

    private final JLabel timerCountLabel = new JLabel("0");
    public void setElapsedTime(long seconds) {
        timerCountLabel.setText(String.valueOf(seconds));
    }

    public void repaintBackground() {
        gameContentDrawingPanel.staticBackground = null; // Force background to redraw on next paintComponent
        this.repaint(); // Requires the panel to redraw itself
    }

    // SETTINGS
    public GameMenu toggleMenu(){
//        int currentLevel = Model.getInstance().getGame().getCurrLevel();
//        gameMenu.setCurrentLevel(currentLevel); // Update the level in the settings panel
//        gameMenu.updateContent(); // Update texts/coins

        gameMenu.setVisible(!gameMenu.isVisible()); // Make visible/invisible
        if(gameMenu.isVisible()){
            pauseButton.setEnabled(false);
            GameAudioController.getInstance().stopBackgroundMusic(); // Stop game music when menu is open
        }
        else{
            pauseButton.setEnabled(true);
        }

        this.revalidate();
        this.repaint();
        return gameMenu;
    }


    /**
     * Ends the currentlevel and shows the appropriate dialog (win or lose).
     * @param isWin true if the player won the level, false if lost
     * */
    public void endLevel(boolean isWin) {
        _EndLevelDialog dialog = isWin ? winPanel : losePanel;
        dialog.setCurrentLevel(Game.getInstance().getCurrLevel());
        dialog.updateElapsedTime(Game.getInstance().getElapsedTime());
        dialog.setVisible(true);

        pauseButton.setEnabled(false); // Disable the pause button when the level ends
        GameAudioController.getInstance().stopBackgroundMusic(); // Stop game music when the level ends
    }

    //------------------------------------- CONTROLLER RELATED METHODS -------------------------------------------------------
    @Override
    public void bindControllers() {
        GameController controller = new GameController();
        this.addKeyListener(controller);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                // MUSIC STARTS when the game panel is shown
                GameAudioController.getInstance().stopBackgroundMusic();
                GameAudioController.getInstance().playGameMusic();
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                // MUSIC STOPS when the game panel is hidden
                GameAudioController.getInstance().stopBackgroundMusic();
            }
        });

        // PAUSE BUTTON
        pauseButton.addActionListener(controller::onPause);

    }


    public void clickPause() {
        if (!pauseButton.isEnabled())
            pauseButton.setEnabled(true);

        pauseButton.doClick();
    }



    //------------------------------------- SWINGs OVERRIDE METHODS -------------------------------------------------------
    @Override
    public void addNotify() { // this is called when a container adds this panel
        super.addNotify(); //<-- requests focus here

        // Reset the elapsed time
        setElapsedTime(Game.getInstance().getElapsedTime());

        // Reset components
        gameContentDrawingPanel.setVisible(true);
        gameMenu.setVisible(false);
        losePanel.setVisible(false);
        winPanel.setVisible(false);
        pauseButton.setEnabled(true); // Enable the pause buttons when the game starts
        // Repaint the background
        repaintBackground();
    }

}