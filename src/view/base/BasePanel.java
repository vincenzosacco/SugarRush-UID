package view.base;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public abstract class BasePanel extends JPanel {
    public BasePanel() {
        super();
        this.setFocusable(true);
//        this.addComponentListener(new ComponentAdapter() {
//            @Override
//            public void componentShown(ComponentEvent e) {
//                super.componentShown(e);
//                requestFocusInWindow(); // Request focus when the panel is shown
//            }
//        });
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();
    }
}
