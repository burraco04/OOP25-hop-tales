package controller;

import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

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

    public static void setVolume(Clip clip, float volume) {
        if (!clip.isControlSupported(FloatControl.Type.MASTER_GAIN))
        return;

        FloatControl gain =
            (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        volume = Math.max(0.0001f, Math.min(1f, volume));

        float dB = (float) (20.0 * Math.log10(volume));
        gain.setValue(dB);
    }
    public static Clip getClip(String name) {
        return sounds.get(name);
    }
}
