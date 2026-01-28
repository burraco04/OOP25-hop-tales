package view.impl;

import java.awt.Dimension;
import java.util.Optional;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import controller.AudioManager;
import controller.api.ControllerMenu;
import controller.api.State;
import model.GameConstants;
import view.utils.ButtonBackFactory;
import view.utils.FontFactory;
import view.utils.TopBarPanel;

/**
 * class options.
 */
public final class Options extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final String TITLE_FONT = "SuperShiny";
    private static final float TITLE_SIZE = 50f;
    private final transient FontFactory fontFactory = new FontFactory();
    private final transient ButtonBackFactory buttonbackFactory = new ButtonBackFactory();
    private final TopBarPanel topBarpan = new TopBarPanel();

    /**
     * create the clsaa.
     *
     * @param controller pass the controller
     */
    public Options(final ControllerMenu controller) {
    final JLabel title = new JLabel("audio");
    final JPanel sliderPanel = new JPanel();
    final JSlider volumeSlider = new JSlider(0, 100);

    setBackground(GameConstants.BACK_COLOR);
    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

    final JButton back = this.buttonbackFactory.buildbackbutton();
    back.addActionListener(e -> controller.handleEvent(State.MAIN_MENU, Optional.empty()));
    final JPanel topBar = topBarpan.buildTopPanel(back);

    title.setFont(this.fontFactory.getFont(TITLE_FONT, TITLE_SIZE, this));

    volumeSlider.setValue((int) (AudioManager.getMusicVolume() * 100));
    volumeSlider.addChangeListener(e -> {AudioManager.setMusicVolume(volumeSlider.getValue() / 100f);});
    sliderPanel.add(volumeSlider);
    sliderPanel.setBackground(GameConstants.BACK_COLOR);
    sliderPanel.setPreferredSize(new Dimension(350, 60));
    sliderPanel.setMaximumSize(new Dimension(350, 60));

    title.setAlignmentX(CENTER_ALIGNMENT);
    sliderPanel.setAlignmentX(CENTER_ALIGNMENT);

    this.add(topBar);
    this.add(title);
    this.add(sliderPanel);

    }

}
