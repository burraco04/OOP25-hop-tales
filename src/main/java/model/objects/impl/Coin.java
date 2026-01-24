package model.objects.impl;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

import model.GameConstants;
import model.objects.api.Tangible;
import view.utils.Draw;

/**
 * Class defining coins instances.
 */
public class Coin implements Tangible {
    private final int x;
    private final int y;
    private final Image image;
    private final Draw draw = new Draw();

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
        this.image = new ImageIcon(
            getClass().getResource("/img/coin_gold.png")
        ).getImage();
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
    public void draw(final Graphics g, int camX) {
        draw.drawPanel(g, image, this.x, this.y);
    }

}
