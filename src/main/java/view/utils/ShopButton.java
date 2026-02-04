package view.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controller.api.ControllerMenu;
import model.CoinStorage;
import model.objects.CollectableManager;

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
    private static final Color MAIN_COLOR = new Color(144, 238, 144);
    private static final int SKIN_COST = 20;
    private static final String SKIN_COST_STRING = "20$";

    private final GridLayout grid;
    private final Image background;
    private final JButton[] allButtons;

    final JButton skinDefault = ShopButtonFactory.build("/img/Player_1_frame_1.png");
    final JButton skinShark = ShopButtonFactory.build("/img/squalo_frame_1.png");
    final JButton skinPurple = ShopButtonFactory.build("/img/purple_player_frame_1.png");
    final JButton skinGhost = ShopButtonFactory.build("/img/ghost_frame_1.png");

    private final Set<JButton> purchasedButtons = new HashSet<>(Set.of(skinDefault));
    private final Set<JButton> toBuyButtons =  new HashSet<>(Set.of(skinGhost, skinPurple, skinShark));

    /**
     * Creates the shop button panel.
     *
     * @param controller the menu controller used to select skins
     */
    public ShopButton(final ControllerMenu controller) {

        this.allButtons = new JButton[] { skinDefault, skinShark, skinPurple, skinGhost };

        this.background = CreateBackground.create("/img/Shopback.png");

        this.grid = new GridLayout(ROWS, COLS, 0, 0);
        setLayout(this.grid);

        skinDefault.setText("DEFAULT");
        skinShark.setText(SKIN_COST_STRING);
        skinPurple.setText(SKIN_COST_STRING);
        skinGhost.setText(SKIN_COST_STRING);

        skinDefault.addActionListener(e -> onSkinButtonClick(skinDefault, controller,
                "img/Player_1_frame_1.png", "img/Player_1_frame_2.png"));

        skinShark.addActionListener(e -> onSkinButtonClick(skinShark, controller,
                "img/squalo_frame_1.png", "img/squalo_frame_2.png"));

        skinPurple.addActionListener(e -> onSkinButtonClick(skinPurple, controller,
                "img/purple_player_frame_1.png", "img/purple_player_frame_2.png"));

        skinGhost.addActionListener(e -> onSkinButtonClick(skinGhost, controller,
                "img/ghost_frame_1.png", "img/ghost_frame_2.png"));

        add(skinDefault);
        add(skinPurple);
        add(skinShark);
        add(skinGhost);

       PaintButton(allButtons);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doLayout() {
        final int w = getWidth();
        final int h = getHeight();

        final int padX = clamp((int) (w * PAD_PERCENTUALE), PAD_MIN, PAD_MAX);
        final int padY = clamp((int) (h * PAD_PERCENTUALE), PAD_MIN, PAD_MAX);

        final int gapX = clamp((int) (w * GAP_PERCENTUALE), GAP_MIN, GAP_MAX);
        final int gapY = clamp((int) (h * GAP_PERCENTUALE), GAP_MIN, GAP_MAX);

        setBorder(BorderFactory.createEmptyBorder(padY, padX, padY, padX));
        this.grid.setHgap(gapX);
        this.grid.setVgap(gapY);

        super.doLayout();
    }

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
    private void PaintButton( final JButton[] all) {
        for (final JButton b : all) {
            if (purchasedButtons.contains(b)) {
            b.setBackground(DEFAULT_COLOR);
            } else {
                b.setBackground(SELECTED_COLOR);
            }
        }
    }

    /**
     * selects a button and updates the colors of all buttons.
     *
     * @param selected the selected button
     * @param all all available buttons
     */
    private void selectButton(final JButton selected, final JButton[] all) {
        for (final JButton b : all) {
            if (purchasedButtons.contains(b)) {
            b.setBackground(DEFAULT_COLOR);
            } else {
                b.setBackground(SELECTED_COLOR);
            }
        }
        selected.setBackground(MAIN_COLOR);
    }

    private void onSkinButtonClick(final JButton btn, final ControllerMenu controller, final String f1, final String f2) {
        if (toBuyButtons.contains(btn)) {
            buySkinForOneGame(btn, controller, f1, f2);
        } else {
            controller.selectSkin(f1, f2);
            selectButton(btn, allButtons);
        }
    }

    private void buySkinForOneGame(final JButton btn, final ControllerMenu controller, final String f1, final String f2) {
        if (CollectableManager.getCoins() >= SKIN_COST) {
            CoinStorage.addCoins(-SKIN_COST);

            toBuyButtons.remove(btn);
            purchasedButtons.add(btn);

            controller.selectSkin(f1, f2);
            selectButton(btn, allButtons);
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Fondi insufficienti, ti mancano " + (SKIN_COST - CollectableManager.getCoins()),
                "Shop",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }
}
