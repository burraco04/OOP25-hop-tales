package model.entities.impl;

import model.GameConstants;
import model.entities.api.EnemyType;

/**
 * Implementation of a jumping enemy.
 */
public final class JumperImpl extends EnemyImpl {

    private static final double SPEED = 0.1;
    private static final double JUMP_HEIGHT = 3.0;
    private double jumpRemaining;

    /**
     * Creates a new Jumper enemy at the specified position.
     *
     * @param x starting horizontal coordinate
     * @param y starting vertical coordinate
     * @param type the enemy type
     */
    public JumperImpl(final double x, final double y, final EnemyType type) {
        super(
            x, 
            y, 
            GameConstants.ENEMY_WITDH, 
            GameConstants.ENEMY_HEIGHT, 
            EnemyType.JUMPER
        );
    }

    /**
     * Updates the enemy’s position.
     * Handles horizontal movement, jumping, and gravity. 
     *
     * @param deltaSeconds time elapsed since last update
     */
    @Override
    public void update(final double deltaSeconds) {
        moveHorizontal(getDirection() * SPEED);
    
        if (jumpRemaining == 0 && isOnGround(getX(), getY())) {
            jumpRemaining = JUMP_HEIGHT;
        }
        if (jumpRemaining > 0) {
            double step = Math.min(SPEED, jumpRemaining);
            double targetY = getY() - step;
            if (canMoveTo(getX(), targetY)) {
                setY(targetY);
                jumpRemaining -= step;
            } else {
                jumpRemaining = 0;
            }
        } else {
            applyGravity(GameConstants.GRAVITY);
        }
    }

}
