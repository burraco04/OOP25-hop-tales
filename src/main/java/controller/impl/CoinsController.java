package controller.impl;

import controller.api.ControllerObserver;
import model.entities.api.Player;
import model.objects.CoinManager;

/**
 * Controller responsible for the {@link CoinManager}.
 */
public class CoinsController implements ControllerObserver {
    private final CoinManager coinManager;
    private final Player player;
    
    public CoinsController(final Player player){
        coinManager = new CoinManager((int) player.getWidth(), (int) player.getHeight());
        this.player = player;
    }
    @Override
    public void update() {
        coinManager.checkPossibleCollection((int) player.getX(), (int) player.getY());
    }

}
