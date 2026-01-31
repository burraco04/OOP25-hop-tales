package game;

import java.awt.Color;
import java.awt.Graphics;

public class Coin extends Entity {
    boolean collected = false;

    public Coin(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    @Override
    void draw(Graphics g) {
        g.setColor(new Color(255, 230, 0));
        g.fillOval(x + 6, y + 6, w - 12, h - 12);
    }
}
