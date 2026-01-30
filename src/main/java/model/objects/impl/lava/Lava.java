package model.objects.impl.lava;

import model.objects.api.WorldObject;

/**
 * lava.
 */
public class Lava implements WorldObject {

    private final int x;
    private final int y;

    /**
     * create object brick.
     *
     * @param x parameter
     * @param y parameter
     */
    public Lava(final int x, final int y) {
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
        return "lava";
    }
    
}
