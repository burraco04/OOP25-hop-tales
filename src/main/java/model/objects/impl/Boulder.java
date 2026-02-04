package model.objects.impl;

import app.level.FireboyWatergirlLevel;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import model.objects.api.WorldEntity;

// Player lo aggancerai dopo: qui lo lasciamo come Object per non bloccarti ora.
// Quando avrai Player definitivo, rimetti Player al posto di Object.
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

    // TODO: quando avrai Player vero, rimetti Player qui
    public void tryPushBy(Object player, FireboyWatergirlLevel world) {
        // placeholder: verrà ripristinato quando integri Player del team
    }

    @Override
    public void draw(Graphics g) {
        drawTiled(g, tileTexture, tileSize);
    }
}
