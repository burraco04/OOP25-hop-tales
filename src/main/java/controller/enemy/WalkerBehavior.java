package controller.enemy;

import model.entities.api.Enemy;

/**
 * Defines the behavior of walking enemy.
 */
public final class WalkerBehavior implements EnemyBehavior {
    private static final double SPEED = 100.0;

    @Override
    public void update(final Enemy enemy, final double deltaSeconds) {
        // Cammina avanti
        enemy.setX(enemy.getX() + SPEED * deltaSeconds);
        // Aggiungere stop quando incontra ostacolo
    }
}
