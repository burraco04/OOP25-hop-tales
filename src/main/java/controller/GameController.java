package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

import controller.api.ControllerObserver;
import controller.impl.CoinsController;
import controller.impl.PlayerController;
import model.entities.api.Player;
import model.entities.impl.PlayerImpl;

/**
 * Coordinates every {@link ControllerObserver}, calling their methods whenever it's time for an update.
 */
public final class GameController implements ActionListener {
    private static final int TARGET_UPS = 60;
    private static final int MILLIS_PER_SECOND = 1000;
    private static final int STARTING_POSITION = 0;
    private static final int HEIGHT = 420;
    private static final int WIDTH = 240;
    private final PlayerController playerController;
    private final KeyboardInputManager kim;
    private final CoinsController coinsController;
    private final Player player;
    private final Timer timer;

    /**
     * Creates a controller.
     */
    public GameController() {
        this.player = new PlayerImpl(STARTING_POSITION, STARTING_POSITION, WIDTH, HEIGHT);
        this.playerController = new PlayerController(player);
        this.kim = new KeyboardInputManager(playerController);
        this.coinsController = new CoinsController(player);
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
        coinsController.update();
    }
}
