package controller.api;

import java.util.Optional;

/**
 * implementetion of controller.
 */
@FunctionalInterface
public interface ControllerMenu {
    /**
     * control event.
     *
     * @param e event
     * @param data list of event
     */
    void handleEvent(State e, Optional<?> data);

    /**
     * Seleziona una skin del giocatore.
     * Default no-op per non rompere altre implementazioni.
     *
     * @param frame1 prima immagine della skin
     * @param frame2 seconda immagine della skin (può coincidere con frame1)
     */
    default void selectSkin(final String frame1, final String frame2) {
        // default implementation
    }

}
