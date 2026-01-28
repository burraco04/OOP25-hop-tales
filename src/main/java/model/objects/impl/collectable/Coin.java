package model.objects.impl.collectable;

import model.objects.api.WorldObject;

/**
 * Class defining coins instances.
 */
public class Coin implements WorldObject {
    private final int x;
    private final int y;

    /**
     * Constructor for the coin.
     *
     * @param x horizontal position.
     * 
     * @param y vertical position.
     */
    public Coin(final int x, final int y) {
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
        return "coin";
    }

}
