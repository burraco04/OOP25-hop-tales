package model.objects.impl;

import model.objects.api.WorldObject;

// rappresenta una porta che può essere aperta o chiusa
public class Door implements WorldObject {

    private int x, y, w, h;
    private boolean open = false;

    public Door(int x, int y, int w, int h) {
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
        return "DOOR"; 
    }

    
    public int getW() {
        return w; 
    }

    public int getH() { 
        return h; 
    }

    //logica specifica della porta
    public boolean isOpen() { 
        return open; 
    }

    public void open() { 
        open = true; 
    }

    public void close() { 
        open = false; 
    }
}
