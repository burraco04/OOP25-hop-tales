package view.utils;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;

/**
 * button for go back.
 */
public class ButtonBackFactory {
    private static final float BUTTON_SIZE = 10f;
    private static final Dimension BUTTON_DIM = new Dimension(30, 5);
    private static final Color BUTTON_COLOR = new Color(139, 69, 19);

    /**
     * implemnete button.
     *
     * @return button finish
     */
    public JButton buildbackbutton() {
        final JButton button = new JButton("BACK");

        button.setForeground(Color.BLACK);
        button.setBackground(BUTTON_COLOR);

        button.setMaximumSize(BUTTON_DIM);
        button.setMinimumSize(BUTTON_DIM);

        button.setFont(button.getFont().deriveFont(BUTTON_SIZE));

        return button;

    }
}
