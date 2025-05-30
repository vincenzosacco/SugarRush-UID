package view.shop;

import controller.ControllerObj;
import view.ViewComp;

import javax.swing.*;
import java.awt.*;

public class ShopPanel extends JPanel implements ViewComp {
    public ShopPanel(){
        this.setBackground(Color.BLUE);
    }

    @Override
    public void bindController(ControllerObj controller) {

    }
}
