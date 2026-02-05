package view.impl;

import java.util.Optional;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import controller.api.ControllerMenu;
import controller.api.State;
import model.GameConstants;
import view.utils.ButtonBackFactory;
import view.utils.ButtonFactory;
import view.utils.TopBarPanel;

/**
 * pannel.
 */
public final class ChooseLevelPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final float TITLE_SIZE = 50F;
    private static final int PADDING = 50;
    private final transient ButtonFactory buttonFactory = new ButtonFactory();
    private final transient ButtonBackFactory buttonbackFactory = new ButtonBackFactory();
    private final TopBarPanel topBarpan = new TopBarPanel();

    /**
     * choose level.
     *
     * @param controller creo controller
     */

    public ChooseLevelPanel(final ControllerMenu controller) {

        final JLabel title = new JLabel("GIVE ME SOME PENALTY");
        setBackground(GameConstants.BACK_COLOR);

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        final JButton level1 = this.buttonFactory.buildbutton("penalty");
        final JButton level2 = this.buttonFactory.buildbutton("penalty2");
        final JButton level3 = this.buttonFactory.buildbutton("penalty3");

        title.setFont(title.getFont().deriveFont(TITLE_SIZE));

        title.setAlignmentX(CENTER_ALIGNMENT);
        level1.setAlignmentX(CENTER_ALIGNMENT);
        level2.setAlignmentX(CENTER_ALIGNMENT);
        level3.setAlignmentX(CENTER_ALIGNMENT);

        level1.addActionListener(e -> controller.handleEvent(State.LEVEL_1, Optional.empty()));
        level3.addActionListener(e -> controller.handleEvent(State.LEVEL_3, Optional.empty()));

        final JButton back = this.buttonbackFactory.buildbackbutton();

        back.addActionListener(e -> controller.handleEvent(State.MAIN_MENU, Optional.empty()));

        final JPanel topBar = topBarpan.buildTopPanel(back);

        this.add(topBar);
        this.add(title);
        this.add(Box.createVerticalStrut(PADDING));
        this.add(level1);
        this.add(level2);
        this.add(level3);
    }
}
