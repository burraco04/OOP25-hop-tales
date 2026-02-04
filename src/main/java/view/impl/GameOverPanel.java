package view.impl;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.api.ControllerMenu;
import controller.api.State;
import view.utils.ButtonFactory;
import view.utils.Draw;

/**
 * Game over panel.
 */
public final class GameOverPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int PADDING = 24;
    private static final float TITLE_SIZE = 36f;
    private final transient ButtonFactory buttonFactory = new ButtonFactory();

    /**
     * Create a game over panel.
     *
     * @param controller the menu controller.
     * @param onClose a Runnable to handle the pressing of the button.
     */
    public GameOverPanel(final ControllerMenu controller, final Runnable onClose) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        final JLabel title = new JLabel("GAME OVER");
        title.setFont(title.getFont().deriveFont(TITLE_SIZE));
        title.setAlignmentX(CENTER_ALIGNMENT);

        final JButton back = this.buttonFactory.buildbutton("main menu");
        back.setAlignmentX(CENTER_ALIGNMENT);
        back.addActionListener(e -> {
            controller.handleEvent(State.MAIN_MENU);
            onClose.run();
        });

        this.add(Box.createVerticalStrut(PADDING));
        this.add(title);
        this.add(Box.createVerticalStrut(PADDING));
        this.add(back);
        this.add(Box.createVerticalStrut(PADDING));
    }
}
