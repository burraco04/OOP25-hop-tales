package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import controller.api.ControllerObserver;
import controller.impl.CollectablesController;
import controller.impl.EnemyController;
import controller.impl.PlayerController;
import model.GameConstants;
import model.World;
import view.api.View;

/**
 * Coordinates every {@link ControllerObserver}, calling their methods whenever it's time for an update.
 */
public final class GameController implements ActionListener {

    private final PlayerController playerController;
    @SuppressWarnings("unused")
    private final KeyboardInputManager kim;
    private final CollectablesController coinsController;
    private final EnemyController enemyController;
    private final Timer timer;
    private final View view;
    private final World world;
    private boolean gameOver;

    /**
     * Creates a controller.
     * 
     * @param view the {@link View} that is responsible for the game.
     */
    public GameController(final View view, final int levelId) {
        this.view = view;
        this.world = new World(levelId);
        this.playerController = new PlayerController(this.world);
        this.kim = new KeyboardInputManager(playerController);
        this.coinsController = new CollectablesController(this.world);
        this.enemyController = new EnemyController(this.world);
        this.timer = new Timer((int) (GameConstants.MILLIS_PER_SECOND / GameConstants.TARGET_UPS), this);
        this.start();
        this.view.showLevel(this.world, this.kim);
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
        enemyController.update();
        if (!gameOver && !world.getPlayer().isAlive()) {
            gameOver = true;
            stop();
            view.showGameOver();
        }
    }
}
