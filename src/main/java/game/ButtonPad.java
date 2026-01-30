package game;

import java.awt.*;

public class ButtonPad extends Entity {

    public ButtonPad(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    @Override
    void draw(Graphics g) {
        g.setColor(new Color(255, 80, 200));
        g.fillRect(x + 4, y + 10, w - 8, h - 12);
    }
}
