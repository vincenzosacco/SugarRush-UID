package View;

import Model.game.Constants.Block;
import Model.game.Game;
import View.settings.GameSettingsPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;

import static Config.View.*;


/**
 * Main game panel
 */
public class GamePanel extends AbsView{
    Image wallImage;
    Image creatureImage;
    Image sugarImage;

    public GamePanel() {
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        Color skyblue = new Color(0, 188, 220);
        setBackground(skyblue);
        this.setFocusable(true);

        this.add(gameSettingsPanel);

        //load images
        wallImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/wall.jpg"))).getImage();
        creatureImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/creature.jpg"))).getImage();
        sugarImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/sugar.jpg"))).getImage();
    }

    @Override
    protected void draw(Graphics g) {
        // assert controller != null  is done in AbsView.paintComponent()

        Game model = (Game) this.controller.getModel();
        assert model != null : "Model should not be null here";

        List<List<Block>> gameMatrix = model.gameMatView;
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
                    // space is not drawn
                }
            }
        }
    }

    // SETTINGS
    private final GameSettingsPanel gameSettingsPanel = new GameSettingsPanel();
    public GameSettingsPanel toggleSettingsPanel(){
        gameSettingsPanel.setOpen(!gameSettingsPanel.isOpen());
        gameSettingsPanel.setVisible(gameSettingsPanel.isOpen()); // if isOpen == false set gameSettingsPanel invisible
        this.revalidate();
        return gameSettingsPanel;
    }
}
