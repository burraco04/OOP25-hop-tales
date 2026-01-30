package model.objects.impl.collectable;

import model.objects.api.WorldObject;

/**
 * A simple Powerup object.
 */
public class Powerup implements WorldObject {

    private final int x;
    private final int y;

    /**
     * create object powerup.
     *
     * @param x parameter
     * @param y parameter
     */
    public Powerup(final int x, final int y) {
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
        return "powerup";
    }

}
