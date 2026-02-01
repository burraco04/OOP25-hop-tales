package view.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.Timer;

import controller.KeyboardInputManager;
import controller.deserialization.level.EntityData;
import controller.deserialization.level.EntityFactory;
import controller.deserialization.level.LevelData;
import controller.deserialization.level.LevelLoader;
import model.Camera;
import model.GameConstants;
import model.World;
import model.objects.CollectableManager;
import view.utils.Draw;

/**
 * Panel rapresenting the view of the first level.
 */
public class Level extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int MILLISEC = 16;
    private final World world;
    private final Timer timer;
    private Camera camera;
    private final KeyboardInputManager kim;

    /**
     * Create the Level 1 view. 
     *
     * @param levelPath string containing the path of the file that contains Level 1 data.
     * @param world world instance.
     * @param kim KeyboardInputManager instance.
     */
    public Level(final String levelPath, final World world, final KeyboardInputManager kim) {
        this.world = world;
        this.kim = kim;
        final LevelData data = LevelLoader.load(levelPath);

        world.getPlayer().setX(data.getSpawnPointX());
        world.getPlayer().setY(data.getSpawnPointY());

        for (final EntityData e : data.getEntities()) {
           world.addEntities(EntityFactory.create(e));
        }

        for (final EntityData e : data.getEnemies()) {
           world.addEnemy(EntityFactory.createEnemy(e));
        }

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
        camera = new Camera(world.getLevelWidth() * GameConstants.TILE_SIZE, getWidth());
    }

    if (camera != null) {
        final int playerWorldX = (int) (world.getPlayer().getX() * GameConstants.TILE_SIZE);
        final int playerCenterX = playerWorldX + (GameConstants.TILE_SIZE / 2);
        camera.update(
            playerCenterX,
            getWidth()
        );
    }
}

/**
 * {@inheritDoc}
 */
@Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);

        final long timePassed = System.currentTimeMillis();
        //Se la camera ancora non esiste non disegna niente
        if (camera == null) {
            return;
        }

        drawHUD(g, timePassed);
        final int offsetX = camera.getX();
        

        for (final var object : world.getEntities()) {
            final var img = Draw.get(object.getType(), timePassed);
            g.drawImage(img, object.getX() * GameConstants.TILE_SIZE - offsetX, object.getY() * GameConstants.TILE_SIZE,
            GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, null);
        }

        for (final var enemy : world.getEnemies()) {
            final String enemyName = switch (enemy.getType()) {
                case WALKER -> "walker";
                case JUMPER -> "jumper";
                default -> "walker";
            };

            final var img = Draw.get(enemyName, timePassed);
            g.drawImage(img, (int) enemy.getX() * GameConstants.TILE_SIZE - offsetX, (int) enemy.getY() * GameConstants.TILE_SIZE,
            GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, null);
        }

        final String type = world.getPlayer().isHurt() ? "player_hurt" : "player";
        g.drawImage(Draw.get(type, timePassed), (int) world.getPlayer().getX() * GameConstants.TILE_SIZE - offsetX,
        (int) world.getPlayer().getY() * GameConstants.TILE_SIZE,
        GameConstants.TILE_SIZE * GameConstants.PLAYER_WIDTH_TILES,
        GameConstants.TILE_SIZE * GameConstants.PLAYER_HEIGHT_TILES, null);
    }

    /**
     * Request the input focus for the panel.
     */
    public void focus() {
        this.requestFocusInWindow();
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
                    g.drawImage(Draw.get("full_heart", timePassed), GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, null);
                    g.drawImage(Draw.get("full_heart", timePassed), GameConstants.TILE_SIZE * 2, GameConstants.TILE_SIZE, null);
                    g.drawImage(Draw.get("full_heart", timePassed), GameConstants.TILE_SIZE * 3, GameConstants.TILE_SIZE, null);
                    }
            case 2 -> { 
                    g.drawImage(Draw.get("full_heart", timePassed), GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, null);
                    g.drawImage(Draw.get("full_heart", timePassed), GameConstants.TILE_SIZE * 2, GameConstants.TILE_SIZE, null);
                    g.drawImage(Draw.get("empty_heart", timePassed), GameConstants.TILE_SIZE * 3, GameConstants.TILE_SIZE, null);
                    }
            case 1 -> { 
                    g.drawImage(Draw.get("full_heart", timePassed), GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, null);
                    g.drawImage(Draw.get("empty_heart", timePassed), GameConstants.TILE_SIZE * 2, GameConstants.TILE_SIZE, null);
                    g.drawImage(Draw.get("empty_heart", timePassed), GameConstants.TILE_SIZE * 3, GameConstants.TILE_SIZE, null);
                    }
            case 0 -> {
                    g.drawImage(Draw.get("empty_heart", timePassed), GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, null);
                    g.drawImage(Draw.get("empty_heart", timePassed), GameConstants.TILE_SIZE * 2, GameConstants.TILE_SIZE, null);
                    g.drawImage(Draw.get("empty_heart", timePassed), GameConstants.TILE_SIZE * 3, GameConstants.TILE_SIZE, null);
                    }
            default -> throw new IllegalArgumentException("Illegal health points");
        }
        Font coinFont = new Font("Arial", Font.BOLD, 28);
        g.setFont(coinFont);
        FontMetrics fm = g.getFontMetrics();
        final int coinX = getWidth() - 2 * GameConstants.TILE_SIZE;
        final int coinY = GameConstants.TILE_SIZE;
        final int textX = coinX - fm.stringWidth(String.valueOf(CollectableManager.getCoins())) - 10;
        final int textY = coinY + GameConstants.TILE_SIZE + fm.getAscent() / 2;
        g.drawString(Integer.toString(CollectableManager.getCoins()), textX, textY);
        g.drawImage(Draw.get("coin", timePassed), getWidth() - 2 * GameConstants.TILE_SIZE, GameConstants.TILE_SIZE,
                    GameConstants.TILE_SIZE * 2, GameConstants.TILE_SIZE * 2, null);
    }
}
