package game;

import java.awt.*;

public abstract class Entity {
    int x, y, w, h;

    Entity(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    Rectangle rect() { return new Rectangle(x, y, w, h); }

    boolean intersects(Rectangle r) { return rect().intersects(r); }

    boolean contains(int px, int py) { return rect().contains(px, py); }

    abstract void draw(Graphics g);
}
