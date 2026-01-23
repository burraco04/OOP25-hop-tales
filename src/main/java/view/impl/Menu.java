package view.impl;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.api.ControllerMenu;
import controller.api.State;
import view.utils.ButtonFactory;
import view.utils.FontFactory;

/**
 * menu.
 */

public final class Menu extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final String TITLE_FONT = "SuperShiny";
    private static final float TITLE_SIZE = 100f;
    private static final int WIDTH = 1;
    private static final int HEIGHT = 1;
    private final transient FontFactory fontFactory = new FontFactory();
    private final transient ButtonFactory buttonFactory = new ButtonFactory();
    private transient BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    /**
     * set menu.
     *
     * @param controller passo il controller
     */

    public Menu(final ControllerMenu controller) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        final JLabel title = new JLabel("PESSI & PENALDO");
        title.setFont(this.fontFactory.getFont(TITLE_FONT, TITLE_SIZE, this));
        title.setAlignmentY(CENTER_ALIGNMENT);

        final JButton start = this.buttonFactory.buildbutton("start");
        final JButton options = this.buttonFactory.buildbutton("opzioni");
        final JButton shop = this.buttonFactory.buildbutton("shop");

        start.addActionListener(e -> controller.handleEvent(State.CHOOSE_LEVEL, Optional.empty()));
        options.addActionListener(e -> controller.handleEvent(State.OPTIONS, Optional.empty()));
        shop.addActionListener(e -> controller.handleEvent(State.SHOP, Optional.empty()));

        start.setAlignmentX(CENTER_ALIGNMENT);
        options.setAlignmentX(CENTER_ALIGNMENT);
        shop.setAlignmentX(CENTER_ALIGNMENT);

        this.add(title);
        this.add(start);
        this.add(options);
        this.add(shop);

        this.image = tryLoad("/img/Piattaforma_retro_con_personaggio_pixelato.png");
        if (this.image == null) {
            this.image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        }
    }

    /**
     * load immage.
     *
     * @param path non so
     * @return return the immage
     */
    private BufferedImage tryLoad(final String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is != null) {
                return ImageIO.read(is);
            }
        } catch (final IOException ignored) {
            //non faccio nulla
        }
        return null;
    }

    /**
     * cancella e ridipinge il pannello.
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
         g.drawImage(this.image, 0, 0, getWidth(), getHeight(), this);
    }
}
