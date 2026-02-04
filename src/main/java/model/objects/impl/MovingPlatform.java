package model.objects.impl;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import model.objects.api.WorldEntity;

public class MovingPlatform extends WorldEntity {

    private final BufferedImage tileTexture;
    private final int tileSize;

    public int startY;
    public int targetDy = 0;
    public double speed = 1.0;

    public boolean isLeftSide = false;

    private int prevX, prevY;

    public MovingPlatform(int x, int y, int w, int h, BufferedImage tileTexture, int tileSize) {
        super(x, y, w, h, "PLATFORM");
        this.tileTexture = tileTexture;
        this.tileSize = tileSize;

        startY = y;
        prevX = x;
        prevY = y;
    }

    public void setBalanceRole(boolean isLeft, int dyWhenActive, double speed) {
        this.isLeftSide = isLeft;
        this.targetDy = dyWhenActive;
        this.speed = speed;
    }

    public void updateBalance(boolean active) {
        prevX = x;
        prevY = y;

        int desiredY = active ? (startY + targetDy) : startY;

        if (y < desiredY) y += (int) Math.ceil(speed);
        if (y > desiredY) y -= (int) Math.ceil(speed);

        if (Math.abs(y - desiredY) <= 1) y = desiredY;
    }

    public int deltaX() { return x - prevX; }
    public int deltaY() { return y - prevY; }

    @Override
    public void draw(Graphics g) {
        drawTiled(g, tileTexture, tileSize);
    }
}
