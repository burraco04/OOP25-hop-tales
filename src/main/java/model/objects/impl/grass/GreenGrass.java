package model.objects.impl.grass;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

import model.objects.api.Tangible;
import view.utils.Draw;

public final class GreenGrass implements Tangible{

    private final int x;
    private final int y;
    private final Image image;
    private final Draw draw = new Draw();

     public GreenGrass(final int x, final int y) {
        this.x = x;
        this.y = y;
        this.image = new ImageIcon(
            getClass().getResource("/img/green_grass.png")
        ).getImage();
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
    public void draw(final Graphics g, int camX) {
         draw.drawPanel(g, image, this.x, this.y);
    }
    
}
