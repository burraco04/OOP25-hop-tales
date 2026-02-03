package model.objects.impl.brick;

import model.objects.api.WorldObject;

/**
 * Represents a brick object inside the game world.
 */
public final class Brick implements WorldObject {

    private final int x;
    private final int y;

    /**
     * Creates a new brick located at the specified coordinates.
     *
     * @param x x the horizontal position of the brick
     * @param y y the vertical position of the brick
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType() {
        return "brick";
    }
}
