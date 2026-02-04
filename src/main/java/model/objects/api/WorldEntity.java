package model.objects.api;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public abstract class WorldEntity implements WorldObject {

    protected int x, y, w, h;
    private final String type;

    public WorldEntity(int x, int y, int w, int h, String type) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.type = type;
    }

    @Override
    public final int getX() { return x; }

    @Override
    public final int getY() { return y; }

    @Override
    public final String getType() { return type; }

    
    public final int getW() { return w; }
    public final int getH() { return h; }

    //  spostamento pubblico 
    public final void translate(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    public final Rectangle rect() { return new Rectangle(x, y, w, h); }

    public final boolean intersects(Rectangle r) { return rect().intersects(r); }

    public final boolean contains(int px, int py) { return rect().contains(px, py); }

    protected final void drawTiled(Graphics g, BufferedImage tile, int tileSize) {
        if (tile == null) return;

        for (int yy = y; yy < y + h; yy += tileSize) {
            for (int xx = x; xx < x + w; xx += tileSize) {
                int ww = Math.min(tileSize, (x + w) - xx);
                int hh = Math.min(tileSize, (y + h) - yy);

                g.drawImage(tile,
                        xx, yy, xx + ww, yy + hh,
                        0, 0, ww, hh,
                        null);
            }
        }
    }

    public abstract void draw(Graphics g);
}
