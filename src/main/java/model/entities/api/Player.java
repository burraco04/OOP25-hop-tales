package model.entities.api;

import model.entities.commons.Entity;

/**
 * Abstraction over the controllable player entity.
 */
public interface Player extends Entity {

    /**
     * Updates the pressed state of the left movement input.
     *
     * @param pressed {@code true} when the input is held down
     */
    void setLeftPressed(boolean pressed);

    /**
     * Updates the pressed state of the right movement input.
     *
     * @param pressed {@code true} when the input is held down
     */
    void setRightPressed(boolean pressed);

    /**
     * Signals that a jump should be attempted on the next update.
     */
    void queueJump();

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
