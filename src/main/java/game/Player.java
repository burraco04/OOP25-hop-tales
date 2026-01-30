package game;

import java.awt.*;

public class Player {
    int x, y, w = 20, h = 22;
    int vx = 0;
    double vy = 0;
    boolean onGround = false;
    final Color color;

    public Player(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    void jump() {
        if (onGround) {
            vy = -8.5;
            onGround = false;
        }
    }

    Rectangle getRect() { return new Rectangle(x, y, w, h); }

    void update(FireboyWatergirlLevel world) {
        vy += 0.35;
        if (vy > 10) vy = 10;

        int nx = x + vx;
        if (!collides(world, nx, y)) x = nx;

        int ny = (int) (y + vy);
        if (!collides(world, x, ny)) {
            y = ny;
            onGround = false;
        } else {
            if (vy > 0) onGround = true;
            vy = 0;
        }
    }

    private boolean collides(FireboyWatergirlLevel w, int nx, int ny) {
        return w.isSolidAtPixel(nx + 1, ny + 1)
                || w.isSolidAtPixel(nx + this.w - 2, ny + 1)
                || w.isSolidAtPixel(nx + 1, ny + this.h - 2)
                || w.isSolidAtPixel(nx + this.w - 2, ny + this.h - 2);
    }

    void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, w, h);
    }
}
