package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

import controller.impl.PlayerController;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Coordinates every {@link ControllerObserver}, calling their methods whenever it's time for an update.
 */
public final class GameController implements ActionListener {
    private static final int TARGET_UPS = 60;
    private static final int MILLIS_PER_SECOND = 1000;
    private final PlayerController playerController;
    private final Timer timer;

    /**
     * Creates a controller.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "MVC components share references intentionally")
    public GameController() {
        this.playerController = new PlayerController();
        this.timer = new Timer(MILLIS_PER_SECOND / TARGET_UPS, this); 
    }

    /**
     * Starts the main game loop timer.
     */
    public void start() {
        this.timer.start();
    }

    /**
     * Stops the main game loop timer.
     */
    public void stop() {
        this.timer.stop();
    }

    /**
     * Advances the game simulation on each timer tick.
     *
     * @param event timer event
     */
    @Override
    public void actionPerformed(final ActionEvent event) {
        playerController.update();
    }
}
