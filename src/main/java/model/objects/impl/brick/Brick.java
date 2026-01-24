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
    private final Image image;
    private final Draw draw = new Draw();

    public Brick(final int x, final int y) {
        this.x = x;
        this.y = y;
        this.image = new ImageIcon(
            getClass().getResource("")
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
