package model.entities.player.api;

/**
 * Abstraction over the controllable player entity.
 */
public interface Player {
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
     * Advances the player simulation.
     *
     * @param deltaSeconds elapsed time in seconds since the last update
     */
    void update(double deltaSeconds);

    /**
     * @return an immutable snapshot representing the current player state
     */
    PlayerSnapshot snapshot();
}
