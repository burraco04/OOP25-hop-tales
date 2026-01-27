package model.objects.impl.brick;

import model.objects.api.WorldObject;

public class PowerupBlock implements WorldObject{
    private final int x;
    private final int y;

    /**
     * create Powerup block.
     *
     * @param x parameter
     * @param y parameter
     */
    public PowerupBlock(final int x, final int y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public String getType() {
        return "powerup_block";
    }

}
