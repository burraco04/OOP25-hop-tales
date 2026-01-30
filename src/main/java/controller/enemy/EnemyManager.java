package controller.enemy;

import java.util.HashMap;
import java.util.Map;

import controller.api.ControllerObserver;
import model.entities.api.Enemy;

/**
 * Manages enemies and their associated behaviors.
 */
public class EnemyManager implements ControllerObserver {

    private static final double DELTA = 1.0 / 60.0;

    private final Map<Enemy, EnemyBehavior> enemies = new HashMap<>();

    public void addEnemy(Enemy enemy, EnemyBehavior enemyBehavior){
        enemies.put(enemy, enemyBehavior);
    }

    public void changeBeahvior(Enemy enemy, EnemyBehavior newBehavior){
        if(!enemies.containsKey(enemy)){
            throw new IllegalArgumentException("Enemy not managed");
        }
        enemies.put(enemy, newBehavior);
    }

    public void removeEnemy(Enemy enemy){
        enemies.remove(enemy);
    }

    @Override
    public void update() {
        enemies.forEach((enemy, behavior) -> {
            if (enemy.isAlive()) {
                behavior.update(enemy, DELTA);
            }
        });
    }


}
