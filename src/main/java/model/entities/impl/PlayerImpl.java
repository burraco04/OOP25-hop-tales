package model.entities.impl;

import model.GameConstants;
import model.entities.api.Player;
import model.entities.api.PlayerSnapshot;

/**
 * Default physics-backed implementation of the controllable player.
 */
public final class PlayerImpl implements Player {
    private static final int MIN_DELTA_SECONDS = 1;
    private final double width;
    private final double height;
    private double x;
    private double y;
    private boolean onGround;
    private boolean facingRight = true;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean jumpQueued;
    private int healthPoints;
    private boolean powerUpped;
    private long lastDamageMillis;

    /**
     * Builds a new player instance bound to the given level geometry.
     *
     * @param startX      initial horizontal position
     * @param startY      initial vertical position
     * @param width       player width
     * @param height      player height
     */

    public PlayerImpl(
        final double startX,
        final double startY,
        final double width,
        final double height
    ) {
        this.x = startX;
        this.y = startY;
        this.width = width;
        this.height = height;
        this.healthPoints = GameConstants.STARTING_HEALTH;
        this.powerUpped = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLeftPressed(final boolean pressed) {
        this.leftPressed = pressed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRightPressed(final boolean pressed) {
        this.rightPressed = pressed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void queueJump() {
        this.jumpQueued = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final double deltaSeconds) {
        if (deltaSeconds <= MIN_DELTA_SECONDS) {
            return;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PlayerSnapshot snapshot() {
        return new PlayerSnapshot(x, y, width, height, facingRight, onGround);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getX() {
        return x;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getY() {
        return y;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getWidth() {
        return width;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getHeight() {
        return height;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAlive() {
        return healthPoints > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getHealthPoints() {
        return healthPoints;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasPowerUp() {
        return powerUpped;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean applyDamage() {
        final long now = System.currentTimeMillis();
        final long cooldownMillis = (long) (GameConstants.DAMAGE_COOLDOWN * 1000f);
        if (lastDamageMillis != 0 && now - lastDamageMillis < cooldownMillis) {
            return false;
        }
        if (healthPoints > 0) {
            healthPoints -= 1;
        }
        lastDamageMillis = now;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setX(final double x) {
        this.x = x;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setY(final double y) {
        this.y = y;
    }
}
