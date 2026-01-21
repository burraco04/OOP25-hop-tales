package model.objects.impl;

import model.GameConstants;
import model.objects.api.Tangible;

/**
 * Class defining coins instances.
 */
public class Coin implements Tangible{
    private double x;
    private double y;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTouched(int x, int y) {
        if ((x > this.x && x + GameConstants.PLAYER_WIDTH < this.x) 
            || (y > this.y && y + GameConstants.PLAYER_HEIGHT < this.y)) {
            return true;
        } else {
           return false; 
        }
        
    }

}
