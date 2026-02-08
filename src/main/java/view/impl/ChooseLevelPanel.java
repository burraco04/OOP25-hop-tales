package view.impl;

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
 * Panel that allows the user to select a game level.
 * It displays the available levels and notifies the controller when a selection is made.
 */
public final class ChooseLevelPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final float TITLE_SIZE = 50F;
    private static final int PADDING = 50;
    private final transient ButtonFactory buttonFactory = new ButtonFactory();
    private final transient ButtonBackFactory buttonbackFactory = new ButtonBackFactory();
    private final TopBarPanel topBarpan = new TopBarPanel();

    /**
     * constructor of the panel.
     *
     * @param controller the menu controller used to handle user actions
     */

    public ChooseLevelPanel(final ControllerMenu controller) {

        final JLabel title = new JLabel("CHOOSE THE LEVEL");
        setBackground(GameConstants.BACK_COLOR);

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        final JButton level1 = this.buttonFactory.buildbutton("LEVEL_1");
        final JButton level2 = this.buttonFactory.buildbutton("LEVEL_2");
        final JButton level3 = this.buttonFactory.buildbutton("LEVEL_3");

        title.setFont(title.getFont().deriveFont(TITLE_SIZE));

        title.setAlignmentX(CENTER_ALIGNMENT);
        level1.setAlignmentX(CENTER_ALIGNMENT);
        level2.setAlignmentX(CENTER_ALIGNMENT);
        level3.setAlignmentX(CENTER_ALIGNMENT);

        level1.addActionListener(e -> controller.handleEvent(State.LEVEL_1));

        level2.addActionListener(e -> controller.handleEvent(State.LEVEL_2));
        level3.addActionListener(e -> controller.handleEvent(State.LEVEL_3));

        final JButton back = this.buttonbackFactory.buildbackbutton();

        back.addActionListener(e -> controller.handleEvent(State.MAIN_MENU));

        final JPanel topBar = topBarpan.buildTopPanel(back);

        this.add(topBar);
        this.add(title);
        this.add(Box.createVerticalStrut(PADDING));
        this.add(level1);
        this.add(level2);
        this.add(level3);
    }
}
