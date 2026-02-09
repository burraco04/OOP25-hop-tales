package controller;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.Clip;

/**
 * Basic smoke tests for AudioManager.
 */
class AudioManagerTest {

    @Test
    void testLoadAndPlayDoesNotThrow() {
        assertDoesNotThrow(() -> AudioManager.load("test_sound", "/sounds/CoinSound.wav"));
        assertDoesNotThrow(() -> AudioManager.play("test_sound"));
    }

    @Test
    void testSetVolumeIfClipAvailable() {
        AudioManager.load("test_sound_volume", "/sounds/CoinSound.wav");
        final Clip clip = AudioManager.getClip("test_sound_volume");
        if (clip != null) {
            assertDoesNotThrow(() -> AudioManager.setVolume(clip, 0.5f));
        }
    }
}
