package view.impl.home.editor;

import config.ViewConfig;
import controller.editor.LevelEditorController;
import model.editor.LevelEditor;
import view.base.AbsViewPanel;
import view.impl.game.BlocksImages;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class LevelEditorPanel extends AbsViewPanel {
    private final JPanel sidebar = new JPanel();

    public LevelEditorPanel(){
        super(); // bindController() is called here

        this.setBackground(ViewConfig.GAME_BG);
        // Main layout
        setLayout(new BorderLayout());

        // Make components
        makeSidebar();

        // Add panels to frame
//        add(sidebar, BorderLayout.WEST);

    }

    @Override
    protected void bindController() {
        this.addKeyListener(new LevelEditorController(this, LevelEditor.getInstance()));
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent (g);

        // BLOCK SELECTION //
        g.setColor(Color.WHITE);
        g.drawRect(0, 0, ViewConfig.TILE_SIZE , ViewConfig.TILE_SIZE );
    }

    // MAKE COMPONENTS //

    private void makeSidebar() {
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.LIGHT_GRAY);

        // ADD A BUTTON IMAGE FOR EACH BLOCK TYPE //

        for (BufferedImage bImg: BlocksImages.getInstance().getAllDefaultImgs()) {
            JButton button = new JButton(new ImageIcon(bImg));
            sidebar.add(button);
        }
    }


}