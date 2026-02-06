package controller.impl;

import controller.api.ControllerObserver;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import model.World;
import model.entities.api.Player;
import model.objects.CollectableManager;

/**
 * Controller responsible for the {@link CollectableManager}.
 */
@SuppressFBWarnings(value = "EI2", justification = "World is shared across controllers by design.")
public final class CollectablesController implements ControllerObserver {
    private final World world;

    /**
     * Constructor for the class.
     *
     * @param world the world instance.
     */
    public CollectablesController(final World world) {
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
