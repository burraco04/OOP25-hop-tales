package model.objects.impl.grass;

import model.objects.api.Tangible;

/**
 * grass class.
 */
public final class Grass implements Tangible {

    private final int x;
    private final int y;

     /**
      * class grass.
      *
      * @param x parameter
      * @param y parameter
      */
     public Grass(final int x, final int y) {
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
    public boolean isTouched(final int x, final int y) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isTouched'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType() {
        return "grass";
    }

}
