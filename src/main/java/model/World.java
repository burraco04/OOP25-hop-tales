package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.entities.api.Enemy;
import model.entities.api.Player;
import model.entities.impl.EnemyImpl;
import model.entities.impl.PlayerImpl;
import model.objects.CollectableManager;
import model.objects.api.WorldObject;
import model.objects.impl.brick.PowerupBlock;

/**
 * create class world.
 */
public class World {
    static final String POWERUP_BLOCK_TYPE = "powerup_block";
    static final Set<String> HAZARD_TYPES = Set.of("lava", "top_lava", "water", "water_top");
    private static final Set<String> SOLID_TYPES = Set.of("grass", "green_grass", "brick", "floating_grass",
        "floating_grass_left", "floating_grass_right", "block_planks", "dirt_block", "top_dirt_block", "floating_dirt_middle", 
        "floating_dirt_left", "floating_dirt_right", POWERUP_BLOCK_TYPE
    );
    private static final Set<String> COIN_TYPES = Set.of("coin");
    private static final Set<String> POWERUP_TYPES = Set.of("powerup");
    private final List<WorldObject> entities = new ArrayList<>();
    private final Set<Point> solidTiles = new HashSet<>();
    private final Set<Point> collectableTiles = new HashSet<>();
    private final Set<Point> coinTiles = new HashSet<>();
    private final Set<Point> powerupTiles = new HashSet<>();
    private final Map<Point, WorldObject> collectableMap = new HashMap<>();
    private final Set<Point> powerupBlockTiles = new HashSet<>();
    private final Set<Point> hazardTiles = new HashSet<>();
    private final Collider collider;
    private final List<Enemy> enemies = new ArrayList<>();
    private final Player player;
    private final CollectableManager coinManager;
    private final int levelWidth;
    private final int levelId;
    private final String jsonPath;

    /**
     * Create a {@link World} object.
     */
    public World(final int levelId) {
        this.levelId = levelId;

        this.levelWidth = switch (levelId) {
            case 1 -> GameConstants.LEVEL_1_WIDTH;
            case 2 -> GameConstants.LEVEL_2_WIDTH;
            default -> GameConstants.LEVEL_1_WIDTH;
        };
        this.jsonPath = switch (levelId) {
            case 1 -> "levels/Level1.json";
            case 2 -> "levels/Level2.json";
            default -> "levels/Level1.json";
        };
        this.player = new PlayerImpl(GameConstants.STARTING_POSITION_X, GameConstants.STARTING_POSITION_Y,
                                     GameConstants.PLAYER_WIDTH_TILES, GameConstants.PLAYER_HEIGHT_TILES);
        this.coinManager = new CollectableManager(this);
        this.collider = new Collider(
            solidTiles,
            collectableTiles,
            coinTiles,
            powerupTiles,
            collectableMap,
            powerupBlockTiles,
            hazardTiles,
            entities,
            enemies
        );
    }

    /**
     * Informs what level the World is representing.
     *
     * @return the leveId.
     */
    public int getLevelId() {
        return levelId;
    }

    /**
     * Get the relative path to the file containing the data to load.
     * 
     * @return the path.
     */
    public String getJsonPath() {
        return jsonPath;
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
                solidTiles.add(new Point(entity.getX(), entity.getY()));
                if (POWERUP_BLOCK_TYPE.equals(entity.getType())) {
                    powerupBlockTiles.add(new Point(entity.getX(), entity.getY()));
                }
            } else if (isHazardType(entity.getType())) {
                hazardTiles.add(new Point(entity.getX(), entity.getY()));
            } else if (isCollectableType(entity.getType())) {
                final var tk = new Point(entity.getX(), entity.getY());
                collectableMap.put(tk, entity);
                collectableTiles.add(tk);
                if (isCoinType(entity.getType())) {
                    coinTiles.add(tk);
                } else if (isPowerupType(entity.getType())) {
                    powerupTiles.add(tk);
                }
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
        if (enemy instanceof EnemyImpl enemyImpl) {
            enemyImpl.setCollider(collider);
        }
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
    public CollectableManager getCoinManager() {
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
     * Checks if the entity will collide the next update.
     *
     * @param x the next update entity x value.
     * @param y the next update entity y value.
     * @param widthTiles the width of the entity.
     * @return true if the entity is going to collide.
     */
    public boolean collidesWithSolid(
        final int x,
        final int y,
        final int widthTiles,
        final int heightTiles
    ) {
        return collider.collidesWithSolid(x, y, widthTiles, heightTiles);
    }

    /**
     * Check if the player will collect a coin.
     *
     * @param x the next update player x value.
     * @param y the next update player y value.
     * @return true if the player is collecting a coin.
     */
    public boolean collidesWithCoin(final int x, final int y) {
        return collider.collidesWithCoin(x, y);
    }

    /**
     * Check if the player will collect a powerup.
     *
     * @param x the next update player x value.
     * @param y the next update player y value.
     * @return true if the player is collecting a powerup.
     */
    public boolean collidesWithPowerup(final int x, final int y) {
        return collider.collidesWithPowerup(x, y);
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

    /**
     * Check if the player is colliding with a hazard.
     *
     * @param x the next update player x value.
     * @param y the next update player y value.
     * @return true if the player is colliding with a hazard.
     */
    public boolean collidesWithHazard(final int x, final int y) {
        return collider.collidesWithHazard(x, y);
    }

    /**
     * Check if the player is colliding with any enemy.
     *
     * @param x the player x tile.
     * @param y the player y tile.
     * @return true if colliding with an enemy.
     */
    public boolean collidesWithEnemy(final int x, final int y) {
        return collider.collidesWithEnemy(x, y);
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
        return COIN_TYPES.contains(type) || POWERUP_TYPES.contains(type);
    }

    /**
     * Check if the given type is a Coin.
     *
     * @param type the type of the object.
     * @return true if the object is a Coin.
     */
    private static boolean isCoinType(final String type) {
        return COIN_TYPES.contains(type);
    }

    /**
     * Check if the given type is a Powerup.
     *
     * @param type the type of the object.
     * @return true if the object is a Powerup.
     */
    private static boolean isPowerupType(final String type) {
        return POWERUP_TYPES.contains(type);
    }
    /**
     * Check if the given type is a Hazard.
     *
     * @param type the type of the object.
     * @return true if the object is a Hazard.
     */
    private static boolean isHazardType(final String type) {
        return HAZARD_TYPES.contains(type);
    }

}
