package view.impl;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import controller.api.ControllerMenu;
import view.utils.ButtonFactory;

/**
 * pannel.
 */
public final class ChooseLevelPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final float TITLE_SIZE = 50F;
    private static final int PADDING = 50;
    private final transient ButtonFactory buttonFactory = new ButtonFactory();

    /**
     * choose level.
     *
     * @param controller creo controller
     */

    public ChooseLevelPanel(final ControllerMenu controller) {

        final JLabel title = new JLabel("GIVE ME SOME PENALTY");

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        final JButton level1 = this.buttonFactory.buildbutton("penalty");
        final JButton level2 = this.buttonFactory.buildbutton("penalty2");
        final JButton level3 = this.buttonFactory.buildbutton("penalty3");

        title.setFont(title.getFont().deriveFont(TITLE_SIZE));

        title.setAlignmentX(CENTER_ALIGNMENT);
        level1.setAlignmentX(CENTER_ALIGNMENT);
        level2.setAlignmentX(CENTER_ALIGNMENT);
        level3.setAlignmentX(CENTER_ALIGNMENT);

        this.add(title);
        this.add(Box.createVerticalStrut(PADDING));
        this.add(level1);
        this.add(level2);
        this.add(level3);
    }
}
