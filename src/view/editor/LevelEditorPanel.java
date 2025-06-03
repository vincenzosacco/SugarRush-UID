package view.editor;

import controller.ControllerObj;
import view.ViewComp;

import javax.swing.*;
import java.awt.*;

public class LevelEditorPanel extends JPanel implements ViewComp {
    public LevelEditorPanel(){
        this.setBackground(Color.RED);
    }

    @Override
    public void bindController(ControllerObj controller) {

    }
}
