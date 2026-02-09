package model.level;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Iterator;

import model.CoinStorage;
import model.GameConstants;

/**
 * Utility class for level interactions.
 */
public final class LevelInteractions {

    private LevelInteractions() {
    }

    /**
     * Collects coins when the player touches them and updates the saved total.
     *
     * @param model level model
     * @param player player entity
     */
    public static void collectCoins(final LevelModel model, final model.entities.api.Player player) {
        final Rectangle pr = playerRect(player);

        final Iterator<model.objects.impl.collectable.Coin> it = model.getCoins().iterator();
        while (it.hasNext()) {
            final model.objects.impl.collectable.Coin c = it.next();

            final Rectangle cr = new Rectangle(
                    c.getX(),
                    c.getY(),
                    GameConstants.LEVEL3_TILE_PIXEL_SIZE,
                    GameConstants.LEVEL3_TILE_PIXEL_SIZE
            );

            if (cr.intersects(pr)) {
                it.remove();
                CoinStorage.collectCoin();
                model.setTotalCoinsSaved(CoinStorage.getCoins());
            }
        }
    }

    /**
     * Handles door buttons.
     *
     * @param model level model
     * @param player player entity
     */
    public static void handleButtons(final LevelModel model, final model.entities.api.Player player) {
        for (final model.objects.impl.ButtonPad b : model.getButtons()) {
            if (b.intersects(playerRect(player))) {
                final Point tilePos = new Point(
                        b.getX() / GameConstants.LEVEL3_TILE_PIXEL_SIZE,
                        b.getY() / GameConstants.LEVEL3_TILE_PIXEL_SIZE
                );
                final String doorId = model.getButtonToDoorId().get(tilePos);

                if (doorId != null) {
                    LevelBuilder.removeDoorTilesFromMap(model, doorId);

                    for (final model.objects.impl.Door d : model.getDoors()) {
                        final int rr = d.getY() / GameConstants.LEVEL3_TILE_PIXEL_SIZE;
                        final int cc = d.getX() / GameConstants.LEVEL3_TILE_PIXEL_SIZE;
                        if (model.getMap()[rr][cc] != '3') {
                            d.setOpen(true);
                        }
                    }
                }
            }
        }
    }

    /**
     * Handles teleport interactions.
     *
     * @param model level model
     * @param player player entity
     */
    public static void handleTeleport(final LevelModel model, final model.entities.api.Player player) {
        for (final model.objects.impl.Teleporter t : model.getTeleporters()) {
            if (t.intersects(playerRect(player))) {
                final Point from = new Point(
                        t.getX() / GameConstants.LEVEL3_TILE_PIXEL_SIZE,
                        t.getY() / GameConstants.LEVEL3_TILE_PIXEL_SIZE
                );
                final Point dest = model.getTeleportDestTile().get(from);
                if (dest != null) {
                    player.setX(dest.x * GameConstants.LEVEL3_TILE_PIXEL_SIZE);
                    player.setY(dest.y * GameConstants.LEVEL3_TILE_PIXEL_SIZE);
                }
            }
        }
    }

    private static Rectangle playerRect(final model.entities.api.Player player) {
        final int x = (int) Math.round(player.getX());
        final int y = (int) Math.round(player.getY());
        final int w = (int) Math.round(player.getWidth());
        final int h = (int) Math.round(player.getHeight());
        return new Rectangle(x, y, w, h);
    }
}
