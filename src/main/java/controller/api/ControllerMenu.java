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

}
