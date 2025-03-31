package main.controller;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class AudioManager {
    private static AudioManager instance;
    private Clip backgroundMusic;
    private float currentVolume = 0.1f; // Default volume (10%)
    
    private AudioManager() {
        // Private constructor for singleton
    }
    
    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }
    
    public void playBackgroundMusic() {
        try {
            if (backgroundMusic != null && backgroundMusic.isActive()) {
                // Already playing, don't restart
                return;
            }
            // Load your background music file
            URL url = getClass().getResource("/sounds/videoplayback.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioIn);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            setVolume(currentVolume); // Set initial volume
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    
    public void setVolume(float volume) {
        currentVolume = volume;
        if (backgroundMusic != null && backgroundMusic.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
        }
    }
    
    public void stopMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
    }
}
