package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import model.Model;
import view.View;

/**
 * Coordinates the interaction between the game model, view, and user input.
 */
public final class Controller extends KeyAdapter implements ActionListener {
    private static final int TARGET_FPS = 60;
    private static final int MILLIS_PER_SECOND = 1000;
    private static final double NANOS_PER_SECOND = 1_000_000_000.0;
    private static final int EXIT_SUCCESS = 0;
    private final Model model;
    private final View view;
    private final Timer timer;
    private long lastUpdateTime;
    private boolean gameOverNotified;

    /**
     * Creates a controller that updates the given model and view.
     *
     * @param model the game state container to update
     * @param view  the view that renders the game state
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "MVC components share references intentionally")
    public Controller(final Model model, final View view) {
        this.model = model;
        this.view = view;
        this.timer = new Timer(MILLIS_PER_SECOND / TARGET_FPS, this);
    }

    /**
     * Starts the main game loop timer.
     */
    public void start() {
        this.lastUpdateTime = System.nanoTime();
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
        final long now = System.nanoTime();
        final double deltaSeconds = (now - lastUpdateTime) / NANOS_PER_SECOND;
        lastUpdateTime = now;
        model.update(deltaSeconds);
        final Model.GameState state = model.snapshot();
        view.setGameState(state);
        if (state.isGameOver() && !gameOverNotified) {
            gameOverNotified = true;
            stop();
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(
                    view,
                    "Game over! You ran out of health.\nCoins collected: " + state.getCoinsCollected(),
                    "Hop Tales",
                    JOptionPane.INFORMATION_MESSAGE
                );
                terminateApplication();
            });
        }
    }

    /**
     * Handles key press events for player input.
     *
     * @param event key event
     */
    @Override
    public void keyPressed(final KeyEvent event) {
        handleKeyEvent(event, true);
    }

    /**
     * Handles key release events for player input.
     *
     * @param event key event
     */
    @Override
    public void keyReleased(final KeyEvent event) {
        handleKeyEvent(event, false);
    }

    /**
     * Routes a key event to the appropriate model action.
     *
     * @param event key event
     * @param pressed {@code true} when the key is pressed
     */
    private void handleKeyEvent(final KeyEvent event, final boolean pressed) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                model.setLeftPressed(pressed);
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                model.setRightPressed(pressed);
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
            case KeyEvent.VK_SPACE:
                if (pressed) {
                    model.queueJump();
                }
                break;
            default:
                break;
        }
    }

    /**
     * Terminates the application process.
     */
    @SuppressFBWarnings(value = "DM_EXIT", justification = "The application should terminate when health reaches zero")
    private static void terminateApplication() {
        System.exit(EXIT_SUCCESS);
    }
}
