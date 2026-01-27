package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import model.entities.api.Player;
import model.entities.api.Enemy;
import model.entities.impl.PlayerImpl;
import model.objects.CoinManager;
import model.objects.api.WorldObject;
import model.objects.impl.brick.Brick;
import model.objects.impl.collectable.Powerup;

/**
 * create class world.
 */
public class World {
    private static final String POWERUP_BLOCK_TYPE = "powerup_block";
    private static final Set<String> SOLID_TYPES = Set.of("grass", "green_grass", "brick", "floating_grass",
        "floating_grass_left", "floating_grass_right", POWERUP_BLOCK_TYPE
    );
    private static final Set<String> COLLECTABLE_TYPES = Set.of("coin", "powerup");
    private final List<WorldObject> entities = new ArrayList<>();
    private final Set<TileKey> solidTiles = new HashSet<>();
    private final Set<TileKey> collectableTiles = new HashSet<>();
    private final Map<TileKey, WorldObject> collectableMap = new HashMap<>();
    private final Set<TileKey> powerupBlockTiles = new HashSet<>();
    private final List<Enemy> enemies = new ArrayList<>();
    private final Player player;
    private final CoinManager coinManager;
    private final int levelWidth;

    public World() {
        this.player = new PlayerImpl(GameConstants.STARTING_POSITION_X, GameConstants.STARTING_POSITION_Y,
                                     GameConstants.PLAYER_WIDTH, GameConstants.PLAYER_HEIGHT);
        this.coinManager = new CoinManager(this);
        this.levelWidth = GameConstants.LEVEL_1_WIDTH;

    }
    //Serve anche il player per la camera

    /**
     * add all the entities at the world
     *
     * @param list list of entitties
     */
    public void addEntities(final List<WorldObject> list) {
        entities.addAll(list);
        for (final WorldObject entity : list) {
            if (isSolidType(entity.getType())) {
                solidTiles.add(new TileKey(entity.getX(), entity.getY()));
                if (POWERUP_BLOCK_TYPE.equals(entity.getType())) {
                    powerupBlockTiles.add(new TileKey(entity.getX(), entity.getY()));
                }
            } else if (isCollectableType(entity.getType())) {
                var tk = new TileKey(entity.getX(), entity.getY());
                collectableMap.put(tk, entity);
                collectableTiles.add(tk);
            }
        }
    }

    /**
     * add all the enemies at the world
     *
     * @param list list of entitties
     */
    public void addEnemy(final Enemy enemy) {
        enemies.add(enemy);
    }

    /**
     * return the entities
     *
     * @return the entities
     */
    public List<WorldObject> getEntities() {
        return entities;
    }

    public Player getPlayer() {
        return player;
    }

    public CoinManager getCoinManager() {
        return coinManager;
    }

    public int getLevelWidth() {
        return levelWidth;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public boolean collidesWithSolid(final int x, final int y, final int width, final int height) {
        for (int dx = 0; dx < width; dx++) {
            for (int dy = 0; dy < height; dy++) {
                if (solidTiles.contains(new TileKey(x + dx, y + dy))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean collidesWithCollectable(final int x, final int y, final int width, final int height) {
        for (int dx = 0; dx < width; dx++) {
            for (int dy = 0; dy < height; dy++) {
                if (collectableTiles.contains(new TileKey(x + dx, y + dy))) {
                    var tk = new TileKey(x + dx, y + dy);
                    collectableTiles.remove(tk);
                    entities.remove(collectableMap.get(tk));
                    collectableMap.remove(tk, collectableMap.get(tk));
                    return true;
                }
            }
        }
        return false;
    }

    public boolean collidesWithPowerupBlockFromBelow(final int x, final int y, final int width, final int height) {
        if (y <= 0) {
            return false;
        }
        final int checkY = y - 1;
        for (int dx = 0; dx < width; dx++) {
            final int blockX = x + dx;
            final TileKey blockKey = new TileKey(blockX, checkY);
            if (powerupBlockTiles.contains(blockKey)) {
                powerupBlockTiles.remove(blockKey);
                replaceEntityAt(blockX, checkY, POWERUP_BLOCK_TYPE, new Brick(blockX, checkY));
                spawnPowerupAbove(blockX, checkY);
                return true;
            }
        }
        return false;
    }

    private void spawnPowerupAbove(final int blockX, final int blockY) {
        final int powerupY = blockY - 1;
        if (powerupY < 0) {
            return;
        }
        final TileKey powerupKey = new TileKey(blockX, powerupY);
        if (solidTiles.contains(powerupKey) || collectableTiles.contains(powerupKey)) {
            return;
        }
        final WorldObject powerup = new Powerup(blockX, powerupY);
        entities.add(powerup);
        collectableTiles.add(powerupKey);
        collectableMap.put(powerupKey, powerup);
    }

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

    private static boolean isSolidType(final String type) {
        return SOLID_TYPES.contains(type);
    }

    private static boolean isCollectableType(final String type) {
        return COLLECTABLE_TYPES.contains(type);
    }

    private static final class TileKey {
        private final int x;
        private final int y;

        private TileKey(final int x, final int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final TileKey other = (TileKey) obj;
            return x == other.x && y == other.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
