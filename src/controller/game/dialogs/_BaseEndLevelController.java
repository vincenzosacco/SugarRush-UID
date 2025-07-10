package controller.game.dialogs;

import view.impl.game.dialogs._BaseEndLevelDialog;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

class _BaseEndLevelController extends ComponentAdapter{

    protected final _BaseEndLevelDialog view;

    public _BaseEndLevelController(_BaseEndLevelDialog view) {
        this.view = view;
    }


    @Override
    public void componentResized(ComponentEvent e) {
        view.applyScalingBasedOnCurrentDimensions();
    }

    /**
     * Default implementation set the elapsed time when the panel is shown.
     * @param e the event to be processed
     */
    @Override
    public void componentShown(ComponentEvent e) {
        // Set the elapsed time when the panel is shown
//        int elapsedTime = Game.getInstance().getElapsedTime();
//        view.setElapsedTime(elapsedTime);
    }

}