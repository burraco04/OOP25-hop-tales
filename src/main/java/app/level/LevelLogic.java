package app.level;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.Comparator;

public final class LevelLogic {

    private LevelLogic() {}

    public static void tick(FireboyWatergirlLevel panel, LevelModel m, LevelInput input) {
        if (m.gameOver || m.levelComplete) return;

        // input continuo
        if (input.keysDown.contains(KeyEvent.VK_LEFT) && !input.keysDown.contains(KeyEvent.VK_RIGHT)) {
            m.fireboy.vx = -3;
        } else if (input.keysDown.contains(KeyEvent.VK_RIGHT) && !input.keysDown.contains(KeyEvent.VK_LEFT)) {
            m.fireboy.vx = 3;
        } else {
            m.fireboy.vx = 0;
        }

        if (input.keysDown.contains(KeyEvent.VK_A) && !input.keysDown.contains(KeyEvent.VK_D)) {
            m.watergirl.vx = -3;
        } else if (input.keysDown.contains(KeyEvent.VK_D) && !input.keysDown.contains(KeyEvent.VK_A)) {
            m.watergirl.vx = 3;
        } else {
            m.watergirl.vx = 0;
        }

        if (input.fireboyJumpQueued) {
            m.fireboy.jump();
            input.fireboyJumpQueued = false;
        }
        if (input.watergirlJumpQueued) {
            m.watergirl.jump();
            input.watergirlJumpQueued = false;
        }

        // push massi
        for (model.objects.impl.Boulder b : m.boulders) {
            b.tryPushBy(m.fireboy, panel);
            b.tryPushBy(m.watergirl, panel);
        }

        // update player
        m.fireboy.update(panel);
        m.watergirl.update(panel);

        // fisica massi
        for (model.objects.impl.Boulder b : m.boulders) b.updatePhysics(panel);

        // schiacciamento
        for (model.objects.impl.Boulder b : m.boulders) {
            if (b.vy > 0) {
                if (isCrushedByBoulder(panel, m.fireboy, b) || isCrushedByBoulder(panel, m.watergirl, b)) {
                    m.gameOver = true;
                    break;
                }
            }
        }

        // lava
        if (LevelQueries.touchesLava(m, m.fireboy) || LevelQueries.touchesLava(m, m.watergirl)) m.gameOver = true;

        // monete
        LevelInteractions.collectCoins(m, m.fireboy);
        LevelInteractions.collectCoins(m, m.watergirl);

        // bottoni
        LevelInteractions.handleButtons(m, m.fireboy);
        LevelInteractions.handleButtons(m, m.watergirl);

        // teleport
        LevelInteractions.handleTeleport(m, m.fireboy);
        LevelInteractions.handleTeleport(m, m.watergirl);

        // bilancia attiva se un masso sta sulla piattaforma sinistra
        boolean balanceActive = false;
        model.objects.impl.MovingPlatform leftPlatform = m.platforms.stream()
                .filter(p -> p.isLeftSide)
                .min(Comparator.comparingInt(model.objects.impl.MovingPlatform::getX))
                .orElse(null);

        if (leftPlatform != null) {
            for (model.objects.impl.Boulder b : m.boulders) {
                if (isBoulderOnPlatform(b, leftPlatform)) {
                    balanceActive = true;
                    break;
                }
            }
        }

        // muovi piattaforme
        for (model.objects.impl.MovingPlatform p : m.platforms) p.updateBalance(balanceActive);

        // trascina massi sopra piattaforme
        for (model.objects.impl.MovingPlatform p : m.platforms) {
            int dx = p.deltaX();
            int dy = p.deltaY();
            if (dx == 0 && dy == 0) continue;

            for (model.objects.impl.Boulder b : m.boulders) {
                if (isBoulderOnPlatform(b, p)) {
                    b.translate(dx, dy);

                }
            }
        }

        // trascina player sopra piattaforme
        for (model.objects.impl.MovingPlatform p : m.platforms) {
            int dx = p.deltaX();
            int dy = p.deltaY();
            if (dx == 0 && dy == 0) continue;

            if (isPlayerOnPlatform(m.fireboy, p)) {
                m.fireboy.x += dx;
                m.fireboy.y += dy;
                m.fireboy.vy = 0;
                m.fireboy.onGround = true;
            }

            if (isPlayerOnPlatform(m.watergirl, p)) {
                m.watergirl.x += dx;
                m.watergirl.y += dy;
                m.watergirl.vy = 0;
                m.watergirl.onGround = true;
            }
        }

        // goal
        if (LevelQueries.isOnGoal(m, m.fireboy) && LevelQueries.isOnGoal(m, m.watergirl)) m.levelComplete = true;
    }

    private static boolean isBoulderOnPlatform(model.objects.impl.Boulder b, model.objects.impl.MovingPlatform p) {
        Rectangle br = b.rect();
        Rectangle pr = p.rect();

        int bBottom = br.y + br.height;
        boolean xOverlap = br.x + br.width > pr.x && br.x < pr.x + pr.width;
        boolean onTop = (bBottom >= pr.y - 3) && (bBottom <= pr.y + 3);
        return xOverlap && onTop;
    }

    private static boolean isPlayerOnPlatform(model.entities.Player pl, model.objects.impl.MovingPlatform p) {
        Rectangle pr = pl.getRect();
        Rectangle plat = p.rect();

        int pBottom = pr.y + pr.height;
        boolean xOverlap = pr.x + pr.width > plat.x && pr.x < plat.x + plat.width;
        boolean onTop = (pBottom >= plat.y - 3) && (pBottom <= plat.y + 3);
        return xOverlap && onTop;
    }

    private static boolean isCrushedByBoulder(FireboyWatergirlLevel panel, model.entities.Player p, model.objects.impl.Boulder b) {
        Rectangle pr = p.getRect();
        Rectangle br = b.rect();

        boolean xOverlap = pr.x + pr.width > br.x && pr.x < br.x + br.width;
        if (!xOverlap) return false;

        int pTop = pr.y;
        int bBottom = br.y + br.height;

        boolean bOnTopOfPlayer = (bBottom >= pTop - 2) && (bBottom <= pTop + 6);
        if (!bOnTopOfPlayer) return false;

        boolean ceiling =
                panel.isSolidAtPixel(pr.x + 2, pr.y - 2)
                        || panel.isSolidAtPixel(pr.x + pr.width - 3, pr.y - 2);

        return ceiling;
    }
}
