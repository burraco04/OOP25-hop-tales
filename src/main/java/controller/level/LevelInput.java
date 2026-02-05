package controller.level;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import model.level.LevelModel;
import view.impl.FireboyWatergirlLevel;

public final class LevelInput {

    final Set<Integer> keysDown = new HashSet<>();
    boolean fireboyJumpQueued = false;
    boolean watergirlJumpQueued = false;
    private final Runnable onHome;

    public LevelInput(Runnable onHome) {
        this.onHome = onHome;
    }

    public void keyPressed(FireboyWatergirlLevel panel, LevelModel m, KeyEvent e) {
        int k = e.getKeyCode();
        keysDown.add(k);

        if (k == KeyEvent.VK_UP) fireboyJumpQueued = true;
        if (k == KeyEvent.VK_W) watergirlJumpQueued = true;

        if (k == KeyEvent.VK_R) {
            if (m.isGameOver() || m.isLevelComplete()) panel.restartLevel();
        }

        if (k == KeyEvent.VK_H) {
            if (m.isGameOver() || m.isLevelComplete()) {
                if (onHome != null) {
                    panel.goHome();
                } else {
                    System.out.println("HOME (da collegare al tuo menu)");
                }
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        keysDown.remove(e.getKeyCode());
    }

    public void reset() {
        keysDown.clear();
        fireboyJumpQueued = false;
        watergirlJumpQueued = false;
    }
}
