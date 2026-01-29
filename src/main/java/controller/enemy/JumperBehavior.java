package controller.enemy;

import model.entities.api.Enemy;

/**
 * Defines the behavior of jumping enemy.
 */
public final class JumperBehavior implements EnemyBehavior {
    private static final double SPEED = 50.0;
    private static final double JUMP_HEIGHT = 150.0;
    private boolean goingUp = true;

    @Override
    public void update(final Enemy enemy, final double deltaSeconds) {
        // Movimento orizzontale
        enemy.setX(enemy.getX() + SPEED * deltaSeconds);

        // Movimento salto
        if (goingUp) {
            enemy.setY(enemy.getY() + SPEED * deltaSeconds);
            if (enemy.getY() >= JUMP_HEIGHT) {
                goingUp = false;
            }
        } else {
            enemy.setY(enemy.getY() - SPEED * deltaSeconds);
            if (enemy.getY() <= 0) {
                goingUp = true;
            }
        }
    }
}
