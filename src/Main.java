import persistance.profile.ProfileManager;
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
                    View.getInstance().launch();
                    View.getInstance().showHome();
                });
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}