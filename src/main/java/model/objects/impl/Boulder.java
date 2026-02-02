package model.objects.impl;

import model.objects.api.WorldObject;

public class Boulder implements WorldObject {

    private int x, y, w, h;

    private double vy = 0;

    public Boulder(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
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
        return "BOULDER"; 
    }

    
    public int getW() {
        return w; 
    }
    public int getH() {
        return h; 
    }

    public double getVy() { 
        return vy; 
    }
    public void setVy(double vy) {
        this.vy = vy;
    }

    public void setX(int x) {
        this.x = x; 
        }
    public void setY(int y) {
        this.y = y;
}

    //logica caduta masso
    public void applyGravity() {
        vy += 0.35;
        if (vy > 10) vy = 10;
    }

    public void stepVertical() {
        y = (int) (y + vy);
    }

    public void stopVertical() {
        vy = 0;
    }
}
