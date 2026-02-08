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
                    LevelConstants.TILE,
                    LevelConstants.TILE
            );

            if (cr.intersects(pr)) {
                it.remove();
                CoinStorage.collectCoin();
                model.setTotalCoinsSaved(CoinStorage.getCoins());
                System.out.println("Moneta presa! Totale salvato = " + model.getTotalCoinsSaved());
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
                final Point tilePos = new Point(b.getX() / LevelConstants.TILE, b.getY() / LevelConstants.TILE);
                final String doorId = model.getButtonToDoorId().get(tilePos);

                if (doorId != null) {
                    LevelBuilder.removeDoorTilesFromMap(model, doorId);

                    for (final model.objects.impl.Door d : model.getDoors()) {
                        final int rr = d.getY() / LevelConstants.TILE;
                        final int cc = d.getX() / LevelConstants.TILE;
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
                final Point from = new Point(t.getX() / LevelConstants.TILE, t.getY() / LevelConstants.TILE);
                final Point dest = model.getTeleportDestTile().get(from);
                if (dest != null) {
                    player.setX(dest.x * LevelConstants.TILE);
                    player.setY(dest.y * LevelConstants.TILE);
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
