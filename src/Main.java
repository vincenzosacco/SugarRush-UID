import controller.Controller;
import utils.Resources;
import view.View;

import javax.swing.SwingUtilities;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Resources.loadAllResources(()->{
                SwingUtilities.invokeLater(()->{
                    Controller.bind();
                    View.getInstance().launchView();
                    View.getInstance().showPanel(View.PanelName.CUSTOM_TABBED_PANE.getName());
                });
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}