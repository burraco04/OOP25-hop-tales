package app.level;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.Comparator;

public final class LevelLogic {

    private LevelLogic() {}

    private static final double GRAVITY = 0.35;
    private static final double MAX_FALL_SPEED = 10.0;
    private static final double JUMP_SPEED = -8.0;

    public static void tick(FireboyWatergirlLevel panel, LevelModel m, LevelInput input) {
        if (m.gameOver || m.levelComplete) return;

        // input continuo
        if (input.keysDown.contains(KeyEvent.VK_LEFT) && !input.keysDown.contains(KeyEvent.VK_RIGHT)) {
            m.fireboy.setVelocityX(-3);
        } else if (input.keysDown.contains(KeyEvent.VK_RIGHT) && !input.keysDown.contains(KeyEvent.VK_LEFT)) {
            m.fireboy.setVelocityX(3);
        } else {
            m.fireboy.setVelocityX(0);
        }

        if (input.keysDown.contains(KeyEvent.VK_A) && !input.keysDown.contains(KeyEvent.VK_D)) {
            m.watergirl.setVelocityX(-3);
        } else if (input.keysDown.contains(KeyEvent.VK_D) && !input.keysDown.contains(KeyEvent.VK_A)) {
            m.watergirl.setVelocityX(3);
        } else {
            m.watergirl.setVelocityX(0);
        }

        if (input.fireboyJumpQueued) {
            tryJump(m.fireboy);
            input.fireboyJumpQueued = false;
        }
        if (input.watergirlJumpQueued) {
            tryJump(m.watergirl);
            input.watergirlJumpQueued = false;
        }

        // push massi
        for (model.objects.impl.Boulder b : m.boulders) {
            b.tryPushBy(m.fireboy, panel);
            b.tryPushBy(m.watergirl, panel);
        }

        // update player
        updatePlayer(m, m.fireboy);
        updatePlayer(m, m.watergirl);

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
                m.fireboy.setX(m.fireboy.getX() + dx);
                m.fireboy.setY(m.fireboy.getY() + dy);
                m.fireboy.setVelocityY(0);
                m.fireboy.setOnGround(true);
            }

            if (isPlayerOnPlatform(m.watergirl, p)) {
                m.watergirl.setX(m.watergirl.getX() + dx);
                m.watergirl.setY(m.watergirl.getY() + dy);
                m.watergirl.setVelocityY(0);
                m.watergirl.setOnGround(true);
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

    private static boolean isPlayerOnPlatform(model.entities.api.Player pl, model.objects.impl.MovingPlatform p) {
        Rectangle pr = playerRect(pl);
        Rectangle plat = p.rect();

        int pBottom = pr.y + pr.height;
        boolean xOverlap = pr.x + pr.width > plat.x && pr.x < plat.x + plat.width;
        boolean onTop = (pBottom >= plat.y - 3) && (pBottom <= plat.y + 3);
        return xOverlap && onTop;
    }

    private static boolean isCrushedByBoulder(FireboyWatergirlLevel panel, model.entities.api.Player p, model.objects.impl.Boulder b) {
        Rectangle pr = playerRect(p);
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

    private static void tryJump(model.entities.impl.PlayerImpl p) {
        if (p.isOnGround()) {
            p.setVelocityY(JUMP_SPEED);
            p.setOnGround(false);
        }
    }

    private static void updatePlayer(LevelModel m, model.entities.impl.PlayerImpl p) {
        p.addVelocityY(GRAVITY);
        if (p.getVelocityY() > MAX_FALL_SPEED) {
            p.setVelocityY(MAX_FALL_SPEED);
        }

        moveHorizontal(m, p);
        moveVertical(m, p);

        if (!p.isOnGround()) {
            p.setOnGround(isGrounded(m, p));
        }
    }

    private static void moveHorizontal(LevelModel m, model.entities.impl.PlayerImpl p) {
        double vx = p.getVelocityX();
        if (vx == 0) return;

        int step = vx > 0 ? 1 : -1;
        int steps = (int) Math.abs(vx);

        for (int i = 0; i < steps; i++) {
            int nx = (int) Math.round(p.getX()) + step;
            int ny = (int) Math.round(p.getY());
            if (!collidesAt(m, p, nx, ny)) {
                p.setX(nx);
            } else {
                break;
            }
        }
    }

    private static void moveVertical(LevelModel m, model.entities.impl.PlayerImpl p) {
        double vy = p.getVelocityY();
        if (vy == 0) {
            p.setOnGround(isGrounded(m, p));
            return;
        }

        int step = vy > 0 ? 1 : -1;
        int steps = (int) Math.floor(Math.abs(vy));
        double remainder = Math.abs(vy) - steps;

        for (int i = 0; i < steps; i++) {
            int nx = (int) Math.round(p.getX());
            int ny = (int) Math.round(p.getY()) + step;
            if (!collidesAt(m, p, nx, ny)) {
                p.setY(ny);
                p.setOnGround(false);
            } else {
                if (step > 0) {
                    p.setOnGround(true);
                }
                p.setVelocityY(0);
                return;
            }
        }

        if (remainder > 0) {
            int nx = (int) Math.round(p.getX());
            int ny = (int) Math.round(p.getY() + remainder * step);
            if (!collidesAt(m, p, nx, ny)) {
                p.setY(p.getY() + remainder * step);
                p.setOnGround(false);
            } else {
                if (step > 0) {
                    p.setOnGround(true);
                }
                p.setVelocityY(0);
            }
        }
    }

    private static boolean collidesAt(LevelModel m, model.entities.api.Player p, int nx, int ny) {
        int w = (int) Math.round(p.getWidth());
        int h = (int) Math.round(p.getHeight());
        return LevelQueries.isSolidAtPixel(m, nx + 1, ny + 1, null)
                || LevelQueries.isSolidAtPixel(m, nx + w - 2, ny + 1, null)
                || LevelQueries.isSolidAtPixel(m, nx + 1, ny + h - 2, null)
                || LevelQueries.isSolidAtPixel(m, nx + w - 2, ny + h - 2, null);
    }

    private static boolean isGrounded(LevelModel m, model.entities.api.Player p) {
        int x = (int) Math.round(p.getX());
        int y = (int) Math.round(p.getY());
        int w = (int) Math.round(p.getWidth());
        int h = (int) Math.round(p.getHeight());
        return LevelQueries.isSolidAtPixel(m, x + 1, y + h + 1, null)
                || LevelQueries.isSolidAtPixel(m, x + w - 2, y + h + 1, null);
    }

    private static Rectangle playerRect(model.entities.api.Player p) {
        int x = (int) Math.round(p.getX());
        int y = (int) Math.round(p.getY());
        int w = (int) Math.round(p.getWidth());
        int h = (int) Math.round(p.getHeight());
        return new Rectangle(x, y, w, h);
    }
}
