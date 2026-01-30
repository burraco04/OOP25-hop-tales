package controller.enemy;

import controller.impl.EnemyController;
import model.entities.api.Enemy;
import model.entities.api.EnemyType;
import model.entities.impl.JumperImpl;
import model.entities.impl.WalkerImpl;

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
     * @param controller EnemyControler that will manage this enemy
     * @return an Enemy instance registered in the EnemyController
     */
    public static Enemy createEnemy(
            double x,
            double y,
            double width,
            double height,
            EnemyType type,
            EnemyController controller) {

        Enemy enemy;

        switch (type) {
            case WALKER -> enemy = new WalkerImpl(x, y, width, height, type);
            case JUMPER -> enemy = new JumperImpl(x, y, width, height, type);
            default -> throw new IllegalArgumentException("Unknown enemy: " + type);
        }

        controller.addEnemy(enemy);
        return enemy;

    }
}
