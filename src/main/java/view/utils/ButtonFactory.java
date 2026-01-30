package view.utils;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
 * button factory.
 */

public class ButtonFactory {

    private static final String TITLE_FONT = "SuperShiny";
    private static final float BUTTON_SIZE = 50f;
    private static final Dimension BUTTON_DIM = new Dimension(320, 100);
    private final FontFactory fontFactory = new FontFactory();

    /**
     * create button.
     *
     * @param nameButton name of the button
     * @return the button complete
     */
    public JButton buildbutton(final String nameButton) {
        final JButton button = new JButton(nameButton);

        button.setFont(this.fontFactory.getFont(TITLE_FONT, BUTTON_SIZE, button));
        button.setForeground(Color.GREEN);
        button.setBackground(Color.GRAY);

        button.setMaximumSize(BUTTON_DIM);
        button.setMinimumSize(BUTTON_DIM);

        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);

        return button;
    }
}
