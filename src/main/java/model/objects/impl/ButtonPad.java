package model.objects.impl;

import java.awt.Graphics;
import model.objects.api.WorldEntity;

public class ButtonPad extends WorldEntity {

    public ButtonPad(int x, int y, int w, int h) {
        super(x, y, w, h, "BUTTON");
    }

    @Override
    public void draw(Graphics g) {
        // niente: è già nello sfondo
    }
}
