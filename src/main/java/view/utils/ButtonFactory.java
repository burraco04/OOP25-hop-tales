package view.utils;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
 * Button factory.
 */

public class ButtonFactory {

    private static final String TITLE_FONT = "SuperShiny";
    private static final float BUTTON_SIZE = 50f;
    private static final Dimension BUTTON_DIM = new Dimension(320, 100);
    private final FontFactory fontFactory = new FontFactory();

    /**
     * creates a button.
     *
     * @param nameButton the text displayed on the button
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

        return button;
    }
}
