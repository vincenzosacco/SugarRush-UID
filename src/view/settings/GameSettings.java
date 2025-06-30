package view.settings;

import controller.ControllerObj;
import model.settings.SettingsManager;
import utils.audio.GameAudioController;
import view.View;
import view.ViewComp;
import view.button.RoundCloseButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GameSettings extends BaseSettingsPanel implements ViewComp {

    private final RoundCloseButton closeButton;

    public GameSettings(){
        super();
        this.setName(View.PanelName.SETTINGS.getName());
        closeButton = new RoundCloseButton();

        setupClosableLayout();

        addCloseButtonListener();


    }

    private void setupClosableLayout() {
        // Current layout from BaseSettingsPanel
        GridBagLayout layout = (GridBagLayout) getLayout();
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setOpaque(false);

        GridBagConstraints topGbc = new GridBagConstraints();
        topGbc.insets = new Insets(0, 0, 0, 0); // No padding

        // To push the button to the right
        topGbc.gridx = 0;
        topGbc.gridy = 0;
        topGbc.weightx = 1.0; // To push the button to the right
        topGbc.fill = GridBagConstraints.HORIZONTAL;
        topGbc.anchor = GridBagConstraints.WEST; // Anchored to the left
        topPanel.add(Box.createHorizontalGlue(), topGbc);

        // Adds the panel that contains only the close button (with FlowLayout.RIGHT)
        JPanel closeButtonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)); // No padding
        closeButtonContainer.setOpaque(false);
        closeButtonContainer.add(closeButton);

        topGbc.gridx = 1; //Next column
        topGbc.gridy = 0;
        topGbc.weightx = 0;
        topGbc.fill = GridBagConstraints.NONE;
        topGbc.anchor = GridBagConstraints.EAST; // Anchored to the right
        topGbc.insets = new Insets(5, 5, 5, 5); // Padding for the button
        topPanel.add(closeButtonContainer, topGbc);

        // Added this topPanel to the entire GameSettings in line 0
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Fill all remaining columns
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0; // This panel takes all the extra horizontal space of row 0
        gbc.weighty = 0; // Does not take up extra vertical space
        gbc.anchor = GridBagConstraints.NORTH; // Anchored at the top

        add(topPanel, gbc);


    }

    private void addCloseButtonListener() {
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Close button clicked.");

                SettingsManager.getInstance().revertToSavedSettings();
                GameAudioController.getInstance().stopBackgroundMusic();
                setVisible(false);
            }
        });
    }

    @Override
    public void bindController(ControllerObj controller) {
    }
}