package model.objects.api;

/**
 * Interface defining only objects that can be touched by the player and in some cases by enemies.
 */
public interface WorldObject {

    /**
     * return x.
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
     * return type.
     *
     * @return return type
     */
    String getType();
}
