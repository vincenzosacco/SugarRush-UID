package view.settings;

import controller.ControllerObj;
import view.ViewComp;

import javax.swing.*;
import java.awt.*;

public class AppSettings extends JPanel implements ViewComp {
    public AppSettings(){

        this.setBackground(Color.GRAY);
        JComboBox<String> languageComboBox = new JComboBox<>(new String[]{"English", "Spanish", "French", "German", "Arabic"});
        JCheckBox MusicCheckBox = new JCheckBox("Music");
        JCheckBox SoundCheckBox = new JCheckBox("Sound Effects");
        JCheckBox NotificationsCheckBox = new JCheckBox("Notifications");
        JButton saveButton = new JButton("Save Settings");
        JButton cancelButton = new JButton("Cancel");
        this.add(MusicCheckBox);
        this.add(SoundCheckBox);
        this.add(NotificationsCheckBox);
        this.add(languageComboBox);
        this.add(saveButton);
        this.add(cancelButton);
        this.setLayout(new GridLayout(6, 1)); // Arrange components in a vertical column
    }

    @Override
    public void bindController(ControllerObj controller) {
    }
}
