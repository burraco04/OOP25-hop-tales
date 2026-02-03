package model.objects.impl.grass;

import model.objects.api.WorldObject;

/**
 * Represents a floating grass object inside the game world.
 */
public class FloatingGrass implements WorldObject {

    private final int x;
    private final int y;

    /**
     * Creates a new floating grass located at the specified coordinates.
     *
     * @param x x the horizontal position of the floating grass
     * @param y y the vertical position of the floating grass
     */
    public FloatingGrass(final int x, final int y) {
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
      return "floating_grass";
    }

}
