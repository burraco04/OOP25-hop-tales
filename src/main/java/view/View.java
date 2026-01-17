package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import model.Model;
import model.Model.CoinSnapshot;
import model.Model.GameState;
import model.Model.HazardSnapshot;
import model.Model.Platform;
import model.entities.player.api.PlayerSnapshot;

/**
 * Swing panel that renders the current game state.
 */
public final class View extends JPanel {
    private static final long serialVersionUID = 1L;

    private static final String PLAYER_SPRITE = "bozza_player_1_vers_3.png";

    private static final Color SKY_COLOR = new Color(142, 202, 230);
    private static final Color PLATFORM_COLOR = new Color(87, 117, 144);
    private static final Color HAZARD_COLOR = new Color(214, 40, 57, 180);
    private static final Color HUD_BACKGROUND = new Color(0, 0, 0, 150);
    private static final Color HUD_TEXT_COLOR = Color.WHITE;
    private static final Color COIN_FILL = new Color(255, 193, 7);
    private static final Color COIN_BORDER = new Color(204, 140, 0);
    private static final Color PLAYER_FALLBACK = new Color(241, 96, 111);
    private static final Color PLAYER_OUTLINE = new Color(28, 52, 71);
    private static final Color SHADOW_COLOR = new Color(0, 0, 0, 90);
    private static final Color HEALTH_BAR_FILL = new Color(235, 87, 87);

    private static final int HUD_PADDING = 16;
    private static final int HUD_WIDTH = 300;
    private static final int HUD_HEIGHT = 140;
    private static final int SHADOW_HEIGHT = 12;
    private static final double CAMERA_HALF = 0.5;

    private static final long FRAME_DURATION_NANOS = 150_000_000L;

    private transient BufferedImage[] playerFrames;
    private transient int currentFrameIndex;
    private transient long lastFrameUpdate;
    private transient GameState gameState;

    public View() {
        setPreferredSize(new Dimension(Model.WORLD_WIDTH, Model.WORLD_HEIGHT));
        setBackground(SKY_COLOR);
        setDoubleBuffered(true);
        setFocusable(true);
        this.playerFrames = loadPlayerFrames();
        this.lastFrameUpdate = System.nanoTime();
    }

    public void setGameState(final GameState state) {
        SwingUtilities.invokeLater(() -> {
            this.gameState = state;
            advanceAnimation();
            repaint();
        });
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        if (gameState == null) {
            drawLoadingMessage(g2d);
            g2d.dispose();
            return;
        }

        final PlayerSnapshot player = gameState.getPlayer();
        final double cameraX = clamp(
            player.getX() + player.getWidth() * CAMERA_HALF - getWidth() * CAMERA_HALF,
            0.0,
            Math.max(0.0, gameState.getWorldWidth() - getWidth())
        );
        final double cameraY = clamp(
            player.getY() + player.getHeight() * CAMERA_HALF - getHeight() * CAMERA_HALF,
            0.0,
            Math.max(0.0, gameState.getWorldHeight() - getHeight())
        );
        g2d.translate(-cameraX, -cameraY);

        drawHazards(g2d);
        drawPlatforms(g2d);
        drawCoins(g2d);
        drawPlayer(g2d, player);

        g2d.translate(cameraX, cameraY);
        drawHud(g2d);

        g2d.dispose();
    }

