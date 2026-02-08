package model.entities.impl;

import controller.AudioManager;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import model.GameConstants;
import model.entities.api.Player;

/**
 * Default physics-backed implementation of the controllable player.
 */
public final class PlayerImpl implements Player {
    private static final int MIN_DELTA_SECONDS = 1;
    private final double width;
    private final double height;
    private double x;
    private double y;
    private double velocityX;
    private double velocityY;
    private int healthPoints;
    private boolean powerUpped;
    private long lastDamageMillis;
    private boolean onGround;

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
        this.velocityX = 0.0;
        this.velocityY = 0.0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("PMD.UnnecessaryReturn")
    @SuppressFBWarnings(value = "UC_USELESS_METHOD", justification = "Update is currently handled by controllers.")
    public void update(final double deltaSeconds) {
        if (deltaSeconds <= MIN_DELTA_SECONDS) {
            return;
        }
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
     * @return the current horizontal velocity.
     */
    public double getVelocityX() {
        return velocityX;
    }

    /**
     * @return the current vertical velocity.
     */
    public double getVelocityY() {
        return velocityY;
    }

    /**
     * Set the horizontal velocity.
     *
     * @param velocityX the new horizontal velocity.
     */
    public void setVelocityX(final double velocityX) {
        this.velocityX = velocityX;
    }

    /**
     * Set the vertical velocity.
     *
     * @param velocityY the new vertical velocity.
     */
    public void setVelocityY(final double velocityY) {
        this.velocityY = velocityY;
    }

    /**
     * Adds a delta to the vertical velocity.
     *
     * @param deltaY the delta to apply.
     */
    public void addVelocityY(final double deltaY) {
        this.velocityY += deltaY;
    }

    /**
     * @return true if the player is grounded.
     */
    public boolean isOnGround() {
        return onGround;
    }

    /**
     * Set the grounded state.
     *
     * @param onGround the grounded flag.
     */
    public void setOnGround(final boolean onGround) {
        this.onGround = onGround;
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
    public void setPowerUp(final boolean poweredUp) {
        this.powerUpped = poweredUp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean applyDamage() {
        final long now = System.currentTimeMillis();
        if (lastDamageMillis != 0 && isHurt()) {
            return false;
        }
        if (powerUpped) {
            powerUpped = false;
        } else if (healthPoints > 0) {
            healthPoints -= 1;
        }
        AudioManager.play("player_damaged");
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isHurt() {
        final long now = System.currentTimeMillis();
        final long cooldownMillis = (long) (GameConstants.DAMAGE_COOLDOWN * 1000f);
        return now - lastDamageMillis < cooldownMillis;
    }
}
