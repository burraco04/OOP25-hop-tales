package view.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import controller.api.ControllerMenu;
import model.CoinStorage;
import model.GameConstants;
import model.ShopModel;
import model.ShopModel.SkinId;

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
    private static final Color SELECTED_COLOR = new Color(205, 170, 125);
    private static final Color DEFAULT_COLOR = new Color(208, 208, 208);
    private static final Color MAIN_COLOR = new Color(144, 238, 144);
    private static final String SKIN_COST_STRING = "20$";
    private final transient ShopModel shopModel;

    private final GridLayout grid;
    private final transient Image background;
    private final JButton[] allButtons;
    private final transient ControllerMenu controller;

    /**
     * Creates the shop button panel.
     *
     * @param menuController the menu controller used to select skins
     * @param shopModel the model used to save the skin you buy
     */
    public ShopButton(final ControllerMenu menuController, final ShopModel shopModel) {
     this.controller = menuController;
     this.shopModel = shopModel;

    this.allButtons = new JButton[] {
        GameConstants.SKINDEFAULT,
        GameConstants.SKINSHARK,
        GameConstants.SKINPURPLE,
        GameConstants.SKINGHOST,
    };

    this.background = CreateBackground.create("/img/Shopback.png");
    this.grid = new GridLayout(ROWS, COLS, 0, 0);

    SwingUtilities.invokeLater(this::initUI);
    }

    private void initUI() {
    setLayout(this.grid);

    GameConstants.SKINDEFAULT.setText("DEFAULT");
    GameConstants.SKINSHARK.setText(SKIN_COST_STRING);
    GameConstants.SKINPURPLE.setText(SKIN_COST_STRING);
    GameConstants.SKINGHOST.setText(SKIN_COST_STRING);

    GameConstants.SKINDEFAULT.addActionListener(e ->
        onSkinButtonClick(SkinId.DEFAULT, GameConstants.SKINDEFAULT,
            "img/Player_1_frame_1.png", "img/Player_1_frame_2.png"));

    GameConstants.SKINSHARK.addActionListener(e ->
        onSkinButtonClick(SkinId.SHARK, GameConstants.SKINSHARK,
            "img/squalo_frame_1.png", "img/squalo_frame_2.png"));

    GameConstants.SKINPURPLE.addActionListener(e ->
        onSkinButtonClick(SkinId.PURPLE, GameConstants.SKINPURPLE,
            "img/purple_player_frame_1.png", "img/purple_player_frame_2.png"));

    GameConstants.SKINGHOST.addActionListener(e ->
        onSkinButtonClick(SkinId.GHOST, GameConstants.SKINGHOST,
            "img/ghost_frame_1.png", "img/ghost_frame_2.png"));

    add(GameConstants.SKINDEFAULT);
    add(GameConstants.SKINPURPLE);
    add(GameConstants.SKINSHARK);
    add(GameConstants.SKINGHOST);

    paintButton(this.allButtons);

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
     * @param all all available buttons
     */
    private void paintButton(final JButton[] all) {
        for (final JButton b : all) {
            final SkinId id = idOf(b);
            if (shopModel.isPurchased(id)) {
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
        final SkinId id = idOf(b);
        if (shopModel.isPurchased(id)) {
            b.setBackground(DEFAULT_COLOR);
        } else {
            b.setBackground(SELECTED_COLOR);
        }
        }
        selected.setBackground(MAIN_COLOR);
    }

    private SkinId idOf(final JButton b) {
    if (b == GameConstants.SKINDEFAULT) {
        return SkinId.DEFAULT;
    }
    if (b == GameConstants.SKINSHARK) {
        return SkinId.SHARK;
    }
    if (b == GameConstants.SKINPURPLE) {
        return SkinId.PURPLE;
    }
    return SkinId.GHOST;
    }

    private void onSkinButtonClick(final SkinId id, final JButton btn, final String f1, final String f2) {
        if (shopModel.isToBuy(id)) {
        buySkinForOneGame(id, btn, f1, f2);
        } else {
            this.controller.selectSkin(f1, f2);
            selectButton(btn, allButtons);
        }
    }

    private void buySkinForOneGame(final SkinId id, final JButton btn, final String f1, final String f2) {
        if (CoinStorage.getCoins() >= GameConstants.SKIN_COST) {
            CoinStorage.paySkinFromShop();

            shopModel.markPurchased(id);

            this.controller.selectSkin(f1, f2);
            selectButton(btn, allButtons);
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Insufficient funds, you're missing " + (GameConstants.SKIN_COST - CoinStorage.getCoins()),
                "Shop",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }
}
