package app.level;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public final class LevelRenderer {

    private LevelRenderer() {}
// disegna tutto il livello (mappa, oggetti, player, overlay finale)
    public static void render(FireboyWatergirlLevel panel, LevelModel m, Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        int worldW = m.cols * FireboyWatergirlLevel.TILE;
        int worldH = m.rows * FireboyWatergirlLevel.TILE;

        double sx = panel.getWidth() / (double) worldW;
        double sy = panel.getHeight() / (double) worldH;
        m.viewScale = Math.min(sx, sy);

        m.viewOffsetX = (int) ((panel.getWidth() - worldW * m.viewScale) / 2.0);
        m.viewOffsetY = (int) ((panel.getHeight() - worldH * m.viewScale) / 2.0);

        AffineTransform old = g2.getTransform();

        g2.translate(m.viewOffsetX, m.viewOffsetY);
        g2.scale(m.viewScale, m.viewScale);

        if (m.imgMap != null) {
            g2.drawImage(m.imgMap, 0, 0, worldW, worldH, null);
        } else {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, worldW, worldH);
        }

        for (model.objects.impl.Door d : m.doors) d.draw(g2);

        // coin: non hanno draw, quindi le disegniamo qui
        if (m.imgCoinGold != null) {
            for (model.objects.impl.collectable.Coin c : m.coins) {
                g2.drawImage(m.imgCoinGold, c.getX(), c.getY(), FireboyWatergirlLevel.TILE, FireboyWatergirlLevel.TILE, null);
            }
        }

        for (model.objects.impl.MovingPlatform p : m.platforms) p.draw(g2);
        for (model.objects.impl.Boulder b : m.boulders) b.draw(g2);

        drawPlayer(g2, m.fireboy, m.imgP1);
        drawPlayer(g2, m.watergirl, m.imgP2);

        g2.setTransform(old);

        if (m.gameOver) drawOverlay(g2, panel, "HAI PERSO", "R = Retry, H = Home");
        else if (m.levelComplete) drawOverlay(g2, panel, "LIVELLO COMPLETATO", "R = Replay, H = Home");
    }
    // schermata scura + testo centrato (game over / vittoria)
    private static void drawOverlay(Graphics g, FireboyWatergirlLevel panel, String title, String subtitle) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(0, 0, 0, 170));
        g2.fillRect(0, 0, panel.getWidth(), panel.getHeight());

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 40));
        FontMetrics fm = g2.getFontMetrics();
        int tx = (panel.getWidth() - fm.stringWidth(title)) / 2;
        g2.drawString(title, tx, panel.getHeight() / 2 - 20);

        g2.setFont(new Font("Arial", Font.PLAIN, 18));
        FontMetrics fm2 = g2.getFontMetrics();
        int sx = (panel.getWidth() - fm2.stringWidth(subtitle)) / 2;
        g2.drawString(subtitle, sx, panel.getHeight() / 2 + 20);
    }

    private static void drawPlayer(Graphics2D g2, model.entities.api.Player p, java.awt.image.BufferedImage sprite) {
        int x = (int) Math.round(p.getX());
        int y = (int) Math.round(p.getY());
        int w = (int) Math.round(p.getWidth());
        int h = (int) Math.round(p.getHeight());
        if (sprite != null) {
            g2.drawImage(sprite, x, y, w, h, null);
        } else {
            g2.setColor(Color.RED);
            g2.fillRect(x, y, w, h);
        }
    }
}
