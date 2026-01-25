package model.objects.impl.brick;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

import model.objects.api.Tangible;
import view.utils.Draw;

/**
 * brick.
 */
public final class Brick implements Tangible {

    private final int x;
    private final int y;

    /**
     * create object brick.
     *
     * @param x parameter
     * @param y parameter
     */
    public Brick(final int x, final int y) {
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
    

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTouched(final int x, final int y) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isTouched'");
    }

    @Override
    public String getType() {
        return "brick";
    }
}
