package view.utils;

import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * create the button for the shop.
 */
public final class ShopButtonFactory {
    private static final FontFactory fontFactory = new FontFactory();
    private static final String TITLE_FONT = "SuperShiny";
    private static final float BUTTON_SIZE = 50f;
    private static final Color BACK = new Color(205, 170, 125);
    private static final int WIDTH_HEIGHT = 160;

    /**
     * create button.
     *
     * @param path of the background
     * @return button.
     */
    public static JButton build(final String path) {
        final ImageIcon icon = new ImageIcon(ShopButtonFactory.class.getResource(path));
        final Image scaledImage = icon.getImage().getScaledInstance(WIDTH_HEIGHT, WIDTH_HEIGHT, Image.SCALE_SMOOTH);
        final JButton button = new JButton(new ImageIcon(scaledImage));
        button.setFont(fontFactory.getFont(TITLE_FONT, BUTTON_SIZE, button));
        button.setBackground(BACK);
        return button;
    }
}
