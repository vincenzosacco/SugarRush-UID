package view.settings;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;

import model.settings.SettingsManager;
import view.button.InternalSettingsButton;

public class BaseSettingsPanel extends JPanel implements PropertyChangeListener {

    private Image backgroundImage;

    private InternalSettingsButton gameControlsButton;
    private JLabel musicLabel;
    private JButton musicSoundButton; // Button with audio icon
    private JSlider musicVolumeSlider;

    private JLabel sfxLabel;
    private JButton sfxSoundButton; // Button with sound effects icon
    private JSlider sfxVolumeSlider;

    private InternalSettingsButton cancelButton;
    private InternalSettingsButton saveButton;


    // Audio icons - will be loaded and scaled dynamically
    private ImageIcon soundIcon;
    private ImageIcon noSoundIcon;


    public BaseSettingsPanel() {
        //setPreferredSize(new Dimension(600, 400)); // Default size for the settings panel
        setOpaque(false);

        gameControlsButton = new InternalSettingsButton("Game controls");

        // Load background image and icons once
        loadImages();

        // Configure the layout of the components (initially with default dimensions)
        setupLayout();
        // Add listeners for buttons and sliders
        addListeners();

        // Register this panel as a listener for settings changes
        SettingsManager.getInstance().addPropertyChangeListener(this);

        // Add a ComponentListener to handle the resizing
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                applyScalingBasedOnCurrentDimensions();
            }
            //Capture the initial volumes when the panel is shown
            @Override
            public void componentShown(ComponentEvent e) {
                SettingsManager.getInstance().revertToSavedSettings();
                // Ensure UI reflects the current settings from SettingsManager
                updateUIFromSettings();
                applyScalingBasedOnCurrentDimensions();
                requestFocusInWindow(); // Ensure the panel has focus
            }
        });

        // Initialize the UI with the current values from the SettingsManager
        SwingUtilities.invokeLater(() -> {
            updateUIFromSettings(); // Call to initialize slider and icon values
            applyScalingBasedOnCurrentDimensions();
        });
    }

    private void loadImages() {
        try {
            URL bgUrl = getClass().getResource("/imgs/panels/settings/settingsBackgroundImage.jpg");
            if (bgUrl == null) {
                System.err.println("Error: settingsBackgroundImage.jpg not found in resource path.");
            } else {
                backgroundImage = ImageIO.read(bgUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading image: " + e.getMessage());
        }
    }

    private ImageIcon getScaledIcon(String path, int size) {
        try {
            URL url = getClass().getResource(path);
            if (url == null) {
                System.err.println("Error: Icon resource not found: " + path);
                return new ImageIcon(new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)); // Fallback
            }
            BufferedImage originalImage = ImageIO.read(url);
            Image scaledImage = originalImage.getScaledInstance(size, size, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (IOException e) {
            e.printStackTrace();
            return new ImageIcon(new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)); // Fallback
        }
    }

    protected void applyScalingBasedOnCurrentDimensions() {
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        if (panelWidth == 0 || panelHeight == 0) return;

        int minPanelSize = Math.min(panelHeight, panelWidth);

        // Calculate font size for labels (Music, Sound Effects)
        int labelFontSize = Math.max(16, minPanelSize / 30);
        musicLabel.setFont(new Font("Arial", Font.BOLD, labelFontSize));
        sfxLabel.setFont(new Font("Arial", Font.BOLD, labelFontSize));

        // Calculate the size of the audio icons
        int iconSize = Math.max(30, minPanelSize / 10);

        // Reload and scale the audio icons with the new size
        soundIcon = getScaledIcon("/imgs/icons/sound.jpg", iconSize);
        noSoundIcon = getScaledIcon("/imgs/icons/noSound.jpg", iconSize);

        // Apply the new icons to the audio buttons
        updateSoundIcon(musicVolumeSlider, musicSoundButton);
        updateSoundIcon(sfxVolumeSlider, sfxSoundButton);

        // Calculate the font size for the buttons (Game Controls, Save, Cancel)
        int buttonFontSize = Math.max(14, minPanelSize / 35);
        gameControlsButton.setFont(new Font("Arial", Font.BOLD, buttonFontSize));
        cancelButton.setFont(new Font("Arial", Font.BOLD, buttonFontSize));
        saveButton.setFont(new Font("Arial", Font.BOLD, buttonFontSize));

        // Calculate the preferred size for the buttons
        int buttonWidth = Math.max(120, panelWidth / 4); // Min 120, 1/4 of the panel width
        int buttonHeight = Math.max(35, panelHeight / 12); // Min 35, 1/12 of the panel height

        Dimension buttonDim = new Dimension(buttonWidth, buttonHeight);
        gameControlsButton.setPreferredSize(new Dimension(buttonWidth*2,buttonHeight));
        cancelButton.setPreferredSize(buttonDim);
        saveButton.setPreferredSize(buttonDim);

        // Update the UI to reflect the changes
        revalidate();
        repaint();
    }

    private void setupLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding


        // Place gameControlsButton below it, centered (Row 1)
        gbc.gridx = 0; // Start from the first column
        gbc.gridy = 1; // Next line
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Spans all remaining columns
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE; // Don't fill the space
        gbc.weightx = 0; // Don't take up extra horizontal space
        gbc.weighty = 0; // Don't take up extra vertical space
        add(gameControlsButton, gbc);

        // Separator/spacer for better visual appearance
        gbc.gridy++;
        gbc.weighty = 0.1; // Push the other components down
        add(Box.createVerticalGlue(), gbc); // Flexible space

        // Music Controls (Label, Icon, Slider)
        gbc.gridwidth = 1; // Reset grid width to 1 column
        gbc.weighty = 0; // Reset the vertical weight
        gbc.anchor = GridBagConstraints.WEST; // Left anchor for labels

        musicLabel = new JLabel("Music");
        musicLabel.setForeground(Color.BLACK);
        // The font size will be set by applyScalingBasedOnCurrentDimensions()
        gbc.gridx = 0;
        gbc.gridy++;
        add(musicLabel, gbc);

        musicSoundButton = new JButton();
        musicSoundButton.setOpaque(false);
        musicSoundButton.setContentAreaFilled(false); // Hide the button's fill area
        musicSoundButton.setBorderPainted(false);
        musicSoundButton.setFocusPainted(false);

        gbc.gridx = 1;
        add(musicSoundButton, gbc);

        musicVolumeSlider = new JSlider(0, 100, 25); // Min, Max, initial value
        musicVolumeSlider.setOpaque(false);
        musicVolumeSlider.setMajorTickSpacing(25); // Increased tick spacing
        musicVolumeSlider.setPaintTicks(true); // Draw ticks
        musicVolumeSlider.setPaintLabels(true); // Draw tick labels
        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Fills the horizontal space
        gbc.weightx = 1.0; // The slider takes up the available horizontal space
        add(musicVolumeSlider, gbc);

        // Sound Effects Controls (Label, Icon, Slider)
        sfxLabel = new JLabel("Sound Effects");
        sfxLabel.setForeground(Color.BLACK);
        // The font size will be set by applyScalingBasedOnCurrentDimensions()
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE; // Reset the padding
        gbc.weightx = 0;
        add(sfxLabel, gbc);

        sfxSoundButton = new JButton();
        sfxSoundButton.setOpaque(false);
        sfxSoundButton.setContentAreaFilled(false);
        sfxSoundButton.setBorderPainted(false);
        sfxSoundButton.setFocusPainted(false);

        gbc.gridx = 1;
        add(sfxSoundButton, gbc);

        sfxVolumeSlider = new JSlider(0, 100, 50); // Min, Max, initial value
        sfxVolumeSlider.setOpaque(false);
        sfxVolumeSlider.setMajorTickSpacing(25);
        sfxVolumeSlider.setPaintTicks(true);
        sfxVolumeSlider.setPaintLabels(true);
        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        add(sfxVolumeSlider, gbc);

        // Separator/spacer before action buttons
        gbc.gridy++;
        gbc.weighty = 0.1;
        add(Box.createVerticalGlue(), gbc);

        //"Cancel" and "Save" buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0)); // FlowLayout for the buttons
        buttonPanel.setOpaque(false);

        cancelButton = new InternalSettingsButton("Cancel");
        saveButton = new InternalSettingsButton("Save");
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Spanning across all columns
        gbc.anchor = GridBagConstraints.CENTER; // Center the button panel
        gbc.fill = GridBagConstraints.NONE; // Don't fill the space
        gbc.weightx = 0; // No extra horizontal space
        gbc.weighty = 0.1; // Allows some space at the bottom
        add(buttonPanel, gbc);
    }

    public void showLevelDialog(GameControlsPanel gameControlsPanel) {
        // Retrieve the top-level window (e.g., JFrame) that contains this panel
        Window parentWindow = SwingUtilities.getWindowAncestor(this);

        // Get the dimensions of the parent window to calculate proportional dialog size
        Dimension parentSize = parentWindow.getSize();
        int newWidth = parentSize.width / 2;
        int newHeight = parentSize.height / 2;

        // Set the preferred size of the level panel to be displayed in the dialog
        gameControlsPanel.setPreferredSize(new Dimension(newWidth, newHeight));

        // Create a modal dialog (blocks interaction with other windows while open)
        JDialog dialog = new JDialog(parentWindow);
        dialog.setUndecorated(true);  // Remove window borders and title bar
        dialog.setModal(true);        // Make dialog modal
        dialog.setResizable(false);   // Disable resizing by the user

        // Add the level panel to the dialog and adjust dialog size
        dialog.getContentPane().add(gameControlsPanel);
        dialog.pack(); // Automatically size dialog to fit its contents

        // Attempt to apply rounded corners to the dialog (if the platform supports it)
        try {
            dialog.setShape(new RoundRectangle2D.Double(0, 0, dialog.getWidth(), dialog.getHeight(), 30, 30));
        } catch (UnsupportedOperationException ex) {
            System.out.println("Rounded corners not supported on this platform");
        }

        // Center the dialog relative to the parent window
        dialog.setLocationRelativeTo(parentWindow);

        // Display the dialog
        dialog.setVisible(true);
    }

    private void addListeners() {
        gameControlsButton.addActionListener(e -> {
            GameControlsPanel gameControlsPanel =new GameControlsPanel(this.getPreferredSize());
            showLevelDialog(gameControlsPanel);
        });

        musicVolumeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // Update SettingsManager
                SettingsManager.getInstance().setMusicVolume(musicVolumeSlider.getValue());
            }
        });


        sfxVolumeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // Update SettingsManager
                SettingsManager.getInstance().setSfxVolume(sfxVolumeSlider.getValue());
            }
        });

        cancelButton.addActionListener(e -> {
            System.out.println("Settings cancelled.");
            // Revert current settings in SettingsManager to the last saved state
            SettingsManager.getInstance().revertToSavedSettings();

        });

        // Listener per il bottone "Save"
        saveButton.addActionListener(e -> {
            System.out.println("Settings saved.");
            // Save the current settings as the new permanent settings
            SettingsManager.getInstance().saveCurrentSettings();

        });
    }

    // Method to update the UI based on the values of the SettingsManager
    private void updateUIFromSettings() {
        musicVolumeSlider.setValue(SettingsManager.getInstance().getMusicVolume());
        sfxVolumeSlider.setValue(SettingsManager.getInstance().getSfxVolume());
        // Update the audio icons based on the values just set on the sliders
        updateSoundIcon(musicVolumeSlider, musicSoundButton);
        updateSoundIcon(sfxVolumeSlider, sfxSoundButton);
    }

    // Update the audio button icon based on the slider value
    private void updateSoundIcon(JSlider slider, JButton iconButton) {
        if (soundIcon == null || noSoundIcon == null) {
            return;
        }
        if (slider.getValue() == 0) {
            iconButton.setIcon(noSoundIcon);
        } else {
            iconButton.setIcon(soundIcon);
        }
    }

