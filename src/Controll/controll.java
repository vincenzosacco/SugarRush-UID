package Controll;

import View.SugarPanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import View.SugarPanel;

public class controll implements KeyListener {

    private final SugarPanel sugarpanel;
    public controll(SugarPanel sugarpanel){
        this.sugarpanel = sugarpanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
