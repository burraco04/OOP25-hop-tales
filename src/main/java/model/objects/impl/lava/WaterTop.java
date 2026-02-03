package model.objects.impl.lava;

import model.objects.api.WorldObject;

/**
 * Represents a water_top object inside the game world.
 */
public class WaterTop implements WorldObject {

    private final int x;
    private final int y;

    /**
     * Create object water_top.
     *
     * @param x x parameter of water
     * @param y y parameter of water
     */
    public WaterTop(final int x, final int y) {
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
        return "water_top";
    }
}
