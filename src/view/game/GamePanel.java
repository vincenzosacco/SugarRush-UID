package view.game;

import controller.GameController;
import controller.ControllerObj;
import controller.GameLoop;
import model.Model;
import utils.audio.GameAudioController;
import view.ViewComp;
import view.button.PauseButton;
import view.menu.GameMenuPanel;
import view.menu.endLevel.LosePanel;
import view.menu.endLevel.WinPanel;

import javax.swing.*;

import java.awt.*;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import static config.ViewConfig.*;

/**
 * Main game panel (Contains graphics, UI, menus, pause/win logic)
 */
public class GamePanel extends JPanel implements ViewComp {

    private final GameMenuPanel gameSettingsPanel;
    private final LosePanel losePanel;
    private final WinPanel winPanel;

    private final PauseButton pauseButton;

    private final JLayeredPane layeredPane;

    private final _GameContent gameContentDrawingPanel=new _GameContent();

    private final GameLoop gameLoop=GameLoop.getInstance();

    public PauseButton getPauseButton() {
        return pauseButton;
    }

    public JPanel getGameContentDrawingPanel() {
        return gameContentDrawingPanel;
    }

    public GamePanel() {
        setLayout(new GridLayout(1,1));
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        Color skyblue = new Color(0, 188, 250);
        setBackground(skyblue);

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
        gameContentDrawingPanel.setBackground(skyblue);
        layeredPane.add(gameContentDrawingPanel, JLayeredPane.DEFAULT_LAYER); // Added to the lowest level

        pauseButton=new PauseButton();
        pauseButton.setEnabled(true);
        int minSizePanel=Math.min(BOARD_WIDTH,BOARD_HEIGHT);
        int buttonSize=Math.max(30,minSizePanel/15);
        pauseButton.setPreferredSize(new Dimension(buttonSize, buttonSize));
        pauseButton.setMinimumSize(new Dimension(buttonSize, buttonSize));
        pauseButton.setMaximumSize(new Dimension(buttonSize, buttonSize));

        pauseButton.addActionListener(e -> {
            // PAUSE GAME when the game menu is opened
            if (gameLoop.isRunning()) {
                gameLoop.pause();
            }
            // RESTORE GAME when the game menu is closed
            else {
                gameLoop.start();
            }

            toggleSettingsPanel();
        });

        // Container panel for the pause button and aligned to the RIGHT
        JPanel buttonContainerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10)); //with padding
        buttonContainerPanel.setOpaque(false);
        buttonContainerPanel.add(pauseButton);
        layeredPane.add(buttonContainerPanel, JLayeredPane.PALETTE_LAYER); // Added to a higher level of the game

        //Menu panels (overlays)
        gameSettingsPanel = new GameMenuPanel();
        losePanel = new LosePanel();
        winPanel = new WinPanel();

        // Add menu panels to the MODAL_LAYER layer (the top one, for overlay)
        layeredPane.add(gameSettingsPanel, JLayeredPane.MODAL_LAYER);
        layeredPane.add(losePanel, JLayeredPane.MODAL_LAYER);
        layeredPane.add(winPanel, JLayeredPane.MODAL_LAYER);

        gameSettingsPanel.setVisible(false);
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

                // Resize and reposition the panel containing the pause button
                int currentButtonSize = Math.min(getWidth(), getHeight());
                currentButtonSize = Math.max(30, currentButtonSize/15);
                pauseButton.setPreferredSize(new Dimension(currentButtonSize, currentButtonSize));
                pauseButton.setMinimumSize(new Dimension(currentButtonSize, currentButtonSize));
                pauseButton.setMaximumSize(new Dimension(currentButtonSize, currentButtonSize));
                // The height of the button container should fit the size of the button plus the padding
                buttonContainerPanel.setBounds(0, 0, getWidth(), currentButtonSize + 20);
                buttonContainerPanel.revalidate();

                // Update the position of the menu panels (centered)
                applyPanelBounds(gameSettingsPanel);
                applyPanelBounds(losePanel);
                applyPanelBounds(winPanel);

                layeredPane.revalidate();
                layeredPane.repaint();
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
    public void setElapsedSeconds(long seconds) {
        timerCountLabel.setText(String.valueOf(seconds));
    }

    /**
     * Binds the controller to the view component.
     * Only one controller can be bound to this view component.
     * @param controller the controller to be bound to the view component, must be a {@link GameController}
     */
    @Override
    public void bindController(ControllerObj controller) {
        if (!(controller instanceof GameController)) {
            throw new IllegalArgumentException("controller must be instance of GameController");
        }
        if (this.getKeyListeners().length != 0){
            throw new IllegalStateException("Cannot have more than one key listener");
        }

        this.addKeyListener((GameController) controller);

        this.setFocusable(true);
    }

    /**
     * Calls {@code super().setVisible(aFlag)} and {@code if aFlag == true}:
     * <p>
     *     - calls {@code this.requestFocusInWindow()} -> so this panel can get input from user
     * </p>
     * <p>
     *     - show 'start new game' messageDialog.
     * </p>
     */

    public void repaintBackground() {
        gameContentDrawingPanel.staticBackground = null; // Force background to redraw on next paintComponent
        this.repaint(); // Requires the panel to redraw itself
    }

    // SETTINGS
    public GameMenuPanel toggleSettingsPanel(){
        int currentLevel = Model.getInstance().getGame().getCurrLevel();
        gameSettingsPanel.setCurrentLevel(currentLevel); // Update the level in the settings panel
        gameSettingsPanel.updateContent(); // Update texts/coins

        gameSettingsPanel.setOpen(!gameSettingsPanel.isOpen()); // Invert the opening state
        gameSettingsPanel.setVisible(gameSettingsPanel.isOpen()); // Make visible/invisible
        if(gameSettingsPanel.isOpen()){
            pauseButton.setEnabled(false);
            GameAudioController.getInstance().stopBackgroundMusic(); // Stop game music when menu is open
        }
        else{
            pauseButton.setEnabled(true);
        }

        this.revalidate();
        this.repaint();
        return gameSettingsPanel;
    }

    //END LEVEL
    public LosePanel loseLevel(){
        int currentLevel = Model.getInstance().getGame().getCurrLevel();
        losePanel.setCurrentLevel(currentLevel);
        losePanel.updateLabels(currentLevel);
        losePanel.setVisible(true);
        applyPanelBounds(losePanel);
        losePanel.requestFocusInWindow();
        pauseButton.setEnabled(false);
        GameAudioController.getInstance().playSfx("death"); // Play Death SFX
        GameAudioController.getInstance().stopBackgroundMusic(); // Stop game music
        this.revalidate();
        this.repaint();
        return losePanel;
    }
    //WIN LEVEL
    public WinPanel winLevel(){
        int currentLevel = Model.getInstance().getGame().getCurrLevel();
        winPanel.setCurrentLevel(currentLevel);
        winPanel.updateLabels(currentLevel);
        winPanel.setVisible(true);
        applyPanelBounds(winPanel);
        winPanel.requestFocusInWindow();
        pauseButton.setEnabled(false);
        GameAudioController.getInstance().playSfx("win"); // Play Victory SFX
        GameAudioController.getInstance().stopBackgroundMusic(); // Stop game music
        this.revalidate();
        this.repaint();
        return winPanel;
    }
    //END GAME
    public void endGame(){
        GameLoop.getInstance().shutdown();
        GameAudioController.getInstance().stopBackgroundMusic();
    }
}