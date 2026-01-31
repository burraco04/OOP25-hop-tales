package model.entities.impl;

import model.Collider;
import model.GameConstants;
import model.entities.api.EnemyType;

public class JumperImpl extends EnemyImpl {

    private static final double SPEED = 0.1;
    private static final double JUMP_HEIGHT = 3.0;
    private double jumpRemaining;
    private int direction = 1;
    private Collider collider;

    public JumperImpl(double x, double y, EnemyType type) {
        super(
            x, 
            y, 
            GameConstants.ENEMY_WITDH, 
            GameConstants.ENEMY_HEIGHT, 
            EnemyType.JUMPER
        );
    }

    @Override
    public void update(final double deltaSeconds) {
        double x = getX();
        double y = getY();
        final double targetX = x + direction * SPEED;
        if (canMoveTo(targetX, y)) {
            setX(targetX);
            x = targetX;
        } else {
            direction *= -1;
        }
        if (x < 0.0) {
            x = 0.0;
            setX(x);
            direction = 1;
        } else if (x > GameConstants.LEVEL_1_WIDTH - GameConstants.ENEMY_WITDH) {
            x = GameConstants.LEVEL_1_WIDTH - GameConstants.ENEMY_WITDH;
            setX(x);
            direction = -1;
        }

        if (jumpRemaining == 0 && isOnGround(x, y)) {
            jumpRemaining = JUMP_HEIGHT;
        }
        if (jumpRemaining > 0) {
            final double step = Math.min(SPEED, jumpRemaining);
            final double targetY = y - step;
            if (canMoveTo(x, targetY)) {
                setY(targetY);
                y = targetY;
                jumpRemaining -= step;
            } else {
                jumpRemaining = 0;
            }
        } else if (!isOnGround(x, y)) {
            final double targetY = y + GameConstants.GRAVITY;
            if (canMoveTo(x, targetY)) {
                setY(targetY);
            }
        }
    }

    public void setCollider(final Collider collider) {
        this.collider = collider;
    }

    private boolean canMoveTo(final double nextX, final double nextY) {
        if (collider == null) {
            return true;
        }
        final int tileX = (int) Math.floor(nextX);
        final int tileY = (int) Math.floor(nextY);
        return !collider.collidesWithSolid(
            tileX,
            tileY,
            GameConstants.ENEMY_WITDH,
            GameConstants.ENEMY_HEIGHT
        );
    }

    private boolean isOnGround(final double x, final double y) {
        if (collider == null) {
            return false;
        }
        final int tileX = (int) Math.floor(x);
        final int tileY = (int) Math.floor(y);
        return collider.collidesWithSolid(
            tileX,
            tileY + 1,
            GameConstants.ENEMY_WITDH,
            GameConstants.ENEMY_HEIGHT
        );
    }

}
