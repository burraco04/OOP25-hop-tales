package model.objects.impl;

import model.objects.api.WorldObject;

public class ButtonPad implements WorldObject {

    private int x, y, w, h;
    private boolean pressed = false;

    public ButtonPad(int x, int y, int w, int h) {
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
        return "BUTTON"; 
    }

    
    public int getW() {
        return w; 
    }

    public int getH() {
        return h; 
    }

    //stato bottone
    public boolean isPressed() {
        return pressed; 
    }

    public void press() {
        pressed = true; 
    }

    public void release() {
        pressed = false; 
    }
}
