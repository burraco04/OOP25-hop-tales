package view.utils;

import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public final class ShopButtonFactory {
    private static final FontFactory fontFactory = new FontFactory();
    private static final String TITLE_FONT = "SuperShiny";
    private static final float BUTTON_SIZE = 50f;
    private static final Color back = new Color(205, 170, 125);

    public static JButton build(String path) {
        final ImageIcon icon = new ImageIcon(ShopButtonFactory.class.getResource(path));
        final Image scaledImage = icon.getImage().getScaledInstance( 160,   160,  Image.SCALE_SMOOTH);
        JButton skin1 = new JButton(new ImageIcon(scaledImage));
        skin1.setFont(fontFactory.getFont(TITLE_FONT, BUTTON_SIZE, skin1));
        skin1.setBackground(back);
        return skin1;
    }
}
