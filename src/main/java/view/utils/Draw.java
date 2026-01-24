package view.utils;

import java.awt.Graphics;
import java.awt.Image;

import model.GameConstants;

/**
 * calss draw.
 */
public final class Draw {

    /**
     * function for paint the.
     *
     * @param g g
     * @param image image of the 
     * @param x parameter x
     * @param y parametr y
     */
    public void drawPanel(final Graphics g, final Image image, final int x, final int y ) {
        g.drawImage(
            image,
            x * GameConstants.TILE_SIZE,
            y * GameConstants.TILE_SIZE,
            GameConstants.TILE_SIZE,
            GameConstants.TILE_SIZE,
            null
        );
    }
}
