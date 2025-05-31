package view.settings;

import controller.ControllerObj;
import view.ViewComp;

import javax.management.ValueExp;
import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel implements ViewComp {
    public SettingsPanel(){
        this.setBackground(Color.GRAY);
    }

    @Override
    public void bindController(ControllerObj controller) {

    }
}
