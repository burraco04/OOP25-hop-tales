package model.entities.commons;

/**
 * Abstraction defining every entity's behaviour. 
 */
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
     * Set the entity's horizontal position value. 
     * 
     * @param x the new horizontal value.
     */
    void setX(int x);

    /**
     * Set the entity's vertical position value.
     * 
     * @param y the new vertical value.
     */
    void setY(int y);

    /** 
     * @return whether the {@link Entity} is floating or not.
     */
    boolean isFloating();

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
