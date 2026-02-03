package app;

import controller.api.ControllerMenu;
import controller.api.State;
import controller.impl.ControllerMenuImpl;
import view.impl.SwingView;

/**
 * start.
 */
public final class HopTales {

    private HopTales() {
        // Impedisce l'istanziazione.
    }

    /**
     * Initializes the main view and controller, and starts the game.
     *
     * @param args  start 
     */
    public static void main(final String[] args) {

        final SwingView view = new SwingView();
        final ControllerMenu controller = new ControllerMenuImpl(view);
        view.setController(controller);
        controller.handleEvent(State.MAIN_MENU);
    }
}
