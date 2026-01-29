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
import model.objects.impl.brick.PowerupBlock;

/**
 * create class world.
 */
public class World {
    static final String POWERUP_BLOCK_TYPE = "powerup_block";
    static final Set<String> HAZARD_TYPES = Set.of("lava", "top_lava");
    private static final Set<String> SOLID_TYPES = Set.of("grass", "green_grass", "brick", "floating_grass",
        "floating_grass_left", "floating_grass_right", POWERUP_BLOCK_TYPE
    );
    private static final Set<String> COLLECTABLE_TYPES = Set.of("coin", "powerup");
    private final List<WorldObject> entities = new ArrayList<>();
    private final Set<TileKey> solidTiles = new HashSet<>();
    private final Set<TileKey> collectableTiles = new HashSet<>();
    private final Map<TileKey, WorldObject> collectableMap = new HashMap<>();
    private final Set<TileKey> powerupBlockTiles = new HashSet<>();
    private final Set<TileKey> hazardTiles = new HashSet<>();
    private final Collider collider;
    private final List<Enemy> enemies = new ArrayList<>();
    private final Player player;
    private final CoinManager coinManager;
    private final int levelWidth;

    /**
     * Create a {@link World} object.
     */
    public World() {
        this.player = new PlayerImpl(GameConstants.STARTING_POSITION_X, GameConstants.STARTING_POSITION_Y,
                                     GameConstants.PLAYER_WIDTH_TILES, GameConstants.PLAYER_HEIGHT_TILES);
        this.coinManager = new CoinManager(this);
        this.levelWidth = GameConstants.LEVEL_1_WIDTH;
        this.collider = new Collider(
            solidTiles,
            collectableTiles,
            collectableMap,
            powerupBlockTiles,
            hazardTiles,
            entities
        );
    }

    /**
     * Add all the objects to the world.
     *
     * @param list list of objects.
     */
    public void addEntities(final List<WorldObject> list) {
        entities.addAll(list);
        for (final WorldObject entity : list) {
            if (isSolidType(entity.getType())) {
                solidTiles.add(new TileKey(entity.getX(), entity.getY()));
                if (POWERUP_BLOCK_TYPE.equals(entity.getType())) {
                    powerupBlockTiles.add(new TileKey(entity.getX(), entity.getY()));
                }
            } else if (isHazardType(entity.getType())) {
                hazardTiles.add(new TileKey(entity.getX(), entity.getY()));
            } else if (isCollectableType(entity.getType())) {
                final var tk = new TileKey(entity.getX(), entity.getY());
                collectableMap.put(tk, entity);
                collectableTiles.add(tk);
            }
        }
    }

    /**
     * Add an {@link Enemy} to the world.
     *
     * @param enemy the enemy object.
     */
    public void addEnemy(final Enemy enemy) {
        enemies.add(enemy);
    }

    /**
     * Get all the objects in the World.
     *
     * @return the objects.
     */
    public List<WorldObject> getEntities() {
        return entities;
    }

    /**
     * Get the {@link Player}.
     *
     * @return the {@link Player}.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the CoinManager.
     *
     * @return the CoinManager.
     */
    public CoinManager getCoinManager() {
        return coinManager;
    }

    /**
     * Get the width of the level. 
     *
     * @return the level width.
     */
    public int getLevelWidth() {
        return levelWidth;
    }

    /**
     * Get the list of all enemies.
     *
     * @return the list of enemies.
     */
    public List<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * Checks if the player will collide the next update.
     *
     * @param x the next update player x value.
     * @param y the next update player y value.
     * @return true if the player is going to collide.
     */
    public boolean collidesWithSolid(final int x, final int y) {
        return collider.collidesWithSolid(x, y);
    }

    /**
     * Check if the player will collect a collectable object.
     *
     * @param x the next update player x value.
     * @param y the next update player y value.
     * @return true if the player is collecting any collectable object.
     */
    public boolean collidesWithCollectable(final int x, final int y) {
        return collider.collidesWithCollectable(x, y);
    }

    /**
     * Check if the player will collide with a {@link PowerupBlock} from beneath next update.
     *
     * @param x the next update player x value.
     * @param y the next update player y value.
     * @return true if the player is going to collide.
     */
    public boolean collidesWithPowerupBlockFromBelow(final int x, final int y) {
        return collider.collidesWithPowerupBlockFromBelow(x, y);
    }

    public boolean collidesWithHazard(final int x, final int y) {
        return collider.collidesWithHazard(x, y);
    }

    /**
     * Check if the given type is solid.
     * 
     * @param type the type of the object.
     * @return true if the object is solid.
     */
    private static boolean isSolidType(final String type) {
        return SOLID_TYPES.contains(type);
    }

    /**
     * Check if the given type is collectable.
     *
     * @param type the type of the object.
     * @return true if the object is collectable.
     */
    private static boolean isCollectableType(final String type) {
        return COLLECTABLE_TYPES.contains(type);
    }

    private static boolean isHazardType(final String type) {
        return HAZARD_TYPES.contains(type);
    }

    /**
     * A simple TileKey.
     */
    static final class TileKey {
        private final int x;
        private final int y;

        TileKey(final int x, final int y) {
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
