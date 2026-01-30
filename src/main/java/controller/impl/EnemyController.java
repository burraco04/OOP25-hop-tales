package controller.impl;

import java.util.HashSet;
import java.util.Set;

import controller.api.ControllerObserver;
import model.entities.api.Enemy;

/**
 * Manages enemies and their associated behaviors.
 */
public class EnemyController implements ControllerObserver {

    private static final double DELTA = 1.0 / 60.0;

    private final Set<Enemy> enemies = new HashSet<>();

    public void addEnemy(Enemy enemy){
        enemies.add(enemy);
    }

    public void removeEnemy(Enemy enemy){
        enemies.remove(enemy);
    }

    @Override
    public void update() {
        enemies.forEach((enemy) -> {
            if (enemy.isAlive()) {
                enemy.update(DELTA);
            }
        });
    }


}
