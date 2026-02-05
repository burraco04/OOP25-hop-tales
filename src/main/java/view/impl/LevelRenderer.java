package view.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import model.level.LevelConstants;
import model.level.LevelModel;

public final class LevelRenderer {

    private LevelRenderer() {}
// disegna tutto il livello (mappa, oggetti, player, overlay finale)
    public static void render(FireboyWatergirlLevel panel, LevelModel m, Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        int worldW = m.getCols() * LevelConstants.TILE;
        int worldH = m.getRows() * LevelConstants.TILE;

        double sx = panel.getWidth() / (double) worldW;
        double sy = panel.getHeight() / (double) worldH;
        m.setViewScale(Math.min(sx, sy));

        m.setViewOffsetX((int) ((panel.getWidth() - worldW * m.getViewScale()) / 2.0));
        m.setViewOffsetY((int) ((panel.getHeight() - worldH * m.getViewScale()) / 2.0));

        AffineTransform old = g2.getTransform();

        g2.translate(m.getViewOffsetX(), m.getViewOffsetY());
        g2.scale(m.getViewScale(), m.getViewScale());

        if (m.getImgMap() != null) {
            g2.drawImage(m.getImgMap(), 0, 0, worldW, worldH, null);
        } else {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, worldW, worldH);
        }

        for (model.objects.impl.Door d : m.getDoors()) d.draw(g2);

        // coin: non hanno draw, quindi le disegniamo qui
        if (m.getImgCoinGold() != null) {
            for (model.objects.impl.collectable.Coin c : m.getCoins()) {
                g2.drawImage(m.getImgCoinGold(), c.getX(), c.getY(), LevelConstants.TILE, LevelConstants.TILE, null);
            }
        }

        for (model.objects.impl.MovingPlatform p : m.getPlatforms()) p.draw(g2);
        for (model.objects.impl.Boulder b : m.getBoulders()) b.draw(g2);

        drawPlayer(g2, m.getFireboy(), m.getImgP1());
        drawPlayer(g2, m.getWatergirl(), m.getImgP2());

        g2.setTransform(old);

        if (m.isGameOver()) drawOverlay(g2, panel, "HAI PERSO", "R = Retry, H = Home");
        else if (m.isLevelComplete()) drawOverlay(g2, panel, "LIVELLO COMPLETATO", "R = Replay, H = Home");
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
