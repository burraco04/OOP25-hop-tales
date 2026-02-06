package model.objects;

import controller.AudioManager;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import model.CoinStorage;
import model.World;
import model.objects.impl.collectable.Coin;

/**
 * Class used to manage the {@link Coin} system.
 */
@SuppressFBWarnings(value = "EI2", justification = "World reference is shared across model managers.")
public class CollectableManager {
    @SuppressFBWarnings(value = "UWF_UNWRITTEN_FIELD", justification = "Legacy field kept for API stability.")
    private static int collectedCoins;
    private static final String COIN_SOUND = "coin_sound";
    private final World world;
    private boolean powerupCollected;

    /**
     * Constructor that istantiate the CoinManager and register the {@link World} depending by the level.
     *
     * @param world the current world.
     */
    public CollectableManager(final World world) {
        this.world = world;
        AudioManager.load(COIN_SOUND, "/sounds/CoinSound.wav");
        AudioManager.setVolume(AudioManager.getClip(COIN_SOUND), AudioManager.getMusicVolume());
    }

    /**
     * Get the number of coins collected.
     *
     * @return the number of coins collected.
     */
    public static int getCoins() {
        return collectedCoins;
    }

    /**
     * Collect a Powerup.
     */
    private void collectPowerup() {
        powerupCollected = true;
    }

    /**
     * Informs if the player has a Powerup.
     *
     * @return true if the player has a Powerup.
     */
    public boolean hasPowerUp() {
        return powerupCollected;
    }

    /**
     * Check if there is a coin that should be collected.
     *
     * @param x player's horizontal position
     * @param y player's vertical position
     */
    public void checkPossibleCollection(final int x, final int y) {
        if (world.collidesWithCoin(x, y)) {
            CoinStorage.collectCoin();
        }
        if (world.collidesWithPowerup(x, y)) {
            collectPowerup();
        }
    }
}
