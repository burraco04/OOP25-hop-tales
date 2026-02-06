package controller.impl;

import controller.GameController;
import controller.api.ControllerMenu;
import controller.api.State;
import model.ShopModel;
import view.utils.Draw;
import view.api.View;

/**
 * Implementation of the ControllerMenu interface.
 */
public class ControllerMenuImpl implements ControllerMenu {

    private final View view;
    private final ShopModel shopModel = new ShopModel();

    /**
     * Constructor for the class.
     *
     * @param view the active view used 
     */
    public ControllerMenuImpl(final View view) {
        this.view = view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleEvent(final State e) {
        switch (e) {
        case MAIN_MENU -> view.showMainMenu();

        case CHOOSE_LEVEL -> view.showLevels();

        case SHOP -> view.showShop();

        case OPTIONS -> view.showOptions();

        case LEVEL_1 -> new GameController(this.view, 1, "levels/Level1.json");

        case LEVEL_2 -> new GameController(this.view, 2, "levels/Level2.json");
     }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void selectSkin(final String frame1, final String frame2) {
        Draw.setPlayerSkin(frame1, frame2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ShopModel getShopModel() {
        return this.shopModel;
    }
}
