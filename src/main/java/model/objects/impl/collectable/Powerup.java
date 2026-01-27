package model.objects.impl.collectable;

import model.objects.api.WorldObject;

public class Powerup implements WorldObject{

    private final int x;
    private final int y;

    /**
     * create object powerup.
     *
     * @param x parameter
     * @param y parameter
     */
    public Powerup(final int x, final int y) {
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
        return "powerup";
    }

}
