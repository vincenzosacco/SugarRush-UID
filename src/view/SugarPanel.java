package View;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Objects;
import Model.Block;

public class SugarPanel extends JPanel {

    int rowCount=21;
    int columnCount=19;
    int tileSize=32;
    int boardWidth= columnCount * tileSize;
    int boardHeight= rowCount * tileSize;

    private Image wallImage;
    private Image creatureImage;
    private Image sugarImage;

    // c=creature, s=sugar, x=wall
    public String[] tileMap={
            "xxxxxxxxxxxxxxxxxxx",
            "xc                x",
            "x                 x",
            "x                 x",
            "x                 x",
            "x                 x",
            "x                 x",
            "x                 x",
            "x                 x",
            "x                 x",
            "x                 x",
            "x                 x",
            "x                 x",
            "x                 x",
            "x                 x",
            "x                 x",
            "x                 x",
            "x                 x",
            "x                 x",
            "xs                x",
            "xxxxxxxxxxxxxxxxxxx",
    };

    HashSet<Block> walls;
    Block creature;
    Block sugar;
    final HashSet<Block> spaces;

    public SugarPanel() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        Color skyblue= new Color(0, 188, 220);
        setBackground(skyblue);

        spaces = new HashSet<Block>();

        //load images
        wallImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/wall.jpg"))).getImage();
        creatureImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/creature.jpg"))).getImage();
        sugarImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/sugar.jpg"))).getImage();

        loadMap();
    }
    public void loadMap(){
        walls = new HashSet<Block>();
        spaces.clear();

        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                int x = c * tileSize;
                int y = r * tileSize;

                if (tileMapChar == 'x') {
                    Block wall = new Block(wallImage, x, y, tileSize, tileSize);
                    walls.add(wall);
                }
                else if (tileMapChar == 'c') {
                    creature = new Block(creatureImage, x, y, tileSize, tileSize);
                }
                else if (tileMapChar == 's') {
                    sugar = new Block(sugarImage, x, y, tileSize, tileSize);
                }
                else if (tileMapChar == ' '){
                    Block space = new Block(null, x, y, 0, 0);
                    spaces.add(space);
                }
            }
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        g.drawImage(creatureImage,creature.x,creature.y,creature.width,creature.height,null);
        g.drawImage(sugarImage,sugar.x,sugar.y,sugar.width,sugar.height,null);
        for (Block wall : walls){
            g.drawImage(wallImage,wall.x,wall.y,wall.width,wall.height, null);
        }
        g.setColor(Color.WHITE);
        for (Block space : spaces){
            g.fillRect(space.x,space.y,space.width,space.height);
        }
    }
}
