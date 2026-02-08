package view.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import model.GameConstants;
import model.level.LevelModel;

/**
 * Renders the third level view.
 */
public final class LevelRenderer {

    private static final int OVERLAY_ALPHA = GameConstants.LEVEL3_OVERLAY_ALPHA;
    private static final int TITLE_FONT_SIZE = GameConstants.LEVEL3_OVERLAY_TITLE_FONT_SIZE;
    private static final int SUBTITLE_FONT_SIZE = GameConstants.LEVEL3_OVERLAY_SUBTITLE_FONT_SIZE;
    private static final int TITLE_OFFSET = GameConstants.LEVEL3_OVERLAY_TITLE_VERTICAL_OFFSET;
    private static final int SUBTITLE_OFFSET = GameConstants.LEVEL3_OVERLAY_SUBTITLE_VERTICAL_OFFSET;

    private LevelRenderer() {
    }

    /**
     * Draws the level (map, objects, players, overlays).
     *
     * @param panel view panel
     * @param model level model
     * @param graphics graphics context
     */
    public static void render(final FireboyWatergirlLevel panel, final LevelModel model,
            final Graphics graphics) {
        final Graphics2D g2 = (Graphics2D) graphics;

        final int worldW = model.getCols() * GameConstants.LEVEL3_TILE_PIXEL_SIZE;
        final int worldH = model.getRows() * GameConstants.LEVEL3_TILE_PIXEL_SIZE;

        final double sx = panel.getWidth() / (double) worldW;
        final double sy = panel.getHeight() / (double) worldH;
        model.setViewScale(Math.min(sx, sy));

        model.setViewOffsetX((int) ((panel.getWidth() - worldW * model.getViewScale()) / 2.0));
        model.setViewOffsetY((int) ((panel.getHeight() - worldH * model.getViewScale()) / 2.0));

        final AffineTransform old = g2.getTransform();

        g2.translate(model.getViewOffsetX(), model.getViewOffsetY());
        g2.scale(model.getViewScale(), model.getViewScale());

        if (model.getImgMap() != null) {
            g2.drawImage(model.getImgMap(), 0, 0, worldW, worldH, null);
        } else {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, worldW, worldH);
        }

        for (final model.objects.impl.Door d : model.getDoors()) {
            d.draw(g2);
        }

        // coin: non hanno draw, quindi le disegniamo qui
        if (model.getImgCoinGold() != null) {
            for (final model.objects.impl.collectable.Coin c : model.getCoins()) {
                g2.drawImage(model.getImgCoinGold(), c.getX(), c.getY(),
                        GameConstants.LEVEL3_TILE_PIXEL_SIZE, GameConstants.LEVEL3_TILE_PIXEL_SIZE, null);
            }
        }

        for (final model.objects.impl.MovingPlatform p : model.getPlatforms()) {
            p.draw(g2);
        }
        for (final model.objects.impl.Boulder b : model.getBoulders()) {
            b.draw(g2);
        }

        drawPlayer(g2, model.getFireboy(), model.getImgP1());
        drawPlayer(g2, model.getWatergirl(), model.getImgP2());

        g2.setTransform(old);

        if (model.isGameOver()) {
            drawOverlay(g2, panel, "HAI PERSO", "R = Retry, H = Home");
        } else if (model.isLevelComplete()) {
            drawOverlay(g2, panel, "LIVELLO COMPLETATO", "R = Replay, H = Home");
        }
    }

    // schermata scura + testo centrato (game over / vittoria)
    private static void drawOverlay(final Graphics g, final FireboyWatergirlLevel panel,
            final String title, final String subtitle) {
        final Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(0, 0, 0, OVERLAY_ALPHA));
        g2.fillRect(0, 0, panel.getWidth(), panel.getHeight());

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, TITLE_FONT_SIZE));
        final FontMetrics fm = g2.getFontMetrics();
        final int tx = (panel.getWidth() - fm.stringWidth(title)) / 2;
        g2.drawString(title, tx, panel.getHeight() / 2 - TITLE_OFFSET);

        g2.setFont(new Font("Arial", Font.PLAIN, SUBTITLE_FONT_SIZE));
        final FontMetrics fm2 = g2.getFontMetrics();
        final int sx = (panel.getWidth() - fm2.stringWidth(subtitle)) / 2;
        g2.drawString(subtitle, sx, panel.getHeight() / 2 + SUBTITLE_OFFSET);
    }

    private static void drawPlayer(final Graphics2D g2, final model.entities.api.Player player,
            final java.awt.image.BufferedImage sprite) {
        final int x = (int) Math.round(player.getX());
        final int y = (int) Math.round(player.getY());
        final int w = (int) Math.round(player.getWidth());
        final int h = (int) Math.round(player.getHeight());
        if (sprite != null) {
            g2.drawImage(sprite, x, y, w, h, null);
        } else {
            g2.setColor(Color.RED);
            g2.fillRect(x, y, w, h);
        }
    }
}
