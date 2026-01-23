package controller.enemy;

import model.entities.api.Enemy;

public interface EnemyBehavior {
    /**
     * Updates the enemy logic according to its behavior.
     *
     * @param enemy the enemy to update
     * @param deltaSeconds time passed since last update
     */
    void update(Enemy enemy, double deltaSeconds);
}
