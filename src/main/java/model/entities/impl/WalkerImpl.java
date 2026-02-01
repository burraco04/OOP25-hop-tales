package model.entities.impl;

import model.GameConstants;
import model.entities.api.EnemyType;

/**
 * Implementation of a walking enemy.
 */
public final class WalkerImpl extends EnemyImpl {

    private static final double SPEED = 0.2;

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
     * Updates the enemy’s position.
     * Handles horizontal movement and gravity. 
     *
     * @param deltaSeconds time elapsed since last update
     */
    @Override
    public void update(final double deltaSeconds) {
        moveHorizontal(getDirection() * SPEED);
        applyGravity(GameConstants.GRAVITY);
    }
   
}
