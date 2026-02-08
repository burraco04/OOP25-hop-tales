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
     * return the parametr y.
     *
     * @return the entity's bottom coordinate.
     */
    double getY();

    /**
     * Set the entity's horizontal position value. 
     *
     * @param x the new horizontal value.
     */
    void setX(double x);

    /**
     * Set the entity's vertical position value.
     *
     * @param y the new vertical value.
     */
    void setY(double y);

    /**
     * @return the entity width.
     */
    double getWidth();

    /**
     * @return the entity height.
     */
    double getHeight();

    /**
     * Updates entity logic.
     *
     * @param deltaSeconds time in seconds since last update
     */
    void update(double deltaSeconds);
}
