package view.impl;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.api.ControllerMenu;
import controller.api.State;
import view.utils.ButtonFactory;
import view.utils.CreateBackground;
import view.utils.FontFactory;

/**
 * Main menu panel of the application.
 * This class represents the initial screen of the game,allowing the user to start a new game, access options,
 * or open the game shop.
 */

public final class Menu extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final String TITLE_FONT = "SuperShiny";
    private static final float TITLE_SIZE = 100f;
    private final transient FontFactory fontFactory = new FontFactory();
    private final transient ButtonFactory buttonFactory = new ButtonFactory();
    private final transient Image image = CreateBackground.create("/img/Piattaforma_retro_con_personaggio_pixelato.png");

    /**
     * constructor of menu.
     *
     * @param controller the menu controller used to handle user actions.
     */

    public Menu(final ControllerMenu controller) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        final JLabel title = new JLabel("HOP TALES");
        title.setFont(this.fontFactory.getFont(TITLE_FONT, TITLE_SIZE, this));
        title.setAlignmentY(CENTER_ALIGNMENT);

        final JButton start = this.buttonFactory.buildbutton("start");
        final JButton options = this.buttonFactory.buildbutton("opzioni");
        final JButton shop = this.buttonFactory.buildbutton("shop");

        start.addActionListener(e -> controller.handleEvent(State.CHOOSE_LEVEL));
        options.addActionListener(e -> controller.handleEvent(State.OPTIONS));
        shop.addActionListener(e -> controller.handleEvent(State.SHOP));

        start.setAlignmentX(CENTER_ALIGNMENT);
        options.setAlignmentX(CENTER_ALIGNMENT);
        shop.setAlignmentX(CENTER_ALIGNMENT);

        this.add(title);
        this.add(start);
        this.add(options);
        this.add(shop);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
         g.drawImage(this.image, 0, 0, getWidth(), getHeight(), this);
    }
}
