package model.entities.api;

import model.entities.commons.Entity;

/**
 * Abstraction over the controllable player entity.
 */
public interface Player extends Entity {

    /**
     * Get the player's current health. 
     *
     * @return the {@link Player} current health points. 
     */
    int getHealthPoints();

    /**
     * Tells if the player has a power-up.
     *
     * @return whether the {@link Player} currently owns the power-up.
     */
    boolean hasPowerUp();

    /**
     * Set whether the player currently owns a power-up.
     *
     * @param poweredUp {@code true} if the player has a power-up
     */
    void setPowerUp(boolean poweredUp);

    /**
     * Apply damage to the player if the cooldown allows it.
     *
     * @return {@code true} if damage was applied.
     */
    boolean applyDamage();

    /**
     * @return whether the Player got hurt recently.
     */
    boolean isHurt();
}
