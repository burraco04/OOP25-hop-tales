package model.objects.impl.grass;

import model.objects.api.Tangible;

/**
 * green grass class.
 */
public final class GreenGrass implements Tangible{

    private final int x;
    private final int y;

     public GreenGrass(final int x, final int y) {
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
    public boolean isTouched(int x, int y) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isTouched'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType() {
        return "green_grass";
    }
}
