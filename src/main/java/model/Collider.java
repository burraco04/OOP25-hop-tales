package model;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.objects.api.WorldObject;
import model.objects.impl.brick.Brick;
import model.objects.impl.brick.PowerupBlock;
import model.objects.impl.collectable.Powerup;
import model.entities.api.Enemy;

/**
 * Class responsible for checking and handling the collisions in the game phase.
 */
public final class Collider {
    private final Set<Point> solidTiles;
    private final Set<Point> collectableTiles;
    private final Set<Point> coinTiles;
    private final Set<Point> powerupTiles;
    private final Map<Point, WorldObject> collectableMap;
    private final Set<Point> powerupBlockTiles;
    private final Set<Point> hazardTiles;
    private final List<WorldObject> entities;
    private final List<Enemy> enemies;
    private final int levelId;

    /**
     * Instantiate a {@link Collider}.
     *
     * @param solidTiles set of all the solid tiles.
     * @param collectableTiles set of all the collectable tiles.
     * @param coinTiles set of all the coin tiles.
     * @param powerupTiles set of all the powerup tiles.
     * @param collectableMap map that binds tiles to objects.
     * @param powerupBlockTiles set of all the {@link PowerupBlock} tiles.
     * @param hazardTiles set of all the hazard tiles.
     * @param entities list of all the objects.
     * @param enemies list of all the enemies.
     */
    protected Collider(
        final Set<Point> solidTiles,
        final Set<Point> collectableTiles,
        final Set<Point> coinTiles,
        final Set<Point> powerupTiles,
        final Map<Point, WorldObject> collectableMap,
        final Set<Point> powerupBlockTiles,
        final Set<Point> hazardTiles,
        final List<WorldObject> entities,
        final List<Enemy> enemies,
        final int levelId
    ) {
        this.solidTiles = solidTiles;
        this.collectableTiles = collectableTiles;
        this.coinTiles = coinTiles;
        this.powerupTiles = powerupTiles;
        this.collectableMap = collectableMap;
        this.powerupBlockTiles = powerupBlockTiles;
        this.hazardTiles = hazardTiles;
        this.entities = entities;
        this.enemies = enemies;
        this.levelId = levelId;
    }

