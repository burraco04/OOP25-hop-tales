package model.objects.impl.brick;

import model.objects.api.WorldObject;

public class Plank implements  WorldObject{

    private final int x;
    private final int y;

    /**
     * create object brick.
     *
     * @param x parameter
     * @param y parameter
     */
    public Plank(final int x, final int y) {
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
        return "block_planks";
    }

}
