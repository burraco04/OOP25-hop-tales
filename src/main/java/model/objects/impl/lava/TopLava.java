package model.objects.impl.lava;

import model.objects.api.WorldObject;

/**
 * Represents a top_lava object inside the game world.
 */
public class TopLava implements WorldObject {
    private final int x;
    private final int y;

    /**
     * create object brick.
     *
     * @param x x parameter of the lava
     * @param y y parameter of the lava
     */
    public TopLava(final int x, final int y) {
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
        return "top_lava";
    }
}
