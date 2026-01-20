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
     * @return the {@link Player} current health points. 
     */
    int getHealthPoints();
    
    /**
     * 
     * @return whether the {@link Player} currently owns the power-up.
     */
    boolean hasPowerUp();

    /**
     * @return an immutable snapshot representing the current player state.
     */
    PlayerSnapshot snapshot();
}
