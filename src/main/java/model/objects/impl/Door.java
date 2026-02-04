package model.objects.impl;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import model.objects.api.WorldEntity;

public class Door extends WorldEntity {

    public boolean open = false;
    private final BufferedImage tileTexture;
    private final int tileSize;

    public Door(int x, int y, int w, int h, BufferedImage tileTexture, int tileSize) {
        super(x, y, w, h, "DOOR");
        this.tileTexture = tileTexture;
        this.tileSize = tileSize;
    }

    @Override
    public void draw(Graphics g) {
        if (open) return;
        drawTiled(g, tileTexture, tileSize);
    }
}
