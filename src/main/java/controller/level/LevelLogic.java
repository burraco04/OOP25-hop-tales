package controller.level;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.Comparator;

import model.level.LevelInteractions;
import model.GameConstants;
import model.level.LevelModel;
import model.level.LevelQueries;
import model.objects.impl.MovingPlatform;
import view.impl.FireboyWatergirlLevel;

/**
 * Controller for the third level gameplay loop.
 */
public final class LevelLogic {

    private static final double GRAVITY = GameConstants.LEVEL3_PLAYER_GRAVITY_PER_TICK;
    private static final double MAX_FALL_SPEED = GameConstants.LEVEL3_PLAYER_MAX_FALL_SPEED;
    private static final double JUMP_SPEED = GameConstants.LEVEL3_PLAYER_JUMP_SPEED;
    private static final int MOVE_SPEED = GameConstants.LEVEL3_PLAYER_MOVE_SPEED_PIXELS;
    private static final int PLATFORM_TOLERANCE = GameConstants.LEVEL3_PLATFORM_VERTICAL_TOLERANCE_PIXELS;
    private static final int CRUSH_TOP_TOLERANCE = GameConstants.LEVEL3_CRUSH_TOP_TOLERANCE_PIXELS;
    private static final int CRUSH_BOTTOM_TOLERANCE = GameConstants.LEVEL3_CRUSH_BOTTOM_TOLERANCE_PIXELS;

    private LevelLogic() { }

    /**
     * Advances the level simulation by one tick.
     *
     * @param panel view panel
     * @param model level model
     * @param input input handler
     */
    public static void tick(final FireboyWatergirlLevel panel, final LevelModel model, final LevelInput input) {
        if (model.isGameOver() || model.isLevelComplete()) {
            return;
        }

        // input continuo
        if (input.isKeyDown(KeyEvent.VK_LEFT) && !input.isKeyDown(KeyEvent.VK_RIGHT)) {
            model.getFireboy().setVelocityX(-MOVE_SPEED);
        } else if (input.isKeyDown(KeyEvent.VK_RIGHT) && !input.isKeyDown(KeyEvent.VK_LEFT)) {
            model.getFireboy().setVelocityX(MOVE_SPEED);
        } else {
            model.getFireboy().setVelocityX(0);
        }

        if (input.isKeyDown(KeyEvent.VK_A) && !input.isKeyDown(KeyEvent.VK_D)) {
            model.getWatergirl().setVelocityX(-MOVE_SPEED);
        } else if (input.isKeyDown(KeyEvent.VK_D) && !input.isKeyDown(KeyEvent.VK_A)) {
            model.getWatergirl().setVelocityX(MOVE_SPEED);
        } else {
            model.getWatergirl().setVelocityX(0);
        }

        if (input.isFireboyJumpQueued()) {
            tryJump(model.getFireboy());
            input.consumeFireboyJump();
        }
        if (input.isWatergirlJumpQueued()) {
            tryJump(model.getWatergirl());
            input.consumeWatergirlJump();
        }

        // push massi
        for (final model.objects.impl.Boulder b : model.getBoulders()) {
            b.tryPushBy(model.getFireboy(), panel);
            b.tryPushBy(model.getWatergirl(), panel);
        }

        // update player
        updatePlayer(model, model.getFireboy());
        updatePlayer(model, model.getWatergirl());

        // fisica massi
        for (final model.objects.impl.Boulder b : model.getBoulders()) {
            b.updatePhysics(panel);
        }

        // schiacciamento
        for (final model.objects.impl.Boulder b : model.getBoulders()) {
            if (b.getVelocityY() > 0) {
                if (isCrushedByBoulder(panel, model.getFireboy(), b)
                        || isCrushedByBoulder(panel, model.getWatergirl(), b)) {
                    model.setGameOver(true);
                    break;
                }
            }
        }

        // lava
        if (LevelQueries.touchesLava(model, model.getFireboy())
                || LevelQueries.touchesLava(model, model.getWatergirl())) {
            model.setGameOver(true);
        }

        // monete
        LevelInteractions.collectCoins(model, model.getFireboy());
        LevelInteractions.collectCoins(model, model.getWatergirl());

        // bottoni
        LevelInteractions.handleButtons(model, model.getFireboy());
        LevelInteractions.handleButtons(model, model.getWatergirl());

        // teleport
        LevelInteractions.handleTeleport(model, model.getFireboy());
        LevelInteractions.handleTeleport(model, model.getWatergirl());

        // bilancia attiva se un masso sta sulla piattaforma sinistra
        boolean balanceActive = false;
        final MovingPlatform leftPlatform = model.getPlatforms().stream()
                .filter(MovingPlatform::isLeftSide)
                .min(Comparator.comparingInt(MovingPlatform::getX))
                .orElse(null);

        if (leftPlatform != null) {
            for (final model.objects.impl.Boulder b : model.getBoulders()) {
                if (isBoulderOnPlatform(b, leftPlatform)) {
                    balanceActive = true;
                    break;
                }
            }
        }

        // muovi piattaforme
        for (final model.objects.impl.MovingPlatform p : model.getPlatforms()) {
            p.updateBalance(balanceActive);
        }

        // trascina massi sopra piattaforme
        for (final model.objects.impl.MovingPlatform p : model.getPlatforms()) {
            final int dx = p.deltaX();
            final int dy = p.deltaY();
            if (dx == 0 && dy == 0) {
                continue;
            }

            for (final model.objects.impl.Boulder b : model.getBoulders()) {
                if (isBoulderOnPlatform(b, p)) {
                    b.translate(dx, dy);
                }
            }
        }

        // trascina player sopra piattaforme
        for (final model.objects.impl.MovingPlatform p : model.getPlatforms()) {
            final int dx = p.deltaX();
            final int dy = p.deltaY();
            if (dx == 0 && dy == 0) {
                continue;
            }

            if (isPlayerOnPlatform(model.getFireboy(), p)) {
                model.getFireboy().setX(model.getFireboy().getX() + dx);
                model.getFireboy().setY(model.getFireboy().getY() + dy);
                model.getFireboy().setVelocityY(0);
                model.getFireboy().setOnGround(true);
            }

            if (isPlayerOnPlatform(model.getWatergirl(), p)) {
                model.getWatergirl().setX(model.getWatergirl().getX() + dx);
                model.getWatergirl().setY(model.getWatergirl().getY() + dy);
                model.getWatergirl().setVelocityY(0);
                model.getWatergirl().setOnGround(true);
            }
        }

        // goal
        if (LevelQueries.isOnGoal(model, model.getFireboy())
                && LevelQueries.isOnGoal(model, model.getWatergirl())) {
            model.setLevelComplete(true);
        }
    }

