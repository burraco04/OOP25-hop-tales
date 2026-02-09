package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import controller.api.ControllerObserver;
import controller.impl.CollectablesController;
import controller.impl.EnemyController;
import controller.impl.PlayerController;
import deserialization.level.EnemyFactory;
import deserialization.level.EntityData;
import deserialization.level.EntityFactory;
import deserialization.level.LevelData;
import deserialization.level.LevelLoader;
import model.GameConstants;
import model.World;
import view.api.View;

/**
 * Coordinates every {@link ControllerObserver}, calling their methods whenever it's time for an update.
 */
public final class GameController implements ActionListener {

    private final PlayerController playerController;
    private final CollectablesController collectablesController;
    private final EnemyController enemyController;
    private final Timer timer;
    private final View view;
    private final World world;
    private boolean gameOver;
    private boolean levelCompleted;

    /**
     * Creates a controller.
     *
     * @param view the {@link View} that is responsible for the game.
     * @param levelId number representing the level selected.
     * @param levelPath path of the level choose by the.
     */
    public GameController(final View view, final int levelId, final String levelPath) {
        this.view = view;
        this.world = new World(levelId);
        this.playerController = new PlayerController(this.world);
        final KeyboardInputManager kim = new KeyboardInputManager(playerController);
        this.collectablesController = new CollectablesController(this.world);
        this.enemyController = new EnemyController(this.world);
        this.timer = new Timer(GameConstants.MILLIS_PER_SECOND / GameConstants.TARGET_UPS, this);
        this.start();
        this.view.showLevel(this.world, kim);

        final LevelData data = LevelLoader.load(levelPath);

        world.getPlayer().setX(data.getSpawnPointX());
        world.getPlayer().setY(data.getSpawnPointY());

        for (final EntityData e : data.getEntities()) {
           world.addEntities(EntityFactory.create(e));
        }

        for (final EntityData e : data.getEnemies()) {
           world.addEnemy(EnemyFactory.createEnemy(e));
        }
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
        collectablesController.update();
        enemyController.update();
        if (!gameOver && !world.getPlayer().isAlive()) {
            gameOver = true;
            stop();
            view.showGameOver();
            return;
        }
        if (!levelCompleted && playerController.isLevelCompleted()) {
            levelCompleted = true;
            stop();
            view.showLevelCompleted();
        }
    }

    /**
     * Expose the world for test purposes.
     *
     * @return the current world
     */
    World getWorldForTest() {
        return world;
    }

    /**
     * Expose the player controller for test purposes.
     *
     * @return the player controller
     */
    PlayerController getPlayerControllerForTest() {
        return playerController;
    }
}
