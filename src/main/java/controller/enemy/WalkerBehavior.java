package controller.enemy;

import model.entities.api.Enemy;

public class WalkerBehavior implements EnemyBehavior {
    private static final double SPEED = 100.0;

    @Override
    public void update(Enemy enemy, double deltaSeconds) {
        // Cammina avanti
        enemy.setX((int)(enemy.getX() + SPEED * deltaSeconds));
        // Aggiungere stop quando incontra ostacolo
    }
}