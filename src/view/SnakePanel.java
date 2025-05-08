package view;

import config.Settings;
import model.Game;
import model.Position;
import model.World;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class SnakePanel extends JPanel {

    public SnakePanel() {
        reset();
    }

    public void reset() {
        this.setBackground(Color.WHITE);
    }

//    Apple image
    private static final Image apple;

    static {
        InputStream inStream = SnakePanel.class.getResourceAsStream("/apple.jpg");
        try {
            apple = ImageIO.read(Objects.requireNonNull(inStream)).getScaledInstance(Settings.BLOCK_SIZE,10, Image.SCALE_SMOOTH);
            inStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    private void drawEnd(Graphics g, String message) {
        this.setBackground(Color.DARK_GRAY);
        g.setFont(new Font("arial", Font.PLAIN, 20));
        g.setColor(Color.WHITE);
        g.drawString(message, Settings.WINDOW_SIZE/20, Settings.WINDOW_SIZE/2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Game game = Game.getGame();
        if (!game.isAlive()) {
            drawEnd(g, "Game over! Press n to start a new game");
            return;
        } else if (game.win()) {
            drawEnd(g, "You win! Press n to start a new game");
            return;
        }

        World world = game.getWorld();
        for (int i = 0; i < world.getSize(); i++) {
            for (int j = 0; j < world.getSize(); j++) {
                Position p = new Position(i, j);
                if(world.isEmpty(p))
                    continue;
                Color c = Color.BLACK;
                boolean drawOval = false;
                if(world.isGrass(p)) c = Color.GREEN;
                else if (world.isSnakeHead(p)) drawOval = true;
                else if(world.isApple(p)){
                    g.drawImage(apple,i * Settings.BLOCK_SIZE, j * Settings.BLOCK_SIZE,this);
                    continue;
                }
//                else if(world.isApple(p)) c = Color.RED;
                g.setColor(c);
                if(drawOval)
                    g.fillOval(i * Settings.BLOCK_SIZE, j * Settings.BLOCK_SIZE, Settings.BLOCK_SIZE,
                            Settings.BLOCK_SIZE);
                else
                    g.fillRect(i * Settings.BLOCK_SIZE, j * Settings.BLOCK_SIZE, Settings.BLOCK_SIZE,
                            Settings.BLOCK_SIZE);


            }
        }
    }
}
