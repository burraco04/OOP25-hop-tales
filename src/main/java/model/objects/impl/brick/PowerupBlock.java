package model.objects.impl.brick;

import model.objects.api.WorldObject;

/**
 * Represents a simple Powerup block.
 */
public class PowerupBlock implements WorldObject {
    private final int x;
    private final int y;

    /**
     * create Powerup block.
     *
     * @param x x parameter of the powerup_block
     * @param y y parameter of the powerup_block
     */
    public PowerupBlock(final int x, final int y) {
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
        return "powerup_block";
    }

}
