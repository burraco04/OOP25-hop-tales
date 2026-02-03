package model.objects.impl.lava;

import model.objects.api.WorldObject;

/**
 * Represents a lava object inside the game world.
 */
public class Lava implements WorldObject {

    private final int x;
    private final int y;

    /**
     * Create object lava.
     *
     * @param x x parameter of the lava
     * @param y y parameter of the lava
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
