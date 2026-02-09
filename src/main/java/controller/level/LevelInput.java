package controller.level;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import model.level.LevelModel;
import view.impl.FireboyWatergirlLevel;

/**
 * Handles input for the third level.
 */
public final class LevelInput {

    private final Set<Integer> keysDown = new HashSet<>();
    private boolean fireboyJumpQueued;
    private boolean watergirlJumpQueued;
    private final Runnable onHome;

    /**
     * Creates a new input handler.
     *
     * @param onHome action to invoke when returning to the main menu
     */
    public LevelInput(final Runnable onHome) {
        this.onHome = onHome;
    }

    /**
     * Handles key press events.
     *
     * @param panel level panel
     * @param model level model
     * @param event key event
     */
    public void keyPressed(final FireboyWatergirlLevel panel, final LevelModel model, final KeyEvent event) {
        final int keyCode = event.getKeyCode();
        keysDown.add(keyCode);

        if (keyCode == KeyEvent.VK_UP) {
            fireboyJumpQueued = true;
        }
        if (keyCode == KeyEvent.VK_W) {
            watergirlJumpQueued = true;
        }

        if (keyCode == KeyEvent.VK_R && (model.isGameOver() || model.isLevelComplete())) {
            panel.restartLevel();
        }

        if (keyCode == KeyEvent.VK_H && (model.isGameOver() || model.isLevelComplete()) && onHome != null) {
            panel.goHome();
        }
    }

    /**
     * Handles key release events.
     *
     * @param event key event
     */
    public void keyReleased(final KeyEvent event) {
        keysDown.remove(event.getKeyCode());
    }

    /**
     * @param keyCode key code to check
     * @return true if the key is currently pressed
     */
    public boolean isKeyDown(final int keyCode) {
        return keysDown.contains(keyCode);
    }

    /**
     * @return true if fireboy has a queued jump
     */
    public boolean isFireboyJumpQueued() {
        return fireboyJumpQueued;
    }

    /**
     * Clears the fireboy jump queue.
     */
    public void consumeFireboyJump() {
        fireboyJumpQueued = false;
    }

    /**
     * @return true if watergirl has a queued jump
     */
    public boolean isWatergirlJumpQueued() {
        return watergirlJumpQueued;
    }

    /**
     * Clears the watergirl jump queue.
     */
    public void consumeWatergirlJump() {
        watergirlJumpQueued = false;
    }

    /**
     * Resets input state.
     */
    public void reset() {
        keysDown.clear();
        fireboyJumpQueued = false;
        watergirlJumpQueued = false;
    }
}
