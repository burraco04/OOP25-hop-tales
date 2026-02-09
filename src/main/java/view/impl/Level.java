package view.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.Timer;

import controller.KeyboardInputManager;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import model.Camera;
import model.CoinStorage;
import model.GameConstants;
import model.World;
import view.utils.Draw;

/**
 * Panel rapresenting the view of the first level.
 */
@SuppressFBWarnings(
    value = {"MC_OVERRIDABLE_METHOD_CALL_IN_CONSTRUCTOR", "EI2"},
    justification = "Swing setup happens in constructor and the view keeps a shared World reference."
)
public class Level extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int MILLISEC = 16;
    private final transient World world;
    private final Timer timer;
    private transient Camera camera;
    private int tileSize;

    /**
     * Create the Level 1 view. 
     *
     * @param world world instance.
     * @param kim KeyboardInputManager instance.
     */
    @SuppressWarnings("PMD.ConstructorCallsOverridableMethod")
    public Level(final World world, final KeyboardInputManager kim) {
        this.world = world;

        this.timer = new Timer(MILLISEC, e -> {
            update();
            repaint();
        });
        this.timer.start();

        setBackground(Color.CYAN);
        this.addKeyListener(kim);
    }

    private void update() {
        if (!world.getPlayer().isAlive()) {
            timer.stop();
            return;
        }

        if (camera == null && getWidth() > 0) {
            camera = new Camera(world.getLevelWidth() * tileSize, getWidth());
        }

        if (camera != null) {
            final int playerWorldX = (int) (world.getPlayer().getX() * tileSize);
            final int playerCenterX = playerWorldX + (tileSize / 2);
            camera.update(
                playerCenterX,
                getWidth()
            );
        }
    }

    private void updateTileSize() {
        final int sizeX = getWidth() / GameConstants.TILE_SIZE_X;
        final int sizeY = getHeight() / GameConstants.TILE_SIZE_Y;

        tileSize = Math.min(sizeX, sizeY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        updateTileSize();

        final long timePassed = System.currentTimeMillis();
        //Se la camera ancora non esiste non disegna niente
        if (camera == null) {
            return;
        }

        final int offsetX = camera.getX();

        for (final var object : world.getEntities()) {
            final var img = Draw.get(object.getType(), timePassed);
            g.drawImage(
                img,
                object.getX() * tileSize - offsetX,
                object.getY() * tileSize,
                tileSize,
                tileSize,
                null
            );
        }

        for (final var enemy : world.getEnemies()) {
            final String enemyName = switch (enemy.getType()) {
                case WALKER -> "walker";
                case JUMPER -> "jumper";
            };

            final var img = Draw.get(enemyName, timePassed);
            g.drawImage(
                img,
                (int) enemy.getX() * tileSize - offsetX,
                (int) enemy.getY() * tileSize,
                tileSize * GameConstants.ENEMY_WITDH,
                tileSize * GameConstants.ENEMY_HEIGHT,
                null
            );
        }

        final String type = world.getPlayer().isHurt() ? "player_hurt" : "player";
        g.drawImage(
            Draw.get(type, timePassed),
            (int) world.getPlayer().getX() * tileSize - offsetX,
            (int) world.getPlayer().getY() * tileSize,
            tileSize * GameConstants.PLAYER_WIDTH_TILES,
            tileSize * GameConstants.PLAYER_HEIGHT_TILES,
            null
        );
        drawHUD(g, timePassed);

    }

    /**
     * Request the input focus for the panel.
     */
    public void focus() {
        this.requestFocusInWindow();
    }

    /**
     * Stop the level timer to prevent background updates.
     */
    public void stop() {
        timer.stop();
    }

    /**
     * Draw on the screen a basic HUD. 
     *
     * @param g {@link Graphics} object used to draw on the panel.
     * @param timePassed used for change the frame of the entities.
     */
    private void drawHUD(final Graphics g, final long timePassed) {
        switch (world.getPlayer().getHealthPoints()) {
            case 3 -> {
                g.drawImage(Draw.get(GameConstants.FULL_HEART, timePassed),
                    tileSize, tileSize, null);
                g.drawImage(Draw.get(GameConstants.FULL_HEART, timePassed),
                    tileSize * 2, tileSize, null);
                g.drawImage(Draw.get(GameConstants.FULL_HEART, timePassed),
                    tileSize * 3, tileSize, null);
            }
            case 2 -> {
                g.drawImage(Draw.get(GameConstants.FULL_HEART, timePassed),
                    tileSize, tileSize, null);
                g.drawImage(Draw.get(GameConstants.FULL_HEART, timePassed),
                    tileSize * 2, tileSize, null);
                g.drawImage(Draw.get(GameConstants.EMPTY_HEART, timePassed),
                    tileSize * 3, tileSize, null);
            }
            case 1 -> {
                g.drawImage(Draw.get(GameConstants.FULL_HEART, timePassed),
                    tileSize, tileSize, null);
                g.drawImage(Draw.get(GameConstants.EMPTY_HEART, timePassed),
                    tileSize * 2, tileSize, null);
                g.drawImage(Draw.get(GameConstants.EMPTY_HEART, timePassed),
                    tileSize * 3, tileSize, null);
            }
            case 0 -> {
                g.drawImage(Draw.get(GameConstants.EMPTY_HEART, timePassed),
                    tileSize, tileSize, null);
                g.drawImage(Draw.get(GameConstants.EMPTY_HEART, timePassed),
                    tileSize * 2, tileSize, null);
                g.drawImage(Draw.get(GameConstants.EMPTY_HEART, timePassed),
                    tileSize * 3, tileSize, null);
            }
            default -> throw new IllegalArgumentException("Illegal health points");
        }
        if (world.getPlayer().hasPowerUp()) {
            g.drawImage(
                Draw.get(GameConstants.FULL_HEART, timePassed),
                tileSize * 4,
                tileSize,
                null
            );
        }
        final Font coinFont = new Font("Arial", Font.BOLD, GameConstants.COIN_COUNT_SIZE);
        g.setFont(coinFont);
        final FontMetrics fm = g.getFontMetrics();
        final int coinX = getWidth() - 2 * tileSize;
        final int coinY = tileSize;
        final int textX = coinX - fm.stringWidth(String.valueOf(CoinStorage.getCoins())) - 10;
        final int textY = coinY + tileSize + fm.getAscent() / 2;
        g.drawString(Integer.toString(CoinStorage.getCoins()), textX, textY);
        g.drawImage(
            Draw.get("coin", timePassed),
            getWidth() - 2 * tileSize,
            tileSize,
            tileSize * 2,
            tileSize * 2,
            null
        );
    }
}
