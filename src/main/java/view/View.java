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
import model.entities.api.PlayerSnapshot;

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
    private static final double HALF_RATIO = 0.5;
    private static final double CAMERA_HALF = HALF_RATIO;

    private static final long FRAME_DURATION_NANOS = 150_000_000L;
    private static final double MIN_CAMERA_OFFSET = 0.0;
    private static final float LOADING_FONT_SIZE = 28.0f;
    private static final int CENTER_DIVISOR = 2;
    private static final double PLATFORM_CORNER_RADIUS = 12.0;
    private static final double HAZARD_CORNER_RADIUS = 10.0;
    private static final float COIN_STROKE_WIDTH = 1.5f;
    private static final double COIN_DIAMETER_MULTIPLIER = 2.0;
    private static final double SHADOW_WIDTH_RATIO = 0.8;
    private static final int PLAYER_CORNER_RADIUS = 16;
    private static final float PLAYER_OUTLINE_WIDTH = 2.0f;
    private static final int HUD_CORNER_RADIUS = 16;
    private static final float HUD_TITLE_FONT_SIZE = 16.0f;
    private static final float HUD_BODY_FONT_SIZE = 14.0f;
    private static final int HUD_TEXT_X_OFFSET = 16;
    private static final int HUD_TITLE_Y_OFFSET = 28;
    private static final int HUD_MOVE_Y_OFFSET = 48;
    private static final int HUD_JUMP_Y_OFFSET = 68;
    private static final int HUD_COINS_Y_OFFSET = 88;
    private static final int HUD_HEALTH_TEXT_Y_OFFSET = 108;
    private static final int HUD_HEALTH_BAR_Y_OFFSET = 116;
    private static final double MIN_HEALTH_VALUE = 0.0;
    private static final double MIN_FILL_RATIO = 0.0;
    private static final double MAX_FILL_RATIO = 1.0;
    private static final int HEALTH_PIP_SPACING = 26;
    private static final int HEALTH_PIP_WIDTH = 20;
    private static final int HEALTH_PIP_HEIGHT = 10;
    private static final int HEALTH_PIP_ARC = 8;
    private static final int NO_FRAMES = 0;
    private static final int FRAME_INDEX_INCREMENT = 1;
    private static final int FRAME_SPLIT_FACTOR = 2;
    private static final int ORIGIN = 0;
    private static final BufferedImage[] EMPTY_FRAMES = new BufferedImage[0];

    private transient BufferedImage[] playerFrames;
    private transient int currentFrameIndex;
    private transient long lastFrameUpdate;
    private transient GameState gameState;

    /**
     * Creates a view and loads player animation frames.
     */
    public View() {
        setPreferredSize(new Dimension(Model.WORLD_WIDTH, Model.WORLD_HEIGHT));
        setBackground(SKY_COLOR);
        setDoubleBuffered(true);
        setFocusable(true);
        this.playerFrames = loadPlayerFrames();
        this.lastFrameUpdate = System.nanoTime();
    }

    /**
     * Updates the state to render and schedules a repaint.
     *
     * @param state latest game state snapshot
     */
    public void setGameState(final GameState state) {
        SwingUtilities.invokeLater(() -> {
            this.gameState = state;
            advanceAnimation();
            repaint();
        });
    }

    /**
     * Renders the current game frame.
     *
     * @param g graphics context
     */
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
            MIN_CAMERA_OFFSET,
            Math.max(MIN_CAMERA_OFFSET, gameState.getWorldWidth() - getWidth())
        );
        final double cameraY = clamp(
            player.getY() + player.getHeight() * CAMERA_HALF - getHeight() * CAMERA_HALF,
            MIN_CAMERA_OFFSET,
            Math.max(MIN_CAMERA_OFFSET, gameState.getWorldHeight() - getHeight())
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

    /**
     * Draws a centered loading message when no state is available.
     *
     * @param g2d graphics context
     */
    private void drawLoadingMessage(final Graphics2D g2d) {
        g2d.setColor(Color.DARK_GRAY);
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, LOADING_FONT_SIZE));
        final String message = "Loading world...";
        final int width = g2d.getFontMetrics().stringWidth(message);
        g2d.drawString(message, (getWidth() - width) / CENTER_DIVISOR, getHeight() / CENTER_DIVISOR);
    }

    /**
     * Draws the platforms from the game state.
     *
     * @param g2d graphics context
     */
    private void drawPlatforms(final Graphics2D g2d) {
        g2d.setColor(PLATFORM_COLOR);
        for (final Platform platform : gameState.getPlatforms()) {
            g2d.fill(new RoundRectangle2D.Double(
                platform.getX(),
                platform.getY(),
                platform.getWidth(),
                platform.getHeight(),
                PLATFORM_CORNER_RADIUS,
                PLATFORM_CORNER_RADIUS
            ));
        }
    }

    /**
     * Draws the hazard zones from the game state.
     *
     * @param g2d graphics context
     */
    private void drawHazards(final Graphics2D g2d) {
        g2d.setColor(HAZARD_COLOR);
        for (final HazardSnapshot hazard : gameState.getHazards()) {
            g2d.fill(new RoundRectangle2D.Double(
                hazard.getX(),
                hazard.getY(),
                hazard.getWidth(),
                hazard.getHeight(),
                HAZARD_CORNER_RADIUS,
                HAZARD_CORNER_RADIUS
            ));
        }
    }

    /**
     * Draws all uncollected coins.
     *
     * @param g2d graphics context
     */
    private void drawCoins(final Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(COIN_STROKE_WIDTH));
        for (final CoinSnapshot coin : gameState.getCoins()) {
            if (coin.isCollected()) {
                continue;
            }
            final double diameter = coin.getRadius() * COIN_DIAMETER_MULTIPLIER;
            final int drawX = (int) Math.round(coin.getCenterX() - coin.getRadius());
            final int drawY = (int) Math.round(coin.getCenterY() - coin.getRadius());
            final int drawDiameter = (int) Math.round(diameter);
            g2d.setColor(COIN_FILL);
            g2d.fillOval(drawX, drawY, drawDiameter, drawDiameter);
            g2d.setColor(COIN_BORDER);
            g2d.drawOval(drawX, drawY, drawDiameter, drawDiameter);
        }
    }

    /**
     * Draws the player sprite, shadow, and fallback rectangle.
     *
     * @param g2d graphics context
     * @param player player snapshot
     */
    private void drawPlayer(final Graphics2D g2d, final PlayerSnapshot player) {
        final int shadowWidth = (int) Math.round(player.getWidth() * SHADOW_WIDTH_RATIO);
        final int shadowX = (int) Math.round(player.getX() + (player.getWidth() - shadowWidth) * HALF_RATIO);
        final int shadowY = (int) Math.round(player.getY() + player.getHeight() - SHADOW_HEIGHT * HALF_RATIO);
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
            g2d.fillRoundRect(drawX, drawY, drawWidth, drawHeight, PLAYER_CORNER_RADIUS, PLAYER_CORNER_RADIUS);
            g2d.setColor(PLAYER_OUTLINE);
            g2d.setStroke(new BasicStroke(PLAYER_OUTLINE_WIDTH));
            g2d.drawRoundRect(drawX, drawY, drawWidth, drawHeight, PLAYER_CORNER_RADIUS, PLAYER_CORNER_RADIUS);
        }
    }

    /**
     * Draws the HUD with controls and player stats.
     *
     * @param g2d graphics context
     */
    private void drawHud(final Graphics2D g2d) {
        g2d.setColor(HUD_BACKGROUND);
        g2d.fillRoundRect(HUD_PADDING, HUD_PADDING, HUD_WIDTH, HUD_HEIGHT, HUD_CORNER_RADIUS, HUD_CORNER_RADIUS);
        g2d.setColor(HUD_TEXT_COLOR);
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, HUD_TITLE_FONT_SIZE));
        g2d.drawString("Hop Tales", HUD_PADDING + HUD_TEXT_X_OFFSET, HUD_PADDING + HUD_TITLE_Y_OFFSET);
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, HUD_BODY_FONT_SIZE));
        g2d.drawString(
            "Move: A / D or Left / Right",
            HUD_PADDING + HUD_TEXT_X_OFFSET,
            HUD_PADDING + HUD_MOVE_Y_OFFSET
        );
        g2d.drawString(
            "Jump: Space or W / Up",
            HUD_PADDING + HUD_TEXT_X_OFFSET,
            HUD_PADDING + HUD_JUMP_Y_OFFSET
        );
        g2d.drawString(
            String.format("Coins: %d / %d", gameState.getCoinsCollected(), gameState.getTotalCoins()),
            HUD_PADDING + HUD_TEXT_X_OFFSET,
            HUD_PADDING + HUD_COINS_Y_OFFSET
        );
        g2d.drawString(
            String.format("Health: %.1f / %.1f", gameState.getHealth(), gameState.getMaxHealth()),
            HUD_PADDING + HUD_TEXT_X_OFFSET,
            HUD_PADDING + HUD_HEALTH_TEXT_Y_OFFSET
        );
        drawHealthBar(
            g2d,
            HUD_PADDING + HUD_TEXT_X_OFFSET,
            HUD_PADDING + HUD_HEALTH_BAR_Y_OFFSET,
            gameState.getHealth(),
            gameState.getMaxHealth()
        );
    }

    /**
     * Draws a segmented health bar.
     *
     * @param g2d graphics context
     * @param startX left coordinate of the bar
     * @param startY top coordinate of the bar
     * @param health current health value
     * @param maxHealth maximum health value
     */
    private void drawHealthBar(
        final Graphics2D g2d,
        final int startX,
        final int startY,
        final double health,
        final double maxHealth
    ) {
        final int segments = (int) Math.round(maxHealth);
        final double clampedHealth = Math.max(MIN_HEALTH_VALUE, Math.min(health, maxHealth));
        for (int index = 0; index < segments; index++) {
            final double remaining = clampedHealth - index;
            final double fillRatio = Math.max(MIN_FILL_RATIO, Math.min(MAX_FILL_RATIO, remaining));
            final int pipX = startX + index * HEALTH_PIP_SPACING;
            g2d.setColor(HEALTH_BAR_FILL);
            g2d.fillRoundRect(
                pipX,
                startY,
                (int) Math.round(HEALTH_PIP_WIDTH * fillRatio),
                HEALTH_PIP_HEIGHT,
                HEALTH_PIP_ARC,
                HEALTH_PIP_ARC
            );
            g2d.setColor(Color.WHITE);
            g2d.drawRoundRect(
                pipX,
                startY,
                HEALTH_PIP_WIDTH,
                HEALTH_PIP_HEIGHT,
                HEALTH_PIP_ARC,
                HEALTH_PIP_ARC
            );
        }
    }

    /**
     * Advances the animation frame when enough time has elapsed.
     */
    private void advanceAnimation() {
        if (playerFrames == null || playerFrames.length == NO_FRAMES) {
            return;
        }
        final long now = System.nanoTime();
        if (now - lastFrameUpdate >= FRAME_DURATION_NANOS) {
            currentFrameIndex = (currentFrameIndex + FRAME_INDEX_INCREMENT) % playerFrames.length;
            lastFrameUpdate = now;
        }
    }

    /**
     * Returns the current player animation frame.
     *
     * @return the current frame, or {@code null} if none are available
     */
    private BufferedImage currentFrame() {
        if (playerFrames == null || playerFrames.length == NO_FRAMES) {
            return null;
        }
        return playerFrames[currentFrameIndex];
    }

    /**
     * Loads player animation frames from resources or the filesystem.
     *
     * @return the loaded frames, or an empty array when unavailable
     */
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
                return EMPTY_FRAMES;
            }
        }
        return EMPTY_FRAMES;
    }

    /**
     * Splits a sprite sheet into two animation frames.
     *
     * @param sheet sprite sheet image
     * @return the extracted frames, or an empty array if unavailable
     */
    private BufferedImage[] sliceFrames(final BufferedImage sheet) {
        if (sheet == null) {
            return EMPTY_FRAMES;
        }
        if (sheet.getWidth() >= sheet.getHeight() * FRAME_SPLIT_FACTOR) {
            final int frameWidth = sheet.getWidth() / FRAME_SPLIT_FACTOR;
            final int frameHeight = sheet.getHeight();
            return new BufferedImage[] {
                sheet.getSubimage(ORIGIN, ORIGIN, frameWidth, frameHeight),
                sheet.getSubimage(frameWidth, ORIGIN, frameWidth, frameHeight)
            };
        } else {
            final int frameWidth = sheet.getWidth();
            final int frameHeight = sheet.getHeight() / FRAME_SPLIT_FACTOR;
            return new BufferedImage[] {
                sheet.getSubimage(ORIGIN, ORIGIN, frameWidth, frameHeight),
                sheet.getSubimage(ORIGIN, frameHeight, frameWidth, frameHeight)
            };
        }
    }

    /**
     * Clamps a value between the provided bounds.
     *
     * @param value value to clamp
     * @param min lower bound
     * @param max upper bound
     * @return the clamped value
     */
    private double clamp(final double value, final double min, final double max) {
        return Math.max(min, Math.min(max, value));
    }
}
