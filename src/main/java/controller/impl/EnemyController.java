package controller.impl;

import java.util.HashSet;
import java.util.Set;

import controller.api.ControllerObserver;
import model.World;
import model.entities.api.Enemy;

/**
 * Manages enemies and their associated behaviors.
 */
public class EnemyController implements ControllerObserver {

    private static final double DELTA = 1.0 / 60.0;
    private final World world;

    public EnemyController(final World world) {
        this.world = world;
    }

    @Override
    public void update() {
        world.getEnemies().forEach((enemy) -> {
            if (enemy.isAlive()) {
                enemy.update(DELTA);
            }
        });
    }


}
