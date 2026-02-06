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
        if (lastDamageMillis != 0 && isHurt()) {
            return false;
        }
        if (healthPoints > 0) {
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
