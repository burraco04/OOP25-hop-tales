package model.objects.impl;

import model.objects.api.WorldObject;

// rappresenta una piattaforma mobile che può muoversi verticalmente
public class MovingPlatform implements WorldObject {

    private int x, y, w, h;

    private int startY;
    private int targetDy = 0;
    private double speed = 1.0;

    private boolean isLeftSide = false;

    private int prevX, prevY;

    public MovingPlatform(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        this.startY = y;
        this.prevX = x;
        this.prevY = y;
    }

  
    @Override
    public int getX() { 
        return x; 
    }

    @Override
    public int getY() { 
        return y; 
    }

    @Override
    public String getType() { 
        return "PLATFORM"; 
    }


  
    public int getW() {
        return w; 
    }

    public int getH() { 
        return h; 
    }

    //logica specifica della piattaforma mobile
    public void setBalanceRole(boolean isLeft, int dyWhenActive, double speed) {
        this.isLeftSide = isLeft;
        this.targetDy = dyWhenActive;
        this.speed = speed;
    }

    public boolean isLeftSide() {
        return isLeftSide;
    }

    public void updateBalance(boolean active) {
        prevX = x;
        prevY = y;

        int desiredY = active ? (startY + targetDy) : startY;

        int step = (int) Math.ceil(speed);
        if (y < desiredY) y += step;
        if (y > desiredY) y -= step;

        if (Math.abs(y - desiredY) <= 1) y = desiredY;
    }

    public int deltaX() {
        return x - prevX; 
    }

    public int deltaY() {
        return y - prevY; 
    }
}
