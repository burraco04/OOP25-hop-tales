package game;

import java.awt.*;

public class MovingPlatform extends Entity {
    int startY;
    int targetDy = 0;
    double speed = 1.0;

    boolean isLeftSide = false;

    int prevX, prevY;

    public MovingPlatform(int x, int y, int w, int h) {
        super(x, y, w, h);
        startY = y;
        prevX = x;
        prevY = y;
    }

    void setBalanceRole(boolean isLeft, int dyWhenActive, double speed) {
        this.isLeftSide = isLeft;
        this.targetDy = dyWhenActive;
        this.speed = speed;
    }

    void updateBalance(boolean active) {
        prevX = x;
        prevY = y;

        int desiredY = active ? (startY + targetDy) : startY;

        if (y < desiredY) y += (int) Math.ceil(speed);
        if (y > desiredY) y -= (int) Math.ceil(speed);

        if (Math.abs(y - desiredY) <= 1) y = desiredY;
    }

    int deltaX() { return x - prevX; }
    int deltaY() { return y - prevY; }

    @Override
    void draw(Graphics g) {
        g.setColor(new Color(220, 220, 220));
        g.fillRect(x, y, w, h);
    }
}
