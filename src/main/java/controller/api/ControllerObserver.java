package controller.api;

/**
 * An interface used to simulate the Observer pattern in the implementation of the Controller part of the MVC pattern.
 */
@FunctionalInterface
public interface ControllerObserver {
    /**
     * Used for executing every controller that implements {@link ControllerObserver}'s duties.
     */
    void update();
}
