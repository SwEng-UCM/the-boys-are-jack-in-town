package main.controller;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

/**
 * The AudioManager class manages background music and sound effects in the game.
 * It follows the singleton pattern to ensure consistent audio control across the application.
 */
public class AudioManager {
    private static AudioManager instance;
    private Clip backgroundMusic;
    private float currentVolume = 0.1f;

    /**
     * Private constructor for the singleton pattern.
     */
    private AudioManager() {
    }

    /**
     * Returns the singleton instance of AudioManager.
     * If the instance does not exist, it initializes it.
     *
     * @return the singleton instance of AudioManager
     */
    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    /**
     * Plays background music in a continuous loop.
     * Loads the audio file from the resource path "/resources/videoplayback.wav".
     * If the music is already playing, this method does nothing.
     */
    public void playBackgroundMusic() {
        try {
            if (backgroundMusic != null && backgroundMusic.isActive()) {
                // Already playing, don't restart
                return;
            }
            // Load your background music file
            URL url = getClass().getResource("/resources/sounds/videoplayback.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioIn);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            setVolume(currentVolume); // Set initial volume
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the volume for background music.
     *
     * @param volume the volume level (range: 0.0 to 1.0)
     */
    public void setVolume(float volume) {
        currentVolume = volume;
        if (backgroundMusic != null && backgroundMusic.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
        }
    }

    /**
     * Retrieves the current background music volume.
     *
     * @return the current volume level (range: 0.0 to 1.0)
     */
    public float getVolume() {
        return currentVolume;
    }

    /**
     * Stops the background music playback.
     */
    public void stopMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
    }

    /**
     * Plays a one-time sound effect from the specified resource path.
     *
     * @param resourcePath the path to the sound effect resource (e.g., "/resources/sounds/effect.wav")
     */
    public void playSoundEffect(String resourcePath) {
        try {
            URL url = getClass().getResource(resourcePath);
            if (url == null) {
                System.err.println("Sound not found: " + resourcePath);
                return;
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start(); // One-time playback
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
