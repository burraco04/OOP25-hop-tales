package view;

import model.Model;
import model.entities.player.api.PlayerSnapshot;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
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

/**
 * Swing view that renders the current {@link Model.GameState}.
 */
public class View extends JPanel {
    private static final String PLAYER_SPRITE_SHEET = "bozza_player_1_vers_3.png";
    private static final int PLAYER_FRAME_COUNT = 2;
    private static final long FRAME_DURATION_NANOS = 150_000_000L;
    private static final Color SKY_COLOR = new Color(142, 202, 230);
    private static final Color PLATFORM_COLOR = new Color(87, 117, 144);
    private static final Color OUTLINE_COLOR = new Color(28, 52, 71);

    private transient BufferedImage[] playerSprites;
    private transient int currentSpriteIndex;
    private transient long lastFrameChangeNanos;
    private transient Model.GameState gameState;

    /**
     * Creates the view with preset dimensions and assets.
     */
    public View() {
        setPreferredSize(new Dimension(Model.WORLD_WIDTH, Model.WORLD_HEIGHT));
        setBackground(SKY_COLOR);
        setDoubleBuffered(true);
        setFocusable(true);
        this.playerSprites = loadPlayerSprites();
        this.lastFrameChangeNanos = System.nanoTime();
    }

    /**
     * Updates the state to render on the next paint pass.
     *
     * @param state immutable snapshot of the world
     */
    public void setGameState(final Model.GameState state) {
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

        final double cameraX = clamp(
            gameState.player.x + gameState.player.width / 2.0 - getWidth() / 2.0,
            0.0,
            Math.max(0.0, gameState.worldWidth - getWidth())
        );
        final double cameraY = clamp(
            gameState.player.y + gameState.player.height / 2.0 - getHeight() / 2.0,
            0.0,
            Math.max(0.0, gameState.worldHeight - getHeight())
        );
        g2d.translate(-cameraX, -cameraY);

        drawPlatforms(g2d);
        drawPlayer(g2d);

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
        if (gameState.platforms.isEmpty()) {
            return;
        }
        g2d.setColor(PLATFORM_COLOR);
        for (final Model.Platform platform : gameState.platforms) {
            g2d.fill(new RoundRectangle2D.Double(platform.x, platform.y, platform.width, platform.height, 12.0, 12.0));
        }
    }

    private void drawPlayer(final Graphics2D g2d) {
        final PlayerSnapshot player = gameState.player;
        final int shadowWidth = (int) Math.round(player.width * 0.8);
        final int shadowHeight = 12;
        final int shadowX = (int) Math.round(player.x + (player.width - shadowWidth) / 2.0);
        final int shadowY = (int) Math.round(player.y + player.height - shadowHeight / 2.0);
        g2d.setColor(new Color(0, 0, 0, player.onGround ? 90 : 40));
        g2d.fillOval(shadowX, shadowY, shadowWidth, shadowHeight);

        final BufferedImage sprite = currentSprite();
        if (sprite != null) {
            final int drawWidth = (int) Math.round(player.width);
            final int drawHeight = (int) Math.round(player.height);
            if (player.facingRight) {
                g2d.drawImage(
                    sprite,
                    (int) Math.round(player.x),
                    (int) Math.round(player.y),
                    drawWidth,
                    drawHeight,
                    null
                );
            } else {
                g2d.drawImage(
                    sprite,
                    (int) Math.round(player.x + player.width),
                    (int) Math.round(player.y),
                    -drawWidth,
                    drawHeight,
                    null
                );
            }
        } else {
            g2d.setColor(new Color(241, 96, 111));
            g2d.fillRoundRect(
                (int) Math.round(player.x),
                (int) Math.round(player.y),
                (int) Math.round(player.width),
                (int) Math.round(player.height),
                16,
                16
            );
            g2d.setColor(OUTLINE_COLOR);
            g2d.setStroke(new BasicStroke(2.0f));
            g2d.drawRoundRect(
                (int) Math.round(player.x),
                (int) Math.round(player.y),
                (int) Math.round(player.width),
                (int) Math.round(player.height),
                16,
                16
            );
        }
    }

    private void drawHud(final Graphics2D g2d) {
        final int padding = 16;
        final int width = 280;
        final int height = 84;
        g2d.setColor(new Color(0, 0, 0, 120));
        g2d.fillRoundRect(padding, padding, width, height, 16, 16);
        g2d.setColor(Color.WHITE);
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 16.0f));
        g2d.drawString("Hop Tales", padding + 16, padding + 28);
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 14.0f));
        g2d.drawString("Move: A / D or Left / Right", padding + 16, padding + 48);
        g2d.drawString("Jump: Space or W / Up", padding + 16, padding + 68);
    }

    private void advanceAnimation() {
        if (playerSprites == null || playerSprites.length == 0) {
            return;
        }
        final long now = System.nanoTime();
        if (now - lastFrameChangeNanos >= FRAME_DURATION_NANOS) {
            currentSpriteIndex = (currentSpriteIndex + 1) % playerSprites.length;
            lastFrameChangeNanos = now;
        }
    }

    private BufferedImage currentSprite() {
        if (playerSprites == null || playerSprites.length == 0) {
            return null;
        }
        return playerSprites[currentSpriteIndex % playerSprites.length];
    }

    private BufferedImage[] loadPlayerSprites() {
        final BufferedImage spriteSheet = loadSprite(PLAYER_SPRITE_SHEET);
        if (spriteSheet == null) {
            return null;
        }
        final int frameWidth = spriteSheet.getWidth();
        final int frameHeight = spriteSheet.getHeight() / PLAYER_FRAME_COUNT;
        if (frameWidth <= 0 || frameHeight <= 0) {
            return null;
        }
        final BufferedImage[] frames = new BufferedImage[PLAYER_FRAME_COUNT];
        for (int i = 0; i < PLAYER_FRAME_COUNT; i++) {
            frames[i] = spriteSheet.getSubimage(0, i * frameHeight, frameWidth, frameHeight);
        }
        return frames;
    }

    private BufferedImage loadSprite(final String name) {
        // Try the classpath first (standard Gradle resources folder)
        try (InputStream stream = View.class.getResourceAsStream("/" + name)) {
            if (stream != null) {
                return ImageIO.read(stream);
            }
        } catch (IOException ignored) {
            // Fallbacks will be tried below
        }
        // Fallback to custom res directory
        final Path path = Path.of("src", "main", "res", name);
        if (Files.exists(path)) {
            try (InputStream stream = Files.newInputStream(path)) {
                return ImageIO.read(stream);
            } catch (IOException ignored) {
                return null;
            }
        }
        return null;
    }

    private double clamp(final double value, final double min, final double max) {
        return Math.max(min, Math.min(max, value));
    }
}
