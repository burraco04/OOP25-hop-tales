package model;

public class Camera {
    private int x;
    private int screenWidth;
    private int maxX;
    private int marginX;
    private final int levelWidth;

    public Camera(final int levelWidth, final int screenWidth) {
        this.levelWidth = levelWidth;
        this.maxX = levelWidth - screenWidth;
        this.marginX = screenWidth / 4;
    }

    public int getX() {
        return x;
    }

    /**
     * Camera orizzontale che segue il player quando supera marginX
     */
    public void update(int playerX, int screenWidth) {
        this.maxX = levelWidth - screenWidth;
        this.marginX = screenWidth / 4;
        
        //Player troppo a destra
        if (playerX - x > screenWidth - marginX) {
            x = playerX - (screenWidth - marginX);
        }
        //Player troppo a sinistra 
        else if (playerX - x < marginX) {
            x = playerX - marginX;
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
}