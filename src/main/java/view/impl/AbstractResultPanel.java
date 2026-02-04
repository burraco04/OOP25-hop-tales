package view.impl;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.api.ControllerMenu;
import controller.api.State;
import view.utils.ButtonFactory;

/**
 * Base panel for result screens like game over or level completed.
 */
public abstract class AbstractResultPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int PADDING = 24;
    private final transient ButtonFactory buttonFactory = new ButtonFactory();

    protected AbstractResultPanel(
        final ControllerMenu controller,
        final Runnable onClose,
        final String titleText,
        final float titleSize
    ) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        final JLabel title = new JLabel(titleText);
        title.setFont(title.getFont().deriveFont(titleSize));
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
