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
     * @param x x parameter of the powerup
     * @param y y parameter of the powerup
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
