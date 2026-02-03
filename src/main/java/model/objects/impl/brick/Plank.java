package model.objects.impl.brick;

import model.objects.api.WorldObject;

/**
 * Represents a block_planks object inside the game world.
 */
public class Plank implements WorldObject {

    private final int x;
    private final int y;

    /**
     * Create object block_planks.
     *
     * @param x horizzontal position of the block
     * @param y vertical position of the block
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType() {
        return "block_planks";
    }

}
