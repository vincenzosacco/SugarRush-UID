package view;

import controller.GameController;
import controller.ControllerObj;
import model.Model;
import model.game.Constants.Block;
import model.game.Game;
import view.settings.GameSettingsPanel;

import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import java.util.List;
import java.util.Objects;

import static config.View.*;


/**
 * Main game panel
 */
public class GamePanel extends JPanel implements ViewComp {
    private final Image wallImage, creatureImage, sugarImage, enemy1Image, thornsImage;

    public GamePanel() {
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        Color skyblue = new Color(0, 188, 220);
        setBackground(skyblue);

        this.add(gameSettings);

        //load images TODO move from here, there are more efficiente approach. Use BufferedReader
        wallImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/wall.jpg"))).getImage();
        creatureImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/creature.jpg"))).getImage();
        sugarImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/sugar.jpg"))).getImage();
        enemy1Image = new ImageIcon(Objects.requireNonNull(getClass().getResource("/enemy1.jpg"))).getImage();
        thornsImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/thorns.jpg"))).getImage();
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
    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
            this.requestFocusInWindow(); // needed to get user input
            assert this.getParent() != null ;
            JOptionPane.showMessageDialog(this.getParent(),"Try to reach the sugar piece",
                    "New Game",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    protected void draw(Graphics g) {
        Game game = Model.getInstance().getGame();

        List<List<Block>> gameMatrix = game.getState();
        assert gameMatrix != null && !gameMatrix.isEmpty();

        // DRAW //
        for (int row = 0; row < gameMatrix.size(); row++) {
                int y = row * TILE_SIZE; // iterating row in model matrix corresponds to moving on y-axis(from top to bottom) on graphics coordinates.
            for (int col = 0; col < gameMatrix.get(row).size(); col++) {
                int x = col * TILE_SIZE; // iterating col in model matrix corresponds to moving on x-axis(from left to right) on graphics coordinates.


                Block block = gameMatrix.get(row).get(col);

                switch (block) {
                    case WALL -> g.drawImage(wallImage, x, y, TILE_SIZE, TILE_SIZE, null);
                    case SUGAR -> g.drawImage(sugarImage, x, y, TILE_SIZE, TILE_SIZE, null);
                    case CREATURE -> g.drawImage(creatureImage, x, y, TILE_SIZE, TILE_SIZE, null);
                    case ENEMY1 -> g.drawImage(enemy1Image, x, y, TILE_SIZE, TILE_SIZE, null);
                    case THORNS -> g.drawImage(thornsImage, x, y, TILE_SIZE, TILE_SIZE, null);
                    // space is not drawn
                }
            }
        }
    }

    // SETTINGS
    private final GameSettingsPanel gameSettings = new GameSettingsPanel();
    public GameSettingsPanel toggleSettingsPanel(){
        gameSettings.setOpen(!gameSettings.isOpen());
        gameSettings.setVisible(gameSettings.isOpen()); // if isOpen == false set gameSettingsPanel invisible
        this.revalidate();
        return gameSettings;
    }



}
