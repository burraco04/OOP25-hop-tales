package view.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import controller.api.ControllerMenu;

/**
 * Panel used to buy and select the skins.
 */
public class ShopButton extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int ROWS = 2;
    private static final int COLS = 2;
    private static final int PAD_MIN = 20;
    private static final int PAD_MAX = 200;
    private static final float PAD_PERCENTUALE = 0.08f;
    private static final int GAP_MIN = 10;
    private static final int GAP_MAX = 200;
    private static final float GAP_PERCENTUALE = 0.20f;
    private static final Color DEFAULT_COLOR = new Color(205, 170, 125);
    private static final Color SELECTED_COLOR = new Color(208, 208, 208);
    private final GridLayout grid;
    private final Image background;
    private final JButton[] allButtons;

    /**
     * Creates the shop button panel.
     *
     * @param controller the menu controller used to select skins
     */
    public ShopButton(final ControllerMenu controller) {

        this.background = CreateBackground.create("/img/Shopback.png");

        this.grid = new GridLayout(ROWS, COLS, 0, 0);
        setLayout(this.grid);

        final JButton skinDefault = ShopButtonFactory.build("/img/Player_1_frame_1.png");
        final JButton skinRobot = ShopButtonFactory.build("/img/skinsqualo.png");
        final JButton skinShark = ShopButtonFactory.build("/img/skinsqualo.png");
        final JButton skinCat = ShopButtonFactory.build("/img/skinsqualo.png");

        this.allButtons = new JButton[] {skinDefault, skinRobot, skinShark, skinCat};

        skinDefault.setText("DEFAULT");
        skinRobot.setText("ROBOT");
        skinShark.setText("SQUALO");
        skinCat.setText("CANE");

        skinDefault.addActionListener(e -> {
            controller.selectSkin("img/Player_1_frame_1.png", "img/Player_1_frame_2.png");
            selectButton(skinDefault, allButtons);
        });
        skinRobot.addActionListener(e -> {
            controller.selectSkin("img/skinsqualo.png", "img/skinsqualo.png");
            selectButton(skinRobot, allButtons);
        });
        skinShark.addActionListener(e -> {
            controller.selectSkin("img/skinsqualo.png", "img/skinsqualo.png");
            selectButton(skinShark, allButtons);
        });
        skinCat.addActionListener(e -> {
            controller.selectSkin("img/skinsqualo.png", "img/skinsqualo.png");
            selectButton(skinCat, allButtons);
        });

        add(skinDefault);
        add(skinRobot);
        add(skinShark);
        add(skinCat);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doLayout() {
        // Dimensioni disponibili
        final int w = getWidth();
        final int h = getHeight();

        // Padding proporzionale 
        final int padX = clamp((int) (w * PAD_PERCENTUALE), PAD_MIN, PAD_MAX);
        final int padY = clamp((int) (h * PAD_PERCENTUALE), PAD_MIN, PAD_MAX);

        // Gap proporzionale 
        final int gapX = clamp((int) (w * GAP_PERCENTUALE), GAP_MIN, GAP_MAX);
        final int gapY = clamp((int) (h * GAP_PERCENTUALE), GAP_MIN, GAP_MAX);

        setBorder(BorderFactory.createEmptyBorder(padY, padX, padY, padX));
        this.grid.setHgap(gapX);
        this.grid.setVgap(gapY);

        super.doLayout();
    }

    /**
     * Clamps a value between a minimum and a maximum.
     *
     * @param v the input value
     * @param min the minimum allowed value
     * @param max the maximum allowed value
     * @return the clamped value
     */
    private static int clamp(final int v, final int min, final int max) {
        return Math.max(min, Math.min(max, v));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
         g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }

    /**
     * selects a button and updates the colors of all buttons.
     *
     * @param selected the selected button
     * @param all all available buttons
     */
    private void selectButton(final JButton selected, final JButton[] all) {
    for (final JButton b : all) {
        b.setBackground(DEFAULT_COLOR);
    }
    selected.setBackground(SELECTED_COLOR);
    }
}
