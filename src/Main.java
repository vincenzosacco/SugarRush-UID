import controller.Controller;
import model.profile.ProfileManager;
import utils.Resources;
import view.View;

import javax.swing.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        // Register shutdown hook to save the last profile on exit

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ProfileManager.saveProfile(ProfileManager.getLastProfile()); // TODO fix this. What happens when there is more than one profile?
            System.out.println("Profile saved on exit.");
        }));
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