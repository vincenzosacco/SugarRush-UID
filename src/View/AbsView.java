package View;

import Controller.IController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

/**
 * Abstract class representing the base common contract for any view implementation.
 * (except the Main Frame)
 */
public abstract class AbsView extends JPanel implements IView {
    /**
     * The model from which retrieve data for the view.
     */
    IController controller;

    @Override
    public void setController(IController controller){
        this.controller = controller;

        // add listener to JPanel
        if (controller instanceof KeyListener){
            addKeyListener((KeyListener) this.controller);
        } else if (controller instanceof MouseListener ) {
            addMouseListener((MouseListener) this.controller);
        }
        this.requestFocusInWindow();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        assert controller != null : "Controller should not be null here. setController must be called";
        draw(g);
    }

    protected abstract void draw(Graphics g);
}
