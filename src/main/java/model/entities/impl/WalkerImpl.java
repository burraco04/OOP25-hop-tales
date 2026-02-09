package model.entities.impl;

import model.GameConstants;
import model.entities.api.EnemyType;

/**
 * Implementation of a walking enemy.
 */
public final class WalkerImpl extends AbstractEnemyImpl {

    private static final double SPEED = 0.15;

    /**
     * Creates a new Walker enemy at the specified position.
     *
     * @param x starting horizontal coordinate
     * @param y starting vertical coordinate
     * @param type the enemy type
     */
    public WalkerImpl(final double x, final double y, final EnemyType type) {
        super(
            x, 
            y, 
            GameConstants.ENEMY_WITDH, 
            GameConstants.ENEMY_HEIGHT, 
            EnemyType.WALKER
        );
    }

    /**
     * Returns the horizontal speed of the enemy walker.
     *
     * @return horizontal speed
     */
    @Override
    protected double getSpeed() {
        return SPEED;
    }

}