// ----------------------------------------OVERRIDE METHODS-------------------------------------------------------------

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, width, height, this);
        } else {
            g2.setColor(Color.GRAY.brighter());
            g2.fillRect(0, 0, width, height);
        }


        g2.dispose();
        super.paintComponent(g);
    }

    // Implementing the propertyChange method from the PropertyChangeListener interface
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // This method is called whenever a property in the SettingsManager changes
        if ("musicVolume".equals(evt.getPropertyName())) {
            int newVolume = (int) evt.getNewValue();
            // Update the music volume slider, but only if the change was not initiated by this slider itself
            if (musicVolumeSlider.getValue() != newVolume) {
                musicVolumeSlider.setValue(newVolume);
            }
            // Update the audio button icon based on the new slider value
            updateSoundIcon(musicVolumeSlider, musicSoundButton);
        } else if ("sfxVolume".equals(evt.getPropertyName())) {
            int newVolume = (int) evt.getNewValue();
            if (sfxVolumeSlider.getValue() != newVolume) {
                sfxVolumeSlider.setValue(newVolume);
            }
            updateSoundIcon(sfxVolumeSlider, sfxSoundButton);
        }
    }

    //Removes the listener when the panel is removed from the component hierarchy
    // Prevents memory leak if the panel is created and destroyed frequently.
    @Override
    public void removeNotify() {
        super.removeNotify();
        SettingsManager.getInstance().removePropertyChangeListener(this);
    }

}