    private void drawLoadingMessage(final Graphics2D g2d) {
        g2d.setColor(Color.DARK_GRAY);
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 28.0f));
        final String message = "Loading world...";
        final int width = g2d.getFontMetrics().stringWidth(message);
        g2d.drawString(message, (getWidth() - width) / 2, getHeight() / 2);
    }

    private void drawPlatforms(final Graphics2D g2d) {
        g2d.setColor(PLATFORM_COLOR);
        for (final Platform platform : gameState.getPlatforms()) {
            g2d.fill(new RoundRectangle2D.Double(
                platform.getX(),
                platform.getY(),
                platform.getWidth(),
                platform.getHeight(),
                12.0,
                12.0
            ));
        }
    }

    private void drawHazards(final Graphics2D g2d) {
        g2d.setColor(HAZARD_COLOR);
        for (final HazardSnapshot hazard : gameState.getHazards()) {
            g2d.fill(new RoundRectangle2D.Double(
                hazard.getX(),
                hazard.getY(),
                hazard.getWidth(),
                hazard.getHeight(),
                10.0,
                10.0
            ));
        }
    }

    private void drawCoins(final Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(1.5f));
        for (final CoinSnapshot coin : gameState.getCoins()) {
            if (coin.isCollected()) {
                continue;
            }
            final double diameter = coin.getRadius() * 2.0;
            final int drawX = (int) Math.round(coin.getCenterX() - coin.getRadius());
            final int drawY = (int) Math.round(coin.getCenterY() - coin.getRadius());
            final int drawDiameter = (int) Math.round(diameter);
            g2d.setColor(COIN_FILL);
            g2d.fillOval(drawX, drawY, drawDiameter, drawDiameter);
            g2d.setColor(COIN_BORDER);
            g2d.drawOval(drawX, drawY, drawDiameter, drawDiameter);
        }
    }

    private void drawPlayer(final Graphics2D g2d, final PlayerSnapshot player) {
        final int shadowWidth = (int) Math.round(player.getWidth() * 0.8);
        final int shadowX = (int) Math.round(player.getX() + (player.getWidth() - shadowWidth) * 0.5);
        final int shadowY = (int) Math.round(player.getY() + player.getHeight() - SHADOW_HEIGHT * 0.5);
        g2d.setColor(SHADOW_COLOR);
        g2d.fillOval(shadowX, shadowY, shadowWidth, SHADOW_HEIGHT);

        final BufferedImage sprite = currentFrame();
        if (sprite != null) {
            final int drawWidth = (int) Math.round(player.getWidth());
            final int drawHeight = (int) Math.round(player.getHeight());
            final int drawX = (int) Math.round(player.getX());
            final int drawY = (int) Math.round(player.getY());
            if (player.isFacingRight()) {
                g2d.drawImage(sprite, drawX, drawY, drawWidth, drawHeight, null);
            } else {
                g2d.drawImage(sprite, drawX + drawWidth, drawY, -drawWidth, drawHeight, null);
            }
        } else {
            final int drawX = (int) Math.round(player.getX());
            final int drawY = (int) Math.round(player.getY());
            final int drawWidth = (int) Math.round(player.getWidth());
            final int drawHeight = (int) Math.round(player.getHeight());
            g2d.setColor(PLAYER_FALLBACK);
            g2d.fillRoundRect(drawX, drawY, drawWidth, drawHeight, 16, 16);
            g2d.setColor(PLAYER_OUTLINE);
            g2d.setStroke(new BasicStroke(2.0f));
            g2d.drawRoundRect(drawX, drawY, drawWidth, drawHeight, 16, 16);
        }
    }

    private void drawHud(final Graphics2D g2d) {
        g2d.setColor(HUD_BACKGROUND);
        g2d.fillRoundRect(HUD_PADDING, HUD_PADDING, HUD_WIDTH, HUD_HEIGHT, 16, 16);
        g2d.setColor(HUD_TEXT_COLOR);
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 16.0f));
        g2d.drawString("Hop Tales", HUD_PADDING + 16, HUD_PADDING + 28);
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 14.0f));
        g2d.drawString("Move: A / D or Left / Right", HUD_PADDING + 16, HUD_PADDING + 48);
        g2d.drawString("Jump: Space or W / Up", HUD_PADDING + 16, HUD_PADDING + 68);
        g2d.drawString(
            String.format("Coins: %d / %d", gameState.getCoinsCollected(), gameState.getTotalCoins()),
            HUD_PADDING + 16,
            HUD_PADDING + 88
        );
        g2d.drawString(
            String.format("Health: %.1f / %.1f", gameState.getHealth(), gameState.getMaxHealth()),
            HUD_PADDING + 16,
            HUD_PADDING + 108
        );
        drawHealthBar(g2d, HUD_PADDING + 16, HUD_PADDING + 116, gameState.getHealth(), gameState.getMaxHealth());
    }

    private void drawHealthBar(
        final Graphics2D g2d,
        final int startX,
        final int startY,
        final double health,
        final double maxHealth
    ) {
        final int segments = (int) Math.round(maxHealth);
        final double clampedHealth = Math.max(0.0, Math.min(health, maxHealth));
        for (int index = 0; index < segments; index++) {
            final double remaining = clampedHealth - index;
            final double fillRatio = Math.max(0.0, Math.min(1.0, remaining));
            final int pipX = startX + index * 26;
            g2d.setColor(HEALTH_BAR_FILL);
            g2d.fillRoundRect(pipX, startY, (int) Math.round(20 * fillRatio), 10, 8, 8);
            g2d.setColor(Color.WHITE);
            g2d.drawRoundRect(pipX, startY, 20, 10, 8, 8);
        }
    }

    private void advanceAnimation() {
        if (playerFrames == null || playerFrames.length == 0) {
            return;
        }
        final long now = System.nanoTime();
        if (now - lastFrameUpdate >= FRAME_DURATION_NANOS) {
            currentFrameIndex = (currentFrameIndex + 1) % playerFrames.length;
            lastFrameUpdate = now;
        }
    }

    private BufferedImage currentFrame() {
        if (playerFrames == null || playerFrames.length == 0) {
            return null;
        }
        return playerFrames[currentFrameIndex];
    }

    private BufferedImage[] loadPlayerFrames() {
        try (InputStream stream = View.class.getResourceAsStream("/" + PLAYER_SPRITE)) {
            if (stream != null) {
                return sliceFrames(ImageIO.read(stream));
            }
        } catch (IOException ignored) {
            // fall back to filesystem
        }
        final Path path = Path.of("src", "main", "res", PLAYER_SPRITE);
        if (Files.exists(path)) {
            try (InputStream stream = Files.newInputStream(path)) {
                return sliceFrames(ImageIO.read(stream));
            } catch (IOException ignored) {
                return new BufferedImage[0];
            }
        }
        return new BufferedImage[0];
    }

    private BufferedImage[] sliceFrames(final BufferedImage sheet) {
        if (sheet == null) {
            return new BufferedImage[0];
        }
        if (sheet.getWidth() >= sheet.getHeight() * 2) {
            final int frameWidth = sheet.getWidth() / 2;
            final int frameHeight = sheet.getHeight();
            return new BufferedImage[] {
                sheet.getSubimage(0, 0, frameWidth, frameHeight),
                sheet.getSubimage(frameWidth, 0, frameWidth, frameHeight)
            };
        } else {
            final int frameWidth = sheet.getWidth();
            final int frameHeight = sheet.getHeight() / 2;
            return new BufferedImage[] {
                sheet.getSubimage(0, 0, frameWidth, frameHeight),
                sheet.getSubimage(0, frameHeight, frameWidth, frameHeight)
            };
        }
    }

    private double clamp(final double value, final double min, final double max) {
        return Math.max(min, Math.min(max, value));
    }
}
