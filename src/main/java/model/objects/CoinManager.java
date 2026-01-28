package model.objects;

import model.GameConstants;
import model.World;
import model.objects.impl.collectable.Coin;

/**
 * Class used to manage the {@link Coin} system.
 */
public class CoinManager {
    private static int collectedCoins;
    private final World world;

    /**
     * Constructor that istantiate the CoinManager and register the {@link World} depending by the level.
     * 
     * @param world the current world.
     */
    public CoinManager(final World world) {
        this.world = world;
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
    }

    /**
     * Check if there is a coin that should be collected.
     *
     * @param x player's horizontal position
     *
     * @param y player's vertical position
     */
    public void checkPossibleCollection(final int x, final int y) {
        if (world.collidesWithCollectable(x, y)) {
            collectCoin();
            System.out.println(collectedCoins);
        }
    }
}
