package model.objects.impl.lava;

import model.objects.api.WorldObject;

public class WaterTop implements WorldObject{


    private final int x;
    private final int y;

    /**
     * create object brick.
     *
     * @param x parameter
     * @param y parameter
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

    @Override
    public String getType() {
        return "water_top";
    }
}
