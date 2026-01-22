package model;

public class Camera {
    private int x;

    public int getX() {
        return x;
    }

    /**
     * Camera orizzontale che segue il player quando supera marginX
     */
    public void update(int playerX, int screenWidth,  int levelWidth) {
        final int marginX = 200;

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
        final int maxX = levelWidth - screenWidth;
        if (x > maxX) {
            x = maxX;
        }

    }
}