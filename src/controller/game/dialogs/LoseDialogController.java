package controller.game.dialogs;

import model.game.Game;
import view.impl.game.dialogs.LoseDialog;

import java.awt.event.ActionEvent;

public class LoseDialogController {
    private final LoseDialog loseDialog;

    public LoseDialogController(LoseDialog loseDialog) {
        this.loseDialog = loseDialog;
    }

    public void onRestart(ActionEvent e){
        loseDialog.setVisible(false);
        Game.getInstance().restart(); // also trigger RESTART event in Game Controller
    }

    public void onExit(ActionEvent e){
        loseDialog.setVisible(false);
        Game.getInstance().end(null); // also trigger EXIT event in Game Controller
    }

}
