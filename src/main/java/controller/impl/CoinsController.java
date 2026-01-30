package controller.impl;

import controller.api.ControllerObserver;
import model.World;
import model.entities.api.Player;
import model.objects.CoinManager;

/**
 * Controller responsible for the {@link CoinManager}.
 */
public final class CoinsController implements ControllerObserver {
    private final World world;

    /**
     * Constructor for the class.
     *
     * @param world the world instance.
     */
    public CoinsController(final World world) {
        this.world = world;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        final Player player = world.getPlayer();
        world.getCoinManager().checkPossibleCollection((int) player.getX(), (int) player.getY());
    }

}
