package model.objects.impl.grass;

import model.objects.api.WorldObject;

/**
 * Represents a dirt_block object inside the game world.
 */
public class Dirt implements WorldObject{

    private final int x;
    private final int y;

    /**
     * Create object dirt_block.
     *
     * @param x horizzontal position of the block
     * @param y vertical position of the block
     */
    public Dirt(final int x, final int y) {
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
        return "dirt_block";
    }

}
