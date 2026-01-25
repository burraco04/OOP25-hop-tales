package model.objects.api;

import java.awt.Graphics;

/**
 * Interface defining only objects that can be touched by the player and in some cases by enemies.
 */
public interface Tangible {

    /**
     * retunr x.
     *
     * @return return x
     */
    int getX();

    /**
     * return y.
     *
     * @return return y
     */
    int getY();

    /**
     * @param x player's horizontal position
     * @param y player's vertical position
     * 
     * @return whether the object got touched or not.
     */
    boolean isTouched(int x, int y);

    /**
     * paint the entities in to the pannel.
     *
     * @param g graphics
     * @param camX camera
     */
    void draw(Graphics g, int camX);
}
