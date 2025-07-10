package controller.game.dialogs;

import model.game.Game;
import view.impl.game.dialogs.WinDialog;

import java.awt.event.ComponentEvent;

public class WinController extends _BaseEndLevelController{

    // Ensure that the view is of type WinPanel
    public WinController(WinDialog view) {
        super(view);
    }

    @Override
    public void componentShown(ComponentEvent e) {
        super.componentShown(e);

        int stars = Game.getInstance().getStarCount(); // Get the number of stars earned in the level

        WinDialog winPanel = (WinDialog) this.view; // cast cause parent use _BaseEndLevelPanel
        winPanel.setCoins(stars *10);  // TODO Replace 10 with a config constant
        winPanel.setStars(stars);
    }

}