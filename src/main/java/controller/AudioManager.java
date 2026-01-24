package controller;

import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioManager {
    private static final Map<String, Clip> sounds = new HashMap<>();

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

    public static void play(String name) {
        Clip clip = sounds.get(name);
        if (clip == null) return;

        if (clip.isRunning())
            clip.stop();

        clip.setFramePosition(0);
        clip.start();
    }
}