    private static boolean isBoulderOnPlatform(final model.objects.impl.Boulder b,
            final model.objects.impl.MovingPlatform p) {
        final Rectangle br = b.rect();
        final Rectangle pr = p.rect();

        final int bBottom = br.y + br.height;
        final boolean xOverlap = br.x + br.width > pr.x && br.x < pr.x + pr.width;
        final boolean onTop = bBottom >= pr.y - PLATFORM_TOLERANCE && bBottom <= pr.y + PLATFORM_TOLERANCE;
        return xOverlap && onTop;
    }

    private static boolean isPlayerOnPlatform(final model.entities.api.Player player,
            final model.objects.impl.MovingPlatform p) {
        final Rectangle pr = playerRect(player);
        final Rectangle plat = p.rect();

        final int pBottom = pr.y + pr.height;
        final boolean xOverlap = pr.x + pr.width > plat.x && pr.x < plat.x + plat.width;
        final boolean onTop = pBottom >= plat.y - PLATFORM_TOLERANCE && pBottom <= plat.y + PLATFORM_TOLERANCE;
        return xOverlap && onTop;
    }

    private static boolean isCrushedByBoulder(final FireboyWatergirlLevel panel,
            final model.entities.api.Player player, final model.objects.impl.Boulder b) {
        final Rectangle pr = playerRect(player);
        final Rectangle br = b.rect();

        final boolean xOverlap = pr.x + pr.width > br.x && pr.x < br.x + br.width;
        if (!xOverlap) {
            return false;
        }

        final int pTop = pr.y;
        final int bBottom = br.y + br.height;

        final boolean bOnTopOfPlayer = bBottom >= pTop - CRUSH_TOP_TOLERANCE
                && bBottom <= pTop + CRUSH_BOTTOM_TOLERANCE;
        if (!bOnTopOfPlayer) {
            return false;
        }

        final boolean ceiling =
                panel.isSolidAtPixel(pr.x + 2, pr.y - 2)
                        || panel.isSolidAtPixel(pr.x + pr.width - 3, pr.y - 2);

        return ceiling;
    }