    /**
     * Checks if an entity will collide the next update.
     *
     * @param x the next update entity x value.
     * @param y the next update entity y value.
     * @param widthTiles entity width in tiles.
     * @param heightTiles entity height in tiles.
     * @return true if the entity is going to collide.
     */
    public boolean collidesWithSolid(
        final int x,
        final int y,
        final int widthTiles,
        final int heightTiles
    ) {
        for (int dx = 0; dx < widthTiles; dx++) {
            for (int dy = 0; dy < heightTiles; dy++) {
                if (solidTiles.contains(new Point(x + dx, y + dy))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check if the player is colliding with any enemy.
     *
     * @param x the player x tile.
     * @param y the player y tile.
     * @return true if colliding with an enemy.
     */
    public boolean collidesWithEnemy(final int x, final int y) {
        final Rectangle playerRect = new Rectangle(
            x,
            y,
            GameConstants.PLAYER_WIDTH_TILES,
            GameConstants.PLAYER_HEIGHT_TILES
        );
        for (final Enemy enemy : enemies) {
            if (!enemy.isAlive()) {
                continue;
            }
            final int ex = (int) Math.floor(enemy.getX());
            final int ey = (int) Math.floor(enemy.getY());
            final Rectangle enemyRect = new Rectangle(ex, ey, GameConstants.ENEMY_WITDH, GameConstants.ENEMY_HEIGHT);
            if (playerRect.intersects(enemyRect)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the player will collect a coin.
     *
     * @param x the next update player x value.
     * @param y the next update player y value.
     * @return true if the player is collecting a coin.
     */
    public boolean collidesWithCoin(final int x, final int y) {
        for (int dx = 0; dx < GameConstants.PLAYER_WIDTH_TILES; dx++) {
            for (int dy = 0; dy < GameConstants.PLAYER_HEIGHT_TILES; dy++) {
                final Point key = new Point(x + dx, y + dy);
                if (coinTiles.contains(key)) {
                    collectableTiles.remove(key);
                    coinTiles.remove(key);
                    powerupTiles.remove(key);
                    entities.remove(collectableMap.get(key));
                    collectableMap.remove(key, collectableMap.get(key));
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check if the player will collect a powerup.
     *
     * @param x the next update player x value.
     * @param y the next update player y value.
     * @return true if the player is collecting a powerup.
     */
    public boolean collidesWithPowerup(final int x, final int y) {
        for (int dx = 0; dx < GameConstants.PLAYER_WIDTH_TILES; dx++) {
            for (int dy = 0; dy < GameConstants.PLAYER_HEIGHT_TILES; dy++) {
                final Point key = new Point(x + dx, y + dy);
                if (powerupTiles.contains(key)) {
                    collectableTiles.remove(key);
                    coinTiles.remove(key);
                    powerupTiles.remove(key);
                    entities.remove(collectableMap.get(key));
                    collectableMap.remove(key, collectableMap.get(key));
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check if the player will collide with a {@link PowerupBlock} from beneath next update.
     *
     * @param x the next update player x value.
     * @param y the next update player y value.
     * @return true if the player is going to collide.
     */
    public boolean collidesWithPowerupBlockFromBelow(final int x, final int y) {
        if (y <= 0) {
            return false;
        }
        final int checkY = y - 1;
        for (int dx = 0; dx < GameConstants.PLAYER_WIDTH_TILES; dx++) {
            final int blockX = x + dx;
            final Point blockKey = new Point(blockX, checkY);
            if (powerupBlockTiles.contains(blockKey)) {
                powerupBlockTiles.remove(blockKey);
                replaceEntityAt(blockX, checkY, World.POWERUP_BLOCK_TYPE, new Brick(blockX, checkY));
                spawnPowerupAbove(blockX, checkY);
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the player is colliding with a hazard.
     *
     * @param x the next update player x value.
     * @param y the next update player y value.
     * @return true if the player is colliding with a hazard.
     */
    public boolean collidesWithHazard(final int x, final int y) {
        for (int dx = 0; dx < GameConstants.PLAYER_WIDTH_TILES; dx++) {
            for (int dy = 0; dy < GameConstants.PLAYER_HEIGHT_TILES; dy++) {
                if (hazardTiles.contains(new Point(x + dx, y + dy))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Spawn a {@link Powerup} object above the specified position.
     *
     * @param blockX x.
     * @param blockY y.
     */
    private void spawnPowerupAbove(final int blockX, final int blockY) {
        final int powerupY = blockY - 1;
        if (powerupY < 0) {
            return;
        }
        final Point powerupKey = new Point(blockX, powerupY);
        if (solidTiles.contains(powerupKey) || collectableTiles.contains(powerupKey)) {
            return;
        }
        final WorldObject powerup = new Powerup(blockX, powerupY);
        entities.add(powerup);
        collectableTiles.add(powerupKey);
        powerupTiles.add(powerupKey);
        collectableMap.put(powerupKey, powerup);
    }

    /**
     * Replace an object given its position with another object.
     *
     * @param x x value.
     * @param y y value.
     * @param type type of the initial object.
     * @param replacement replacement object.
     */
    private void replaceEntityAt(final int x, final int y, final String type, final WorldObject replacement) {
        for (int i = 0; i < entities.size(); i++) {
            final WorldObject obj = entities.get(i);
            if (type.equals(obj.getType()) && obj.getX() == x && obj.getY() == y) {
                entities.set(i, replacement);
                return;
            }
        }
        entities.add(replacement);
    }
    
    public int getLevelWidth() {
        if (levelId == 1) {
            return GameConstants.LEVEL_1_WIDTH;
        }
        if (levelId == 2) {

            return GameConstants.LEVEL_2_WIDTH;
        } else {
            throw new IllegalArgumentException("levelId non accettabile");
        }

    }
}
