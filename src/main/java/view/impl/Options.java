package view.impl;

import java.awt.Dimension;

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
 * This class represents the options menu of the game and is responsible for displaying and managing
 * user-configurable settings (audio volume).
 */
public final class Options extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final String TITLE_FONT = "SuperShiny";
    private static final float TITLE_SIZE = 50f;
    private static final int SLIDER_PANEL_WIDTH = 350;
    private static final int SLIDER_PANEL_HEIGHT = 350;
    private static final float SLIDER_SCALA = 100f;

    private final transient FontFactory fontFactory = new FontFactory();
    private final transient ButtonBackFactory buttonBackFactory = new ButtonBackFactory();
    private final TopBarPanel topBarPanel = new TopBarPanel();

    private final JLabel title = new JLabel("Audio");
    private final JPanel sliderPanel = new JPanel();
    private final JSlider volumeSlider = new JSlider(0, 100);

    /**
     * Creates the Options panel.
     *
     * @param controller the menu controller used to handle user interactions and navigation
     */
    public Options(final ControllerMenu controller) {

    setBackground(GameConstants.BACK_COLOR);
    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

    final JButton back = this.buttonBackFactory.buildbackbutton();
    back.addActionListener(e -> controller.handleEvent(State.MAIN_MENU));
    final JPanel topBar = topBarPanel.buildTopPanel(back);

    title.setFont(this.fontFactory.getFont(TITLE_FONT, TITLE_SIZE, this));

    volumeSlider.setValue((int) (AudioManager.getMusicVolume() * SLIDER_SCALA));
    volumeSlider.addChangeListener(e -> {
    AudioManager.setMusicVolume(volumeSlider.getValue() / SLIDER_SCALA);
    });
    sliderPanel.add(volumeSlider);
    sliderPanel.setBackground(GameConstants.BACK_COLOR);
    sliderPanel.setPreferredSize(new Dimension(SLIDER_PANEL_WIDTH, SLIDER_PANEL_HEIGHT));
    sliderPanel.setMaximumSize(new Dimension(SLIDER_PANEL_WIDTH, SLIDER_PANEL_HEIGHT));

    title.setAlignmentX(CENTER_ALIGNMENT);
    sliderPanel.setAlignmentX(CENTER_ALIGNMENT);

    this.add(topBar);
    this.add(title);
    this.add(sliderPanel);

    }

}
