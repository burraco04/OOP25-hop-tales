package model.objects;

import java.util.ArrayList;
import java.util.List;

import model.GameConstants;
import model.objects.impl.Coin;

/**
 * Class used to manage the {@link Coin} system.
 */
public class CoinManager {    
    private static int collectedCoins;
    private final List<Coin> coins = new ArrayList<>();

    /**
     * Constructor that istantiate and register the coins depending by the level.
     */
    public CoinManager() {
        coins.add(new Coin(GameConstants.COIN_POSITION, GameConstants.COIN_POSITION));
    }

    /** 
     * @return the number of coins collected so far in the current level.
     */
    public int getCoins() {
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
        for (final Coin c : coins) {
            if (c.isTouched(x, y)) {
                collectCoin();
            }
        }
    }
}
