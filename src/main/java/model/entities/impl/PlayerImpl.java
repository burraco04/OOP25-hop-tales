package model.entities.impl;

import model.entities.api.Player;
import model.entities.api.PlayerSnapshot;

/**
 * Default physics-backed implementation of the controllable player.
 */
public final class PlayerImpl implements Player {
    private static final int MIN_DELTA_SECONDS = 1;
    private static final int STARTING_HEALTH = 3;
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

    /**
     * Builds a new player instance bound to the given level geometry.
     *
     * @param startX      initial horizontal position
     * @param startY      initial vertical position
     * @param width       player width
     * @param height      player height
     * @param worldWidth  world width
     * @param worldHeight world height
     * @param platforms   level platforms used for collision checks
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
        this.healthPoints = STARTING_HEALTH;
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
        return (healthPoints > 0) ? true : false;
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
    public void setX(final int x) {
        this.x = x;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setY(final int y) {
        this.y = y;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFloating() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isFloating'");
    }
}
