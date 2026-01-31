package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Boulder extends Entity {

    double vy = 0;
    

    public Boulder(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    // ====== FISICA (CADUTA) ======
    void updatePhysics(FireboyWatergirlLevel world) {
        // gravità
        vy += 0.35;
        if (vy > 10) vy = 10;

        int ny = (int) (y + vy);

        if (!collides(world, x, ny)) {
            y = ny;
           
        } else {
            
            vy = 0;
        }
    }

    private boolean collides(FireboyWatergirlLevel w, int nx, int ny) {
        // IMPORTANTISSIMO: usa la versione con ignore = this
        return w.isSolidAtPixel(nx + 1, ny + 1, this)
                || w.isSolidAtPixel(nx + this.w - 2, ny + 1, this)
                || w.isSolidAtPixel(nx + 1, ny + this.h - 2, this)
                || w.isSolidAtPixel(nx + this.w - 2, ny + this.h - 2, this);
    }

    // ====== PUSH (SPINTA DEL PLAYER) ======
    void tryPushBy(Player p, FireboyWatergirlLevel world) {
    if (p.vx == 0) return;

    Rectangle pr = p.getRect();
    Rectangle br = this.rect();

    // deve essere allineato in verticale (spinta laterale)
    boolean verticalOverlap =
            pr.y + pr.height > br.y + 4 &&
            pr.y < br.y + br.height - 4;
    if (!verticalOverlap) return;

    int push = p.vx;   // 3 o -3
    int tol = 8;       // tolleranza più ampia

    // spinta a destra: player deve essere vicino al lato sinistro del masso
    if (push > 0) {
        boolean closeToLeft =
                pr.x + pr.width >= br.x - tol &&
                pr.x + pr.width <= br.x + tol;
        if (!closeToLeft) return;

        int nx = x + push;

        // controlla che davanti al masso sia libero (ignora sé stesso)
        if (!world.isSolidAtPixel(nx + w - 1, y + 2, this) &&
            !world.isSolidAtPixel(nx + w - 1, y + h - 2, this)) {
            x = nx;
        }
    }

    // spinta a sinistra: player deve essere vicino al lato destro del masso
    if (push < 0) {
        boolean closeToRight =
                pr.x >= br.x + br.width - tol &&
                pr.x <= br.x + br.width + tol;
        if (!closeToRight) return;

        int nx = x + push;

        if (!world.isSolidAtPixel(nx, y + 2, this) &&
            !world.isSolidAtPixel(nx, y + h - 2, this)) {
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
