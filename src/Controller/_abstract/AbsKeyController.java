package Controller._abstract;

import Model.IModel;
import View.AbsView;

import java.awt.event.KeyListener;

/**
 * Abstract class representing the base common contract for any game key controller implementation.
 */
public abstract class AbsKeyController extends AbsController implements KeyListener {
    protected AbsKeyController(IModel model, AbsView view){
        super(model,view);
    }

    @Override
    public void keyTyped(java.awt.event.KeyEvent e) {

    }

    @Override
    public void keyReleased(java.awt.event.KeyEvent e) {

    }

    @Override
    public void keyPressed(java.awt.event.KeyEvent e) {
    }

}
