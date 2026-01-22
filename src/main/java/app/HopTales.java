package app;

import java.util.Optional;

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
     * avvio gioco.
     *
     * @param args  start argomenti riga comando
     */
    public static void main(final String[] args) {

        final SwingView view = new SwingView();
        final ControllerMenu controller = new ControllerMenuImpl(view);
        view.setController(controller);
        controller.handleEvent(State.MAIN_MENU, Optional.empty());
    }
}
