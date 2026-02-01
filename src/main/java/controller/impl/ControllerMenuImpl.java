package controller.impl;

import java.util.Optional;

import controller.GameController;
import controller.api.ControllerMenu;
import controller.api.State;
import view.api.View;

/**
 * controller. 
 */

public class ControllerMenuImpl implements ControllerMenu {

    private final View view;

    /**
     * controlller ciao ciao.
     *
     * @param view active view
     */
    public ControllerMenuImpl(final View view) {
        this.view = view;
    }

    /**
     * valuta che tipo di evento è e chiama la view.
     */

    @Override
    public void handleEvent(final State e, final Optional<?> data) {
        switch (e) {
        case MAIN_MENU -> view.showMainMenu();

        case CHOOSE_LEVEL -> view.showLevels();

        case SHOP -> view.showShop();

        case OPTIONS -> view.showOptions();

        case LEVEL_1 -> new GameController(this.view, 1);

        case LEVEL_2 -> new GameController(this.view, 2);
     }
    }
}
