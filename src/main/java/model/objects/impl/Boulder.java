package model.objects.impl;

import view.impl.FireboyWatergirlLevel;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import model.entities.impl.PlayerImpl;
import model.objects.api.WorldEntity;

public class Boulder extends WorldEntity {

    public double vy = 0;
    private final BufferedImage tileTexture;
    private final int tileSize;

    public Boulder(int x, int y, int w, int h, BufferedImage tileTexture, int tileSize) {
        super(x, y, w, h, "BOULDER");
        this.tileTexture = tileTexture;
        this.tileSize = tileSize;
    }

    public void updatePhysics(FireboyWatergirlLevel world) {
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
        return w.isSolidAtPixel(nx + 1, ny + 1, this)
                || w.isSolidAtPixel(nx + this.w - 2, ny + 1, this)
                || w.isSolidAtPixel(nx + 1, ny + this.h - 2, this)
                || w.isSolidAtPixel(nx + this.w - 2, ny + this.h - 2, this);
    }

    public void tryPushBy(PlayerImpl player, FireboyWatergirlLevel world) {
        Rectangle pr = new Rectangle(
                (int) Math.round(player.getX()),
                (int) Math.round(player.getY()),
                (int) Math.round(player.getWidth()),
                (int) Math.round(player.getHeight())
        );

        Rectangle br = rect();
        if (!pr.intersects(br)) {
            return;
        }

        double vx = player.getVelocityX();
        if (vx == 0) {
            return;
        }

        boolean verticalOverlap =
                pr.y + pr.height > br.y + 2
                        && pr.y < br.y + br.height - 2;
        if (!verticalOverlap) {
            return;
        }

        // Only push when the player is on the side, not on top.
        if (vx > 0) {
            if (pr.x + pr.width > br.x + 2) {
                return;
            }
        } else {
            if (pr.x < br.x + br.width - 2) {
                return;
            }
        }

        int step = vx > 0 ? 1 : -1;
        int steps = (int) Math.abs(vx);

        for (int i = 0; i < steps; i++) {
            int nextX = x + step;
            if (!collides(world, nextX, y)) {
                x = nextX;
            } else {
                break;
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        drawTiled(g, tileTexture, tileSize);
    }
}
