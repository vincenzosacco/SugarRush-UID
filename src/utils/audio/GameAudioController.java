package utils.audio; // O il package che usi per AudioManager

import model.settings.SettingsManager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GameAudioController implements PropertyChangeListener {
    private static GameAudioController instance;
    private final AudioManager audioManager;

    // Audio file paths
    private static final String MENU_MUSIC_PATH = "/audio/menuSound.wav";
    private static final String GAME_MUSIC_PATH = "/audio/gameSound.wav";
    private static final String WALL_SFX_PATH = "/audio/wallSound.wav"; // SFX Example
    private static final String DEATH_SFX_PATH = "/audio/loseSound.wav";
    private static final String WIN_SFX_PATH = "/audio/winSound.wav";
    private static final String BITE_SFX_PATH = "/audio/biteSound.wav";
    private static final String THORNS_SFX_PATH = "/audio/thornsSound.wav";
    private static final String KILL_BEE_SFX_PATH = "/audio/killBeeSound.wav";
    private static final String HIT_SHOT_PATH = "/audio/hitShotSound.wav";

    private GameAudioController() {
        audioManager = new AudioManager();
        SettingsManager.getInstance().addPropertyChangeListener(this);
    }

    public static synchronized GameAudioController getInstance() {
        if (instance == null) {
            instance = new GameAudioController();
        }
        return instance;
    }

    // Methods to control background music from the game
    public void playMenuMusic() {
        float volume = SettingsManager.getInstance().getMusicVolume();
        if (audioManager.isPlaying(MENU_MUSIC_PATH)) {
            return;
        }
        audioManager.playBackgroundMusic(MENU_MUSIC_PATH, volume, true);
    }

    public void playGameMusic() {
        float volume = SettingsManager.getInstance().getMusicVolume();
        if (audioManager.isPlaying(GAME_MUSIC_PATH)) {
            return;
        }
        audioManager.playBackgroundMusic(GAME_MUSIC_PATH, volume, true);
    }

    public void stopBackgroundMusic() {
        audioManager.stopBackgroundMusic();
    }

    // Methods to play SFX
    public void playSfx(String sfxIdentifier) {
        float volume = SettingsManager.getInstance().getSfxVolume();
        String path = "";
        switch (sfxIdentifier) {
            case "wall":
                path = WALL_SFX_PATH;
                break;
            case "death":
                path = DEATH_SFX_PATH;
                break;
            case "win":
                path = WIN_SFX_PATH;
                break;
            case "bite":
                path= BITE_SFX_PATH;
                break;
            case "thorns":
                path= THORNS_SFX_PATH;
                break;
            case "killBee":
                path= KILL_BEE_SFX_PATH;
                break;
            case "hitShot":
                path= HIT_SHOT_PATH;
                break;
            default:
                System.err.println("SFX non riconosciuto: " + sfxIdentifier);
                return;
        }
        audioManager.playSfx(path, volume);
    }

    // Implement PropertyChangeListener to react to volume changes
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("musicVolume".equals(evt.getPropertyName())) {
            float newVolume = (int) evt.getNewValue();
            audioManager.setBackgroundMusicVolume(newVolume);
        }
    }

}
