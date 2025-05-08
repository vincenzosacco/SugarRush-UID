package Model;

import java.awt.*;

public class Block {
    public int x;
    public int y;
    public int width; //tile size;
    public int height;
    public Block(Image image, int x, int y, int width, int height) {
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
    }
}
