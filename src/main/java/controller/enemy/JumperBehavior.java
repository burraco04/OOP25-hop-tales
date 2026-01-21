package controller.enemy;

import model.entities.api.Enemy;

public class JumperBehavior implements EnemyBehavior {
    private static final double SPEED = 50.0;
    private static final double JUMP_HEIGHT = 150.0;
    private boolean goingUp = true;

    @Override
    public void update(Enemy enemy, double deltaSeconds) {
        // Movimento orizzontale
        enemy.setX((int)(enemy.getX() + SPEED * deltaSeconds));

        // Movimento salto
        if (goingUp) {
            enemy.setY((int)(enemy.getY() + SPEED * deltaSeconds));
            if (enemy.getY() >= JUMP_HEIGHT) {
                goingUp = false;
            }
        } else {
            enemy.setY((int)(enemy.getY() - SPEED * deltaSeconds));
            if (enemy.getY() <= 0) {
                goingUp = true;
            }
        }
    }
}
