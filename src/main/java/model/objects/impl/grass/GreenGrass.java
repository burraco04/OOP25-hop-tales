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

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public boolean isTouched(int x, int y) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isTouched'");
    }

    @Override
    public void draw(Graphics g, int camX, int x, int y) {
         draw.drawPanel(g, image, x, y);
    }
    
}
