package model.objects.impl.brick;

import model.objects.api.WorldObject;

/**
 * brick.
 */
public final class Brick implements WorldObject {

    private final int x;
    private final int y;

    /**
     * create object brick.
     *
     * @param x parameter
     * @param y parameter
     */
    public Brick(final int x, final int y) {
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

    @Override
    public String getType() {
        return "brick";
    }
}
