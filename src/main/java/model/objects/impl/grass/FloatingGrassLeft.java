package model.objects.impl.grass;

import model.objects.api.WorldObject;

/**
 * floating grass.
 */
public class FloatingGrassLeft implements WorldObject {

    private final int x;
    private final int y;

     /**
      * class grass.
      *
      * @param x parameter
      * @param y parameter
      */
     public FloatingGrassLeft(final int x, final int y) {
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
       return "floating_grass_left";
    }

}
