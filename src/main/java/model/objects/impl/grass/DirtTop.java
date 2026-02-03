package model.objects.impl.grass;

import model.objects.api.WorldObject;

/**
 * Represents a top_dirt_block object inside the game world.
 */
public class DirtTop implements WorldObject{

    private final int x;
    private final int y;

    /**
     * Create object top_dirt_block.
     *
     * @param x horizzontal position of the block
     * @param y vertical position of the block
     */
    public DirtTop(final int x, final int y) {
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
        return "top_dirt_block";
    }

}
