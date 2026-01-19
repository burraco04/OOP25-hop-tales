package model.entities.commons;

public interface Entity {
    /**
     * @return the entity's left coordinate.
     */
    double getX();

    /**
     * @return the entity's bottom coordinate.
     */
    double getY();

    /**
     * @return the entity width.
     */
    double getWidth();

    /**
     * @return the entity height.
     */
    double getHeight();

    /**
     * @return true if the entity is alive.
     */
    boolean isAlive();

    /**
     * Updates entity logic.
     *
     * @param deltaSeconds time in seconds since last update
     */
    void update(double deltaSeconds);
}
