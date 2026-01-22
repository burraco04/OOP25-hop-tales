package model.entity.grass;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

import model.entity.Entity;

public class Grass implements Entity {

    public static final int TILE_SIZE = 32;//pixel

    private final int x;
    private final int y;
    private final Image image;


     public Grass(final int x, final int y) {
        this.x = x;
        this.y = y;
        this.image = new ImageIcon(
            getClass().getResource("/img/ground.png")
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
    public void draw(Graphics g) {
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
