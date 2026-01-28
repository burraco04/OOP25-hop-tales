package controller.enemy;

import controller.impl.EnemyController;
import model.entities.api.Enemy;
import model.entities.api.EnemyType;
import model.entities.impl.EnemyImpl;

/**
 * Factory class for creating enemies with the appropriate behavior.
 */
public final class EnemyFactory {

    private EnemyFactory() {

    }

    /**
     * Crea un nemico generico con comportamento in base al tipo.
     * 
     * @param x coordinata iniziale X
     * @param y coordinata iniziale Y
     * @param width larghezza
     * @param height altezza
     * @param type tipo di nemico
     * @return EnemyController pronto da aggiornare
     */
    public static EnemyController createEnemy(final double x, final double y, final double width, final double height, final EnemyType type) {
        final Enemy enemy = new EnemyImpl(x, y, width, height, type);
        final EnemyBehavior behavior;

        switch (type) {
            case WALKER -> behavior = new WalkerBehavior();
            case JUMPER -> behavior = new JumperBehavior();
            default -> throw new IllegalArgumentException("Unknown enemy: " + type);
        }

        return new EnemyController(enemy, behavior);
    }
}
