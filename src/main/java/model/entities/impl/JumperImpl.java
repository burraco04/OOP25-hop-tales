package model.entities.impl;

import model.GameConstants;
import model.entities.api.EnemyType;

/**
 * Implementation of a jumping enemy.
 */
public final class JumperImpl extends AbstractEnemyImpl {

    private static final double SPEED = 0.1;
    private static final double JUMP_HEIGHT = 2.0;
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
     * Returns the horizontal speed of the enemy jumper.
     *
     * @return horizontal speed
     */
    @Override
    protected double getSpeed() {
        return SPEED;
    }

    /**
     * Handles the jumping behavior of the Jumper.
     *
     * @param deltaSeconds time since the last update
     */
    @Override
    protected void jumpStep(final double deltaSeconds) {
        if (jumpRemaining == 0 && isOnGround(getX(), getY())) {
            jumpRemaining = JUMP_HEIGHT;
        }
        if (jumpRemaining > 0) {
            final double step = Math.min(SPEED, jumpRemaining);
            final double targetY = getY() - step;
            if (canMoveTo(getX(), targetY)) {
                setY(targetY);
                jumpRemaining -= step;
            } else {
                jumpRemaining = 0;
            }
        }
    }

    /**
     * Handles gravity for the Jumper.
     * Gravity is ignored while jumping
     *
     * @param deltaSeconds time since the last update
     */
    @Override
    protected void gravityStep(final double deltaSeconds) {
        if (jumpRemaining > 0) {
            return;
        }
        applyGravity(GameConstants.GRAVITY);
    }

}
