package game;

import java.awt.*;

public class Teleporter extends Entity {

    public Teleporter(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    @Override
    void draw(Graphics g) {
        g.setColor(new Color(160, 80, 255));
        g.drawRect(x + 2, y + 2, w - 4, h - 4);
        g.drawLine(x + 2, y + 2, x + w - 2, y + h - 2);
        g.drawLine(x + w - 2, y + 2, x + 2, y + h - 2);
    }
}
