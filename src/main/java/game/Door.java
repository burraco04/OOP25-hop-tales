package game;

import java.awt.Color;
import java.awt.Graphics;

public class Door extends Entity {
    boolean open = false;

    public Door(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    @Override
    void draw(Graphics g) {
        if (open) {
            g.setColor(new Color(120, 220, 120, 120));
            g.fillRect(x, y, w, h);
        } else {
            g.setColor(new Color(180, 180, 255));
            g.fillRect(x, y, w, h);
        }
    }
}
