package view.editor;

import controller.ControllerObj;
import model.game.GameConstants;
import view.ViewComp;
import view.game.BlocksImages;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class LevelEditorPanel extends JPanel implements ViewComp {
    private final JPanel sidebar = new JPanel();

    public LevelEditorPanel(){

       this.setBackground(Color.RED);
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
