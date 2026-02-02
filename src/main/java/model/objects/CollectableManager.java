package model.objects;

import controller.AudioManager;
import model.GameConstants;
import model.World;
import model.objects.impl.collectable.Coin;

/**
 * Class used to manage the {@link Coin} system.
 */
public class CollectableManager {
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
     * Collect a single coin, incrementing the total amount collected by the coin value.
     */
    private void collectCoin() {
        collectedCoins += GameConstants.COIN_VALUE;
        AudioManager.play(COIN_SOUND);
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
    public boolean hasPowerup() {
        return powerupCollected;
    }

    /**
     * Check if there is a coin that should be collected.
     *
     * @param x player's horizontal position
     *
     * @param y player's vertical position
     */
    public void checkPossibleCollection(final int x, final int y) {
        if (world.collidesWithCoin(x, y)) {
            collectCoin();
            System.out.println(collectedCoins);
        }
        if (world.collidesWithPowerup(x, y)) {
            collectPowerup();
        }
    }
}
