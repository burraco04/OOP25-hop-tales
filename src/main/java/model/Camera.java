package model;

/**
 * Class responsible for handling the camera movements during the game phase.
 */
public class Camera {
    private int x;
    private int maxX;
    private int deadZonePx;
    private final int levelWidthPx;

    /**
     * Create a camera object.
     * 
     * @param levelWidthPx width of the current level.
     * @param screenWidthPx width of the screen.
     */
    public Camera(final int levelWidthPx, final int screenWidthPx) {
        this.levelWidthPx = levelWidthPx;
        updateBounds(screenWidthPx);
    }

    /**
     * Get the camera offset value.
     *
     * @return camera offset.
     */
    public int getX() {
        return x;
    }

    /**
     * Updates if necessary the value of the camera offset.
     * 
     * @param playerWorldX current x value of the player.
     * @param screenWidthPx current screen width.
     */
    public void update(final int playerWorldX, final int screenWidthPx) {
        updateBounds(screenWidthPx);

        final int leftBound = x + deadZonePx;
        final int rightBound = x + screenWidthPx - deadZonePx;

        //Player troppo a destra
        if (playerWorldX > rightBound) {
            x += playerWorldX - rightBound;
        } else if (playerWorldX < leftBound) {
            x -= leftBound - playerWorldX;
        }

        // limite sinistro
        if (x < 0) {
            x = 0;
        }

        // limite destro
        if (x > maxX) {
            x = maxX;
        }

    }

    /**
     * Updates the bounds based on the screen width.
     *
     * @param screenWidthPx the screen width.
     */
    private void updateBounds(final int screenWidthPx) {
        this.maxX = Math.max(0, levelWidthPx - screenWidthPx);
        this.deadZonePx = screenWidthPx / 4;
    }
}