    private static void tryJump(final model.entities.impl.PlayerImpl player) {
        if (player.isOnGround()) {
            player.setVelocityY(JUMP_SPEED);
            player.setOnGround(false);
        }
    }

    private static void updatePlayer(final LevelModel model, final model.entities.impl.PlayerImpl player) {
        player.addVelocityY(GRAVITY);
        if (player.getVelocityY() > MAX_FALL_SPEED) {
            player.setVelocityY(MAX_FALL_SPEED);
        }

        moveHorizontal(model, player);
        moveVertical(model, player);

        if (!player.isOnGround()) {
            player.setOnGround(isGrounded(model, player));
        }
    }

    private static void moveHorizontal(final LevelModel model, final model.entities.impl.PlayerImpl player) {
        final double vx = player.getVelocityX();
        if (vx == 0) {
            return;
        }

        final int step = vx > 0 ? 1 : -1;
        final int steps = (int) Math.abs(vx);

        for (int i = 0; i < steps; i++) {
            final int nx = (int) Math.round(player.getX()) + step;
            final int ny = (int) Math.round(player.getY());
            if (!collidesAt(model, player, nx, ny)) {
                player.setX(nx);
            } else {
                break;
            }
        }
    }

    private static void moveVertical(final LevelModel model, final model.entities.impl.PlayerImpl player) {
        final double vy = player.getVelocityY();
        if (vy == 0) {
            player.setOnGround(isGrounded(model, player));
            return;
        }

        final int step = vy > 0 ? 1 : -1;
        final int steps = (int) Math.floor(Math.abs(vy));
        final double remainder = Math.abs(vy) - steps;

        for (int i = 0; i < steps; i++) {
            final int nx = (int) Math.round(player.getX());
            final int ny = (int) Math.round(player.getY()) + step;
            if (!collidesAt(model, player, nx, ny)) {
                player.setY(ny);
                player.setOnGround(false);
            } else {
                if (step > 0) {
                    player.setOnGround(true);
                }
                player.setVelocityY(0);
                return;
            }
        }

        if (remainder > 0) {
            final int nx = (int) Math.round(player.getX());
            final int ny = (int) Math.round(player.getY() + remainder * step);
            if (!collidesAt(model, player, nx, ny)) {
                player.setY(player.getY() + remainder * step);
                player.setOnGround(false);
            } else {
                if (step > 0) {
                    player.setOnGround(true);
                }
                player.setVelocityY(0);
            }
        }
    }

    private static boolean collidesAt(final LevelModel model, final model.entities.api.Player player,
            final int nx, final int ny) {
        final int w = (int) Math.round(player.getWidth());
        final int h = (int) Math.round(player.getHeight());
        return LevelQueries.isSolidAtPixel(model, nx + 1, ny + 1, null)
                || LevelQueries.isSolidAtPixel(model, nx + w - 2, ny + 1, null)
                || LevelQueries.isSolidAtPixel(model, nx + 1, ny + h - 2, null)
                || LevelQueries.isSolidAtPixel(model, nx + w - 2, ny + h - 2, null);
    }

    private static boolean isGrounded(final LevelModel model, final model.entities.api.Player player) {
        final int x = (int) Math.round(player.getX());
        final int y = (int) Math.round(player.getY());
        final int w = (int) Math.round(player.getWidth());
        final int h = (int) Math.round(player.getHeight());
        return LevelQueries.isSolidAtPixel(model, x + 1, y + h + 1, null)
                || LevelQueries.isSolidAtPixel(model, x + w - 2, y + h + 1, null);
    }

    private static Rectangle playerRect(final model.entities.api.Player player) {
        final int x = (int) Math.round(player.getX());
        final int y = (int) Math.round(player.getY());
        final int w = (int) Math.round(player.getWidth());
        final int h = (int) Math.round(player.getHeight());
        return new Rectangle(x, y, w, h);
    }
}
