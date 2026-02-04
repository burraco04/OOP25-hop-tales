package model.objects.impl;

import java.awt.Graphics;
import model.objects.api.WorldEntity;

public class Teleporter extends WorldEntity {

    public Teleporter(int x, int y, int w, int h) {
        super(x, y, w, h, "TELEPORTER");
    }

    @Override
    public void draw(Graphics g) {
        // niente: è già disegnato nello sfondo
    }
}
