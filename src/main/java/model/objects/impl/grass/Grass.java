package model.objects.impl.grass;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

import model.objects.api.Tangible;
import view.utils.Draw;

/**
 * grass class.
 */
public final class Grass implements Tangible {

    private final int x;
    private final int y;
    private final Image image;
    private final Draw draw = new Draw();

     /**
      * class grass.
      *
      * @param x parameter
      * @param y parameter
      */
     public Grass(final int x, final int y) {
        this.x = x;
        this.y = y;
        this.image = new ImageIcon(
            getClass().getResource("/img/grass.png")
        ).getImage();
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
    public void draw(final Graphics g, final int camX, final int x, final int y) {
        draw.drawPanel(g, image, x, y);
    }

    @Override
    public boolean isTouched(int x, int y) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isTouched'");
    }

}
