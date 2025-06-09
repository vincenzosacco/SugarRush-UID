package utils.audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class AudioManager {

    private Clip backgroundMusicClip;
    private Clip sfxClip;
    private String currentBackgroundMusicPath; // To keep track of the current music

    public AudioManager() {
        // Initialization
    }

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

    public void playClip(Clip clip, boolean loop) {
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                clip.start();
            }
        }
    }

    public void stopClip(Clip clip) {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void setVolume(Clip clip, float volume) {
        if (clip != null && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float minGain = gainControl.getMinimum();
            float maxGain = gainControl.getMaximum();
            // Convert volume from 0-100 to decibels for gain control
            float actualVolume = volume;
            if (actualVolume <= 0.0f) {
                gainControl.setValue(gainControl.getMinimum()); // Mute
            } else {
                // Logarithmic scale from 0.01 to 1.0 to avoid log(0)
                float dB = (float) (Math.log10(actualVolume / 100.0f) * 20.0);
                if (dB < minGain) dB = minGain; // Clamp to minimum control
                if (dB > maxGain) dB = maxGain; // Clamp at maximum control
                gainControl.setValue(dB);
            }
        }
    }

    public void playBackgroundMusic(String path, float volume, boolean forceRestart) {
        if (backgroundMusicClip == null || !path.equals(currentBackgroundMusicPath) || forceRestart) {
            stopBackgroundMusic(); // Stop previous music if different or if forced restart
            backgroundMusicClip = loadAudioClip(path);
            currentBackgroundMusicPath = path;
        }

        if (backgroundMusicClip != null) {
            setVolume(backgroundMusicClip, volume);
            if (!backgroundMusicClip.isRunning()) { // Start only if not already playing
                playClip(backgroundMusicClip, true);
            }
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundMusicClip != null) {
            stopClip(backgroundMusicClip);
            backgroundMusicClip.close();
            backgroundMusicClip = null;
            currentBackgroundMusicPath = null;
        }
    }

    public void setBackgroundMusicVolume(float volume) {
        setVolume(backgroundMusicClip, volume);
    }

    public void playSfx(String path, float volume) {
        // For SFX, it's best to create a new clip each time if they are short and may overlap,
        Clip tempSfxClip = loadAudioClip(path);
        if (tempSfxClip != null) {
            setVolume(tempSfxClip, volume);
            playClip(tempSfxClip, false);
            // Listener to close the clip once it has finished playing
            tempSfxClip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    event.getLine().close();
                }
            });
        }
    }

    public boolean isPlaying(String path) {
        return backgroundMusicClip != null && backgroundMusicClip.isRunning() && path.equals(currentBackgroundMusicPath);
    }

    public void cleanup() {
        stopBackgroundMusic();
    }
}
