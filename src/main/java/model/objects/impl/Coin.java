package model.objects.impl;

import java.awt.Graphics;

import model.GameConstants;
import model.objects.api.Tangible;

/**
 * Class defining coins instances.
 */
public class Coin implements Tangible {
    private final double x;
    private final double y;

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
    public boolean isTouched(final int x, final int y) {
        if (x > this.x && x + GameConstants.PLAYER_WIDTH < this.x
            || y > this.y && y + GameConstants.PLAYER_HEIGHT < this.y) {
            return true;
        } else {
           return false; 
        }    
    }

    @Override
    public int getX() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getX'");
    }

    @Override
    public int getY() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getY'");
    }

    @Override
    public void draw(Graphics g, int camX, int x, int y) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'draw'");
    }

}
