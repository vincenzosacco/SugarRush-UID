package View;

import Controller.SugarController;
import Model.game.Constants.Block;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;

import static Config.View.*;


/**
 * Main game panel
 */
public class SugarPanel extends JPanel {

    private SugarController controller;

    Image wallImage;
    Image creatureImage;
    Image sugarImage;


    public SugarPanel() {
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        Color skyblue = new Color(0, 188, 220);
        setBackground(skyblue);
        this.setFocusable(true);

        //load images
        wallImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/wall.jpg"))).getImage();
        creatureImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/creature.jpg"))).getImage();
        sugarImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/sugar.jpg"))).getImage();
    }

    public void setController(SugarController controller) {
        this.controller = controller;
        this.addKeyListener(controller);
        this.requestFocusInWindow();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (controller == null) {
            throw new IllegalStateException("Controller is null");
        }

        List<List<Block>> gameMatrix = controller.getModel().gameMatView;

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

}
