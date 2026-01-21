package controller.impl;

import controller.api.ControllerObserver;
import controller.enemy.EnemyBehavior;
import model.entities.api.Enemy;

public final class EnemyController implements ControllerObserver {
    private final Enemy enemy;
    private EnemyBehavior behavior;

    public EnemyController(Enemy enemy, EnemyBehavior behavior) {
        this.enemy = enemy;
        this.behavior = behavior;
    }

    /** Permette di cambiare comportamento dinamicamente */
    public void setBehavior(EnemyBehavior behavior) {
        this.behavior = behavior;
    }

    /** Questo viene chiamato dal GameController ad ogni tick */
    @Override
    public void update() {
        if (enemy.isAlive() && behavior != null) {
            // Divide per il TARGET_UPS
            behavior.update(enemy, 1.0 / 60); 
        }
    }

    public Enemy getEnemy() {
        return enemy;
    }
}
