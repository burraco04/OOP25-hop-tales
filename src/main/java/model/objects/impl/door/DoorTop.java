package model.objects.impl.door;

import model.objects.api.WorldObject;

public class DoorTop implements WorldObject {

    private final int x;
    private final int y;

    /**
     * class grass.
     *
     * @param x parameter
     * @param y parameter
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
