package controller;

import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Class that handles audio files.
 */
public class AudioManager {
    private static final Map<String, Clip> sounds = new HashMap<>();

    /**
     * Register a given file into a map of sounds usable in the game.
     * 
     * @param name name used to address the file in the map.
     * @param path path of the file.
     */
    public static void load(String name, String path) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(
                    AudioManager.class.getResource(path)
            );
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            sounds.put(name, clip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Play the selected song or audio.
     * 
     * @param name name of the audio in the map.
     */
    public static void play(String name) {
        Clip clip = sounds.get(name);
        if (clip == null) return;

        if (clip.isRunning())
            clip.stop();

        clip.setFramePosition(0);
        clip.start();
    }
}
