package model.objects.impl;

import model.objects.api.WorldObject;

// rappresenta un teletrasportatore che trasporta il giocatore in un altro punto
public class Teleporter implements WorldObject {

    private int x, y, w, h;

    // destinazione del teletrasporto
    private int targetX;
    private int targetY;

    public Teleporter(int x, int y, int w, int h) {
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
        return "TELEPORTER"; 
    }

  
    public int getW() {
        return w; 
    }

    public int getH() {
        return h; 
    }

    //logica teletrasporto 
    public void setTarget(int tx, int ty) {
        this.targetX = tx;
        this.targetY = ty;
    }

    public int getTargetX() { 
        return targetX; 
    }

    public int getTargetY() { 
        return targetY; 
    }
}
