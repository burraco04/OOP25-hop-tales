package controller.impl;

import controller.api.ControllerObserver;
import model.entities.api.Player;
import model.objects.CoinManager;

/**
 * Controller responsible for the {@link CoinManager}.
 */
public final class CoinsController implements ControllerObserver {
    private final CoinManager coinManager;
    private final Player player;
    
    /**
     * Constructor for the class.
     * 
     * @param player the player instance.
     */
    public CoinsController(final Player player) {
        coinManager = new CoinManager();
        this.player = player;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        coinManager.checkPossibleCollection((int) player.getX(), (int) player.getY());
    }

}
