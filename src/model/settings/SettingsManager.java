package model.settings;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class SettingsManager {
    private static final SettingsManager instance = new SettingsManager();

    // "Current" settings - these are updated by the sliders in real-time
    private int currentMusicVolume;
    private int currentSfxVolume;

    // "Saved" settings - these are the last permanently saved values
    private int savedMusicVolume;
    private int savedSfxVolume;

    // Support for PropertyChangeListener, which notifies of property changes
    private PropertyChangeSupport support;

    //singleton class
    private SettingsManager() {
        support= new PropertyChangeSupport(this);
        // Default values for volumes
        this.savedMusicVolume = 50;
        this.savedSfxVolume = 70;

        // Initialize current settings with saved values
        this.currentMusicVolume = this.savedMusicVolume;
        this.currentSfxVolume = this.savedSfxVolume;
    }

    public static synchronized SettingsManager getInstance() {
        return instance;
    }

    // Getters and Setters
    public int getMusicVolume() {
        return currentMusicVolume;
    }

    public void setMusicVolume(int volume) {
        int oldVolume = this.currentMusicVolume;
        this.currentMusicVolume = volume;
        // Notify listeners that the "musicVolume" property has changed
        support.firePropertyChange("musicVolume", oldVolume, volume);

    }

    public int getSfxVolume() {
        return currentSfxVolume;
    }

    public void setSfxVolume(int volume) {
        int oldVolume = this.currentSfxVolume;
        this.currentSfxVolume = volume;
        // Notify listeners that the "sfxVolume" property has changed
        support.firePropertyChange("sfxVolume", oldVolume, volume);
    }

    /**
     * Saves the current settings as the new permanent saved settings.
     * This method should be called when the user explicitly saves their changes.
     */
    public void saveCurrentSettings() {
        this.savedMusicVolume = this.currentMusicVolume;
        this.savedSfxVolume = this.currentSfxVolume;
        System.out.println("SettingsManager: Settings saved - Music: " + savedMusicVolume + ", SFX: " + savedSfxVolume);
    }

    /**
     * Reverts the current settings to the last saved settings.
     * This method should be called when the user cancels their changes.
     */
    public void revertToSavedSettings() {
        int oldMusicVolume = this.currentMusicVolume;
        int oldSfxVolume = this.currentSfxVolume;

        this.currentMusicVolume = this.savedMusicVolume;
        this.currentSfxVolume = this.savedSfxVolume;
        System.out.println("SettingsManager: Settings reverted to saved - Music: " + savedMusicVolume + ", SFX: " + savedSfxVolume);

        // Fire property changes only if values have actually changed to notify listeners (UI)
        if (oldMusicVolume != currentMusicVolume) {
            support.firePropertyChange("musicVolume", oldMusicVolume, currentMusicVolume);
        }
        if (oldSfxVolume != currentSfxVolume) {
            support.firePropertyChange("sfxVolume", oldSfxVolume, currentSfxVolume);
        }
    }

    // Methods to add and remove PropertyChangeListener
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }
}
