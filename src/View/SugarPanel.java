package View;

import Model.Block;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Objects;

public class SugarPanel extends JPanel {

    int rowCount=21;
    int columnCount=19;
    int tileSize=32;
    int boardWidth= columnCount * tileSize;
    int boardHeight= rowCount * tileSize;

    Image wallImage;
    Image creatureImage;
    Image sugarImage;


    HashSet<Block> walls;
    Block creature;
    Block sugar;
    final HashSet<Block> spaces;

    public SugarPanel() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        Color skyblue= new Color(0, 188, 220);
        setBackground(skyblue);

        spaces = new HashSet<>();

        //load images
        wallImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/wall.jpg"))).getImage();
        creatureImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/creature.jpg"))).getImage();
        sugarImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/sugar.jpg"))).getImage();

        MapParser mapParser = new MapParser(this);

        mapParser.loadMap(MapParser.MAP_1); // at the moment keep it here
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
