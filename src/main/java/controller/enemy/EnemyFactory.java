package controller.enemy;

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
     * Creates a generic enemy with a behavior based on its type.
     *
     * @param x initial X coordinate
     * @param y initial Y coordinate
     * @param width enemy width
     * @param height enemy height
     * @param type enemy type
     * @param manager EnemyManager that will manage this enemy
     * @return an Enemy instance registered in the EnemyManager
     */
    public static Enemy createEnemy(
            double x,
            double y,
            double width,
            double height,
            EnemyType type,
            EnemyManager manager) {

        Enemy enemy = new EnemyImpl(x, y, width, height, type);
        
        EnemyBehavior behavior;

        switch (type) {
            case WALKER -> behavior = new WalkerBehavior();
            case JUMPER -> behavior = new JumperBehavior();
            default -> throw new IllegalArgumentException("Unknown enemy: " + type);
        }

        manager.addEnemys(enemy, behavior);
        return enemy;

    }
}
