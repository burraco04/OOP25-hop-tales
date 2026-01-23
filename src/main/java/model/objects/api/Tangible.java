package model.objects.api;

/**
 * Interface defining only objects that can be touched by the player and in some cases by enemies.
 */
public interface Tangible {

    /**
     * @param x player's horizontal position
     * @param y player's vertical position
     * 
     * @return whether the object got touched or not.
     */
    boolean isTouched(int x, int y);

}
