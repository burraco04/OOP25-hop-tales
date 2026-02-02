package model.objects.impl.door;

import model.objects.api.WorldObject;

/**
 * Represents the top of a door inside the game world.
 */
public class DoorTop implements WorldObject {

    private final int x;
    private final int y;

    /**
     * Creates the top of a door located at the specified coordinates.
     *
     * @param x x the horizontal position of the brick
     * @param y y the vertical position of the brick
     */
    public DoorTop(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getX() {
       return x;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getY() {
        return y;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType() {
      return "door_top";
    } 
}
