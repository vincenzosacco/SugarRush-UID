package view.editor;

import config.ViewConfig;
import controller.ControllerObj;
import view.ViewComp;
import view.game.BlocksImages;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class LevelEditorPanel extends JPanel implements ViewComp {
    private final JPanel sidebar = new JPanel();

    public LevelEditorPanel(){

       this.setBackground(ViewConfig.GAME_BG);
        // Main layout
        setLayout(new BorderLayout());

        // Make components
        makeSidebar();

        // Add panels to frame
        add(sidebar, BorderLayout.WEST);

    }

    @Override
    public void bindController(ControllerObj controller) {

    }

    @Override
    public void paintComponents(Graphics g) {


        super.paintComponents(g);
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
