package model.objects.impl.grass;

import model.objects.api.WorldObject;

/**
 * Represents a green grass object inside the game world.
 */
public final class GreenGrass implements WorldObject {

    private final int x;
    private final int y;

    /**
     * Creates new green grass located at the specified coordinates.
     *
     * @param x x the horizontal position of the brick
     * @param y y the vertical position of the brick
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
