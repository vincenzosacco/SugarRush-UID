package utils.audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class AudioManager {

    private Clip backgroundMusicClip;
    private Clip sfxClip;
    private String currentBackgroundMusicPath; // To keep track of the currently playing music

    public AudioManager() {
    }

    // Loads an audio clip from the given path inside the resources
    public Clip loadAudioClip(String path) {
        try {
            URL audioUrl = getClass().getResource(path);
            if (audioUrl == null) {
                System.err.println("Audio resource not found: " + path);
                return null;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioUrl);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            return clip;

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error loading or opening audio clip from " + path + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Plays a given Clip. If loop is true, the clip loops continuously.
    public void playClip(Clip clip, boolean loop) {
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0); // Rewind
            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                clip.start();
            }
        }
    }

    // Stops a clip if it is currently playing
    public void stopClip(Clip clip) {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    // Sets the volume of a clip (0 to 100). Uses decibel scaling for realistic audio control.
    public void setVolume(Clip clip, float volume) {
        if (clip != null && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float minGain = gainControl.getMinimum();
            float maxGain = gainControl.getMaximum();
            if (volume <= 0.0f) {
                gainControl.setValue(minGain); // Mute
            } else {
                // Convert volume to decibels
                float dB = (float) (Math.log10(volume / 100.0f) * 20.0);
                gainControl.setValue(Math.max(minGain, Math.min(maxGain, dB)));
            }
        }
    }

    // Plays background music from the specified path with the given volume.
    public void playBackgroundMusic(String path, float volume, boolean forceRestart) {
        if (backgroundMusicClip == null || !path.equals(currentBackgroundMusicPath) || forceRestart) {
            stopBackgroundMusic(); // Stop existing music
            backgroundMusicClip = loadAudioClip(path);
            currentBackgroundMusicPath = path;
        }

        if (backgroundMusicClip != null) {
            setVolume(backgroundMusicClip, volume);
            if (!backgroundMusicClip.isRunning()) {
                playClip(backgroundMusicClip, true); // Loop background music
            }
        }
    }

    // Stops and releases the current background music
    public void stopBackgroundMusic() {
        if (backgroundMusicClip != null) {
            stopClip(backgroundMusicClip);
            backgroundMusicClip.close();
            backgroundMusicClip = null;
            currentBackgroundMusicPath = null;
        }
    }

    // Adjusts the volume of the background music while it's playing
    public void setBackgroundMusicVolume(float volume) {
        setVolume(backgroundMusicClip, volume);
    }

    // Plays a sound effect (non-looping), and closes the clip after it's done playing
    public void playSfx(String path, float volume) {
        Clip tempSfxClip = loadAudioClip(path);
        if (tempSfxClip != null) {
            setVolume(tempSfxClip, volume);
            playClip(tempSfxClip, false);
            // Automatically release resources when the sound finishes
            tempSfxClip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    event.getLine().close();
                }
            });
        }
    }

    // Returns true if the background music at the given path is currently playing
    public boolean isPlaying(String path) {
        return backgroundMusicClip != null && backgroundMusicClip.isRunning() && path.equals(currentBackgroundMusicPath);
    }

//    // Stops and cleans up all audio resources
//    public void cleanup() {
//        stopBackgroundMusic();
//    }
}
