package view.utils;

import java.awt.Graphics;
import java.awt.Image;

/**
 * calss draw
 */
public final class Draw {
    public static final int TILE_SIZE = 32; //pixel

    /**
     * function for paint the.
     *
     * @param g g
     * @param image image of the 
     * @param x parameter x
     * @param y parametr y
     */
    public void drawPanel(final Graphics g, final Image image, final int x, final int y ){
        g.drawImage(
            image,
            x * TILE_SIZE,
            y * TILE_SIZE,
            TILE_SIZE,
            TILE_SIZE,
            null
        );
    }
}
