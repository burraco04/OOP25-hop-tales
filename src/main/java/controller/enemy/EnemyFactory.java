package controller.enemy;

import controller.impl.EnemyController;
import model.entities.api.Enemy;
import model.entities.api.EnemyType;
import model.entities.impl.EnemyImpl;

public final class EnemyFactory {

    /**
     * Crea un nemico generico con comportamento in base al tipo.
     * @param x coordinata iniziale X
     * @param y coordinata iniziale Y
     * @param width larghezza
     * @param height altezza
     * @param type tipo di nemico
     * @return EnemyController pronto da aggiornare
     */
    public static EnemyController createEnemy(double x, double y, double width, double height, EnemyType type) {
        Enemy enemy = new EnemyImpl(x, y, width, height, type);
        EnemyBehavior behavior;

        switch (type) {
            case WALKER -> behavior = new WalkerBehavior();
            case JUMPER -> behavior = new JumperBehavior();
            default -> throw new IllegalArgumentException("Unknown enemy: " + type);
        }

        return new EnemyController(enemy, behavior);
    }
}
