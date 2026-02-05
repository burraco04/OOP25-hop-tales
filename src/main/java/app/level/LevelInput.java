package app.level;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public final class LevelInput {

    final Set<Integer> keysDown = new HashSet<>();
    boolean fireboyJumpQueued = false;
    boolean watergirlJumpQueued = false;
    private final Runnable onHome;

    LevelInput(Runnable onHome) {
        this.onHome = onHome;
    }

    void keyPressed(FireboyWatergirlLevel panel, LevelModel m, KeyEvent e) {
        int k = e.getKeyCode();
        keysDown.add(k);

        if (k == KeyEvent.VK_UP) fireboyJumpQueued = true;
        if (k == KeyEvent.VK_W) watergirlJumpQueued = true;

        if (k == KeyEvent.VK_R) {
            if (m.gameOver || m.levelComplete) panel.restartLevel();
        }

        if (k == KeyEvent.VK_H) {
            if (m.gameOver || m.levelComplete) {
                if (onHome != null) {
                    panel.goHome();
                } else {
                    System.out.println("HOME (da collegare al tuo menu)");
                }
            }
        }
    }

    void keyReleased(KeyEvent e) {
        keysDown.remove(e.getKeyCode());
    }

    void reset() {
        keysDown.clear();
        fireboyJumpQueued = false;
        watergirlJumpQueued = false;
    }
}
