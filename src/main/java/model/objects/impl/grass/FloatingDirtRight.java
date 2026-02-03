package model.objects.impl.grass;

import model.objects.api.WorldObject;

/**
 * Represents a floating_dirt_right object inside the game world.
 */
public class FloatingDirtRight implements WorldObject {

    private final int x;
    private final int y;

    /**
     * Create object floating_dirt_right.
     *
     * @param x horizzontal position of the block
     * @param y vertical position of the block
     */
    public FloatingDirtRight(final int x, final int y) {
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
        return "floating_dirt_right";
    }

}
