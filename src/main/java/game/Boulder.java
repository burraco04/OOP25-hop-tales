package game;

import java.awt.*;

public class Boulder extends Entity {
    double vy = 0;
    boolean onGround = false;

    public Boulder(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    void updatePhysics(FireboyWatergirlLevel world) {
        vy += 0.35;
        if (vy > 10) vy = 10;

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

    void tryPushBy(Player p, FireboyWatergirlLevel world) {
        Rectangle pr = p.getRect();
        Rectangle br = this.rect();
        if (!pr.intersects(br)) return;

        if (p.vx > 0 && pr.x + pr.width <= br.x + 6) {
            int nx = x + 2;
            if (!world.isSolidAtPixel(nx + w, y + 1) && !world.isSolidAtPixel(nx + w, y + h - 2)) {
                x = nx;
            }
        } else if (p.vx < 0 && pr.x >= br.x + br.width - 6) {
            int nx = x - 2;
            if (!world.isSolidAtPixel(nx, y + 1) && !world.isSolidAtPixel(nx, y + h - 2)) {
                x = nx;
            }
        }
    }

    @Override
    void draw(Graphics g) {
        g.setColor(new Color(140, 110, 90));
        g.fillRect(x, y, w, h);
    }
}
