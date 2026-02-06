package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import model.GameConstants;

/**
 * Class that handles audio files.
 */
public final class AudioManager {
    private static final Map<String, Clip> SOUNDS = new HashMap<>();
    private static final Logger LOGGER = Logger.getLogger(AudioManager.class.getName());
    private static float musicVolume;

    /**
     * Shall not be istantiated.
     */
    private AudioManager() { }

    /**
     * Register a given file into a map of sounds usable in the game.
     *
     * @param name name used to address the file in the map.
     * @param path path of the file.
     */
    public static void load(final String name, final String path) {
        try {
            final AudioInputStream ais = AudioSystem.getAudioInputStream(
                    AudioManager.class.getResource(path)
            );
            final Clip clip = AudioSystem.getClip();
            clip.open(ais);
            SOUNDS.put(name, clip);
        } catch (final UnsupportedAudioFileException e) {
            LOGGER.log(Level.WARNING, "Unsupported audio format: " + path, e);
        } catch (final IOException e) {
            LOGGER.log(Level.WARNING, "I/O error while loading audio: " + path, e);
        } catch (final LineUnavailableException e) {
            LOGGER.log(Level.WARNING, "Audio line unavailable for: " + path, e);
        }
    }

    /**
     * Play the selected song or audio.
     *
     * @param name name of the audio in the map.
     */
    public static void play(final String name) {
        final Clip clip = SOUNDS.get(name);
        if (clip == null) {
            return;
        }

        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.start();
    }

    /**
     * Set the wanted volume to a specific {@link Clip}.
     *
     * @param clip the chosen clip.
     * @param volume the desired volume.
     */
    public static void setVolume(final Clip clip, final float volume) {
        if (!clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            return;
        }

        final FloatControl gain =
            (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        final float newVolume = Math.max(GameConstants.MIN_VOLUME, Math.min(1f, volume));

        final float dB = (float) (GameConstants.DB_CONSTANT * Math.log10(newVolume));
        gain.setValue(dB);
    }

    /**
     * Return a specific {@link Clip}.
     *
     * @param name name of the desired {@link Clip}.
     * @return the desired {@link Clip}.
     */
    public static Clip getClip(final String name) {
        return SOUNDS.get(name);
    }

    /**
     * Set a standard volume for every clip.
     *
     * @param volume volume desired.
     */
    public static void setMusicVolume(final float volume) {
        musicVolume = volume;

        for (final Clip clip : SOUNDS.values()) {
            setVolume(clip, musicVolume);
        }
    }

    /**
     * Get the current standard volume.
     *
     * @return the standard volume.
     */
    public static float getMusicVolume() {
        return musicVolume;
    }

}
