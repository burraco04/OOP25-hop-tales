package controller.api;

public interface ControllerObserver {
    /**
     * Used for executing every controller that implements {@link ControllerObserver}'s duties
     */
    void update();
}
