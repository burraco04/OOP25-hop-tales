package app.level;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Iterator;

import model.CoinStorage;

public final class LevelInteractions {

    private LevelInteractions() {}
    // raccoglie le monete quando il player ci passa sopra e aggiorna il totale salvato
    public static void collectCoins(LevelModel m, model.entities.Player p) {
        Rectangle pr = p.getRect();

        Iterator<model.objects.impl.collectable.Coin> it = m.coins.iterator();
        while (it.hasNext()) {
            model.objects.impl.collectable.Coin c = it.next();

            Rectangle cr = new Rectangle(
                    c.getX(),
                    c.getY(),
                    FireboyWatergirlLevel.TILE,
                    FireboyWatergirlLevel.TILE
            );

            if (cr.intersects(pr)) {
                it.remove();
                m.totalCoinsSaved = CoinStorage.addCoins(1);
                System.out.println("Moneta presa! Totale salvato = " + m.totalCoinsSaved);
            }
        }
    }
    // se un player preme un bottone, apre la porta associata
    public static void handleButtons(LevelModel m, model.entities.Player p) {
        for (model.objects.impl.ButtonPad b : m.buttons) {
            if (b.intersects(p.getRect())) {
                Point tilePos = new Point(b.getX() / FireboyWatergirlLevel.TILE, b.getY() / FireboyWatergirlLevel.TILE);
                String doorId = m.buttonToDoorId.get(tilePos);

                if (doorId != null) {
                    LevelBuilder.removeDoorTilesFromMap(m, doorId);

                    for (model.objects.impl.Door d : m.doors) {
                        int rr = d.getY() / FireboyWatergirlLevel.TILE;
                        int cc = d.getX() / FireboyWatergirlLevel.TILE;
                        if (m.map[rr][cc] != '3') d.open = true;
                    }
                }
            }
        }
    }

    public static void handleTeleport(LevelModel m, model.entities.Player p) {
        for (model.objects.impl.Teleporter t : m.teleporters) {
            if (t.intersects(p.getRect())) {
                Point from = new Point(t.getX() / FireboyWatergirlLevel.TILE, t.getY() / FireboyWatergirlLevel.TILE);
                Point dest = m.teleportDestTile.get(from);
                if (dest != null) {
                    p.x = dest.x * FireboyWatergirlLevel.TILE;
                    p.y = dest.y * FireboyWatergirlLevel.TILE;
                }
            }
        }
    }
}
