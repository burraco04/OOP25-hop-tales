package model.entities.player.api;

public interface Enemy {
    /**
     * @return the enemy's left coordinate.
     */
    double getX();

    /**
     * @return the enemy's bottom coordinate.
     */
    double getY();

    /**
     * @return the enemy width.
     */
    double getWidth();

    /**
     * @return the enemy height.
     */
    double getHeight();

    /**
     * @return true if the enemy is alive.
     */
    boolean isAlive();

    /**
     * Updates enemy logic.
     *
     * @param deltaSeconds time in seconds since last update
     */
    void update(double deltaSeconds);

    /**
     * @return a snapshot of the enemy state.
     */
    EnemySnapshot getSnapshot();
    
}
