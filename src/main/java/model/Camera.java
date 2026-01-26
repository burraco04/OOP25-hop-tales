package model;

public class Camera {
    private int x;
    private int maxX;
    private int deadZonePx;
    private final int levelWidthPx;

    public Camera(final int levelWidthPx, final int screenWidthPx) {
        this.levelWidthPx = levelWidthPx;
        updateBounds(screenWidthPx);
    }

    public int getX() {
        return x;
    }

    /**
     * Camera orizzontale che segue il player quando supera la dead zone.
     */
    public void update(final int playerWorldX, final int screenWidthPx) {
        updateBounds(screenWidthPx);

        final int leftBound = x + deadZonePx;
        final int rightBound = x + screenWidthPx - deadZonePx;

        //Player troppo a destra
        if (playerWorldX > rightBound) {
            x += playerWorldX - rightBound;
        }
        //Player troppo a sinistra 
        else if (playerWorldX < leftBound) {
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

    private void updateBounds(final int screenWidthPx) {
        this.maxX = Math.max(0, levelWidthPx - screenWidthPx);
        this.deadZonePx = screenWidthPx / 4;
    }
}
