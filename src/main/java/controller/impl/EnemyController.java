package controller.impl;

import controller.api.ControllerObserver;
import model.GameConstants;
import model.World;

/**
 * Manages enemies and their associated behaviors.
 */
public class EnemyController implements ControllerObserver {

    private static final double DELTA = 1.0 / GameConstants.TARGET_FPS;
    private final World world;

    /**
     * Constructor for the class.
     *
     * @param world instance of the world.
     */
    public EnemyController(final World world) {
        this.world = world;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        world.getEnemies().forEach(enemy -> {
            if (enemy.isAlive()) {
                enemy.update(DELTA);
            }
        });
    }

}
