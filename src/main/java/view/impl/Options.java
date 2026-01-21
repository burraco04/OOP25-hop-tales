package view.impl;

import java.awt.Color;
import java.util.Optional;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.api.ControllerMenu;
import controller.api.State;
import view.utils.ButtonBackFactory;
import view.utils.TopBarPanel;

/**
 * class options.
 */
public final class Options extends JPanel {
    private static final long serialVersionUID = 1L;
    private final transient ButtonBackFactory buttonbackFactory = new ButtonBackFactory();
    private final TopBarPanel topBarpan = new TopBarPanel();

    /**
     * create the clsaa.
     *
     * @param controller pass the controller
     */
    public Options(final ControllerMenu controller) {
    final JLabel title = new JLabel("pessi ahahaha");
    setBackground(Color.GREEN);
    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

    final JButton back = this.buttonbackFactory.buildbackbutton();
    back.addActionListener(e -> controller.handleEvent(State.MAIN_MENU, Optional.empty()));
    final JPanel topBar = topBarpan.buildTopPanel(back);

    this.add(topBar);
    this.add(title);

    }

}
