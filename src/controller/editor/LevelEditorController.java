package controller.editor;

import model.editor.LevelEditor;
import view.impl.home.editor.LevelEditorPanel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class LevelEditorController extends KeyAdapter {
    private final LevelEditor model;
    private final LevelEditorPanel view;
    public LevelEditorController(LevelEditorPanel view, LevelEditor model) {
        this.model = model;
        this.view = view;
    }


    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        view.repaint();
    }
}