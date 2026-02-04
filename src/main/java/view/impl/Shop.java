package view.impl;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.api.ControllerMenu;
import controller.api.State;
import model.CoinStorage;
import model.GameConstants;
import view.utils.ButtonBackFactory;
import view.utils.FontFactory;
import view.utils.ShopButton;
import view.utils.TopBarPanel;

/**
 * This class represents the shop menu of the game, where the player can buy and select available skins.
 */

public final class Shop extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final String TITLE_FONT = "SuperShiny";
    private static final float TITLE_SIZE = 100f;

    private final transient ButtonBackFactory buttonbackFactory = new ButtonBackFactory();
    private final TopBarPanel topBarpan = new TopBarPanel();
    private final transient FontFactory font = new FontFactory();

    /**
     *  Creates the Shop panel.
     *
     * @param controller pass the menu controller used to handle user interactions and navigation
     */
    public Shop(final ControllerMenu controller) {
    CoinStorage.loadTotalCoins();
    final JLabel title = new JLabel("penaldooo");
    title.setFont(this.font.getFont(TITLE_FONT, TITLE_SIZE, this));

    setBackground(GameConstants.BACK_COLOR);
    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

    final JButton back = this.buttonbackFactory.buildbackbutton();
    back.addActionListener(e -> controller.handleEvent(State.MAIN_MENU));
    final JPanel topBar = topBarpan.buildTopPanel(back);
    final JPanel panel = new ShopButton(controller, controller.getShopModel());

    this.add(topBar);
    this.add(title);
    this.add(panel);

    }

}
