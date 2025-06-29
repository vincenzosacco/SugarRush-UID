import controller.Controller;
import model.profile.Profile;
import model.profile.ProfileManager;
import utils.Resources;
import view.View;

import javax.swing.SwingUtilities;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        // Register shutdown hook
        Profile lastProfile = ProfileManager.loadLastProfile();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ProfileManager.saveProfile(lastProfile);
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