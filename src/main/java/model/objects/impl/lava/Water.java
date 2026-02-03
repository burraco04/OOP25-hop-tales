package model.objects.impl.lava;

import model.objects.api.WorldObject;

/**
 * Represents a water object inside the game world.
 */
public class Water implements WorldObject {

    private final int x;
    private final int y;

    /**
     * Create object water.
     *
     * @param x x parameter of water
     * @param y y parameter of water
     */
    public Water(final int x, final int y) {
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
        return "water";
    }

}
