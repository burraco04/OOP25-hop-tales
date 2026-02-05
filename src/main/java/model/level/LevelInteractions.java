package model.level;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Iterator;

import model.CoinStorage;

public final class LevelInteractions {

    private LevelInteractions() {}
    // raccoglie le monete quando il player ci passa sopra e aggiorna il totale salvato
    public static void collectCoins(LevelModel m, model.entities.api.Player p) {
        Rectangle pr = playerRect(p);

        Iterator<model.objects.impl.collectable.Coin> it = m.coins.iterator();
        while (it.hasNext()) {
            model.objects.impl.collectable.Coin c = it.next();

            Rectangle cr = new Rectangle(
                    c.getX(),
                    c.getY(),
                    LevelConstants.TILE,
                    LevelConstants.TILE
            );

            if (cr.intersects(pr)) {
                it.remove();
                m.totalCoinsSaved = CoinStorage.addCoins(1);
                System.out.println("Moneta presa! Totale salvato = " + m.totalCoinsSaved);
            }
        }
    }
    // se un player preme un bottone, apre la porta associata
    public static void handleButtons(LevelModel m, model.entities.api.Player p) {
        for (model.objects.impl.ButtonPad b : m.buttons) {
            if (b.intersects(playerRect(p))) {
                Point tilePos = new Point(b.getX() / LevelConstants.TILE, b.getY() / LevelConstants.TILE);
                String doorId = m.buttonToDoorId.get(tilePos);

                if (doorId != null) {
                    LevelBuilder.removeDoorTilesFromMap(m, doorId);

                    for (model.objects.impl.Door d : m.doors) {
                        int rr = d.getY() / LevelConstants.TILE;
                        int cc = d.getX() / LevelConstants.TILE;
                        if (m.map[rr][cc] != '3') d.open = true;
                    }
                }
            }
        }
    }

    public static void handleTeleport(LevelModel m, model.entities.api.Player p) {
        for (model.objects.impl.Teleporter t : m.teleporters) {
            if (t.intersects(playerRect(p))) {
                Point from = new Point(t.getX() / LevelConstants.TILE, t.getY() / LevelConstants.TILE);
                Point dest = m.teleportDestTile.get(from);
                if (dest != null) {
                    p.setX(dest.x * LevelConstants.TILE);
                    p.setY(dest.y * LevelConstants.TILE);
                }
            }
        }
    }

    private static Rectangle playerRect(model.entities.api.Player p) {
        int x = (int) Math.round(p.getX());
        int y = (int) Math.round(p.getY());
        int w = (int) Math.round(p.getWidth());
        int h = (int) Math.round(p.getHeight());
        return new Rectangle(x, y, w, h);
    }
}
