package view.api;

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

    /**
     * Builds a result panel with a title and a single action to return to the menu.
     *
     * @param controller menu controller to notify on exit
     * @param onClose callback executed after requesting the menu change
     * @param titleText title displayed on the panel
     * @param titleSize font size for the title
     */
    @SuppressWarnings("PMD.ConstructorCallsOverridableMethod")
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
