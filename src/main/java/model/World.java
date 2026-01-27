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

/**
 * create class world.
 */
public class World {
    private static final Set<String> SOLID_TYPES = Set.of("grass", "green_grass", "brick", "floating_grass",
        "floating_grass_left", "floating_grass_right"
    );
    private static final Set<String> COLLECTABLE_TYPES = Set.of("coin", "powerup");
    private final List<WorldObject> entities = new ArrayList<>();
    private final Set<TileKey> solidTiles = new HashSet<>();
    private final Set<TileKey> collectableTiles = new HashSet<>();
    private final Map<TileKey, WorldObject> collectableMap = new HashMap<>();
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

