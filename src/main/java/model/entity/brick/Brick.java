package model.entity.brick;

import java.awt.Color;
import java.awt.Graphics;

import model.entity.Entity;



public class Brick implements Entity {

    private static final int TILE_SIZE = 32;

    private final int x;
    private final int y;
 

    public Brick(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getX() { return x; }

    @Override
    public int getY() { return y; }

    @Override
    public void draw(final Graphics g, int camX) {
        g.setColor(Color.ORANGE);
        g.fillRect(
        x * TILE_SIZE - camX, 
        y * TILE_SIZE, 
        TILE_SIZE, 
        TILE_SIZE
        );
    }
}
