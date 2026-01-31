package model.objects.impl.grass;

import model.objects.api.WorldObject;

/**
 * green grass class.
 */
public final class GreenGrass implements WorldObject {

    private final int x;
    private final int y;

    /**
     * create costructor.
     *
     * @param x parameter x
     * @param y parameter y
     */
    public GreenGrass(final int x, final int y) {
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
        return "green_grass";
    }
}
