package model;

import model.entities.api.Player;
import model.entities.api.PlayerSnapshot;
import model.entities.impl.PlayerImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Central game model that exposes an immutable snapshot for the view layer.
 */
public class Model {
    public static final int WORLD_WIDTH = 960;
    public static final int WORLD_HEIGHT = 540;

    private static final double PLAYER_START_X_RATIO = 0.25;
    private static final double PLAYER_START_Y_OFFSET_FROM_BOTTOM = 120.0;
    private static final double PLAYER_WIDTH = 48.0;
    private static final double PLAYER_HEIGHT = 64.0;

    private static final double FLOOR_HEIGHT = 40.0;
    private static final double PLATFORM_HEIGHT = 24.0;
    private static final double COIN_RADIUS = 12.0;
    private static final double HAZARD_DAMAGE_PER_SECOND = 1.5;
    private static final int COIN_VALUE = 1;
    private static final int MAX_HEALTH = 5;
    private static final double MIN_DELTA_SECONDS = 0.0;
    private static final double NO_DAMAGE = 0.0;
    private static final double MIN_HEALTH = 0.0;
    private static final double LEVEL_ORIGIN_X = 0.0;

    private static final double PLAYER_START_X = WORLD_WIDTH * PLAYER_START_X_RATIO;
    private static final double PLAYER_START_Y = WORLD_HEIGHT - PLAYER_START_Y_OFFSET_FROM_BOTTOM;

    private static final double PLATFORM_TWO_X = 160.0;
    private static final double PLATFORM_TWO_Y_OFFSET_FROM_BOTTOM = 150.0;
    private static final double PLATFORM_TWO_WIDTH = 160.0;
    private static final double PLATFORM_THREE_X = 420.0;
    private static final double PLATFORM_THREE_Y_OFFSET_FROM_BOTTOM = 240.0;
    private static final double PLATFORM_THREE_WIDTH = 200.0;
    private static final double PLATFORM_FOUR_X = 740.0;
    private static final double PLATFORM_FOUR_Y_OFFSET_FROM_BOTTOM = 320.0;
    private static final double PLATFORM_FOUR_WIDTH = 180.0;
    private static final double PLATFORM_FIVE_X = 60.0;
    private static final double PLATFORM_FIVE_Y_OFFSET_FROM_BOTTOM = 280.0;
    private static final double PLATFORM_FIVE_WIDTH = 140.0;
    private static final double PLATFORM_SIX_X = 540.0;
    private static final double PLATFORM_SIX_Y_OFFSET_FROM_BOTTOM = 110.0;
    private static final double PLATFORM_SIX_WIDTH = 140.0;

    private static final double COIN_ONE_X = 210.0;
    private static final double COIN_ONE_Y_OFFSET_FROM_BOTTOM = 190.0;
    private static final double COIN_TWO_X = 470.0;
    private static final double COIN_TWO_Y_OFFSET_FROM_BOTTOM = 270.0;
    private static final double COIN_THREE_X = 780.0;
    private static final double COIN_THREE_Y_OFFSET_FROM_BOTTOM = 340.0;
    private static final double COIN_FOUR_X = 120.0;
    private static final double COIN_FOUR_Y_OFFSET_FROM_BOTTOM = 310.0;
    private static final double COIN_FIVE_X = 600.0;
    private static final double COIN_FIVE_Y_OFFSET_FROM_BOTTOM = 140.0;

    private static final double HAZARD_ONE_X = 320.0;
    private static final double HAZARD_TWO_X = 660.0;
    private static final double HAZARD_WIDTH = 80.0;

    private final List<Platform> platforms = new ArrayList<>();
    private final List<Coin> coins = new ArrayList<>();
    private final List<HazardZone> hazards = new ArrayList<>();
    private final Player player;

    private PlayerSnapshot lastPlayerSnapshot;
    private double health = MAX_HEALTH;
    private int coinsCollected;
    private boolean gameOver;

    /**
     * Creates a model instance and loads the initial level state.
     */
    public Model() {
        initialiseLevel();
        this.player = new PlayerImpl(
            PLAYER_START_X,
            PLAYER_START_Y,
            PLAYER_WIDTH,
            PLAYER_HEIGHT
        );
        this.lastPlayerSnapshot = player.snapshot();
    }

    /**
     * Updates the pressed state of the left movement input.
     *
     * @param pressed {@code true} when left movement is pressed
     */
    public synchronized void setLeftPressed(final boolean pressed) {
        player.setLeftPressed(pressed);
    }

    /**
     * Updates the pressed state of the right movement input.
     *
     * @param pressed {@code true} when right movement is pressed
     */
    public synchronized void setRightPressed(final boolean pressed) {
        player.setRightPressed(pressed);
    }

    /**
     * Queues a jump attempt for the next update.
     */
    public synchronized void queueJump() {
        player.queueJump();
    }

    /**
     * Advances the simulation by the provided delta.
     *
     * @param deltaSeconds elapsed time in seconds since the last update
     */
    public synchronized void update(final double deltaSeconds) {
        if (deltaSeconds <= MIN_DELTA_SECONDS || gameOver) {
            return;
        }
        player.update(deltaSeconds);
        lastPlayerSnapshot = player.snapshot();
        collectCoins(lastPlayerSnapshot);
        applyHazards(lastPlayerSnapshot, deltaSeconds);
    }

    /**
     * Builds an immutable snapshot of the current world state.
     *
     * @return the current game state snapshot
     */
    public synchronized GameState snapshot() {
        final List<CoinSnapshot> coinSnapshots = coins.stream()
            .map(Coin::snapshot)
            .collect(Collectors.toList());
        final List<HazardSnapshot> hazardSnapshots = hazards.stream()
            .map(HazardZone::snapshot)
            .collect(Collectors.toList());
        return new GameState(
            lastPlayerSnapshot,
            platforms,
            coinSnapshots,
            hazardSnapshots,
            WORLD_WIDTH,
            WORLD_HEIGHT,
            health,
            MAX_HEALTH,
            coinsCollected,
            coins.size(),
            gameOver
        );
    }

    /**
     * Populates the level geometry, coins, and hazards.
     */
    private void initialiseLevel() {
        final double floorY = WORLD_HEIGHT - FLOOR_HEIGHT;
        platforms.add(new Platform(LEVEL_ORIGIN_X, floorY, WORLD_WIDTH, FLOOR_HEIGHT));
        platforms.add(new Platform(
            PLATFORM_TWO_X,
            WORLD_HEIGHT - PLATFORM_TWO_Y_OFFSET_FROM_BOTTOM,
            PLATFORM_TWO_WIDTH,
            PLATFORM_HEIGHT
        ));
        platforms.add(new Platform(
            PLATFORM_THREE_X,
            WORLD_HEIGHT - PLATFORM_THREE_Y_OFFSET_FROM_BOTTOM,
            PLATFORM_THREE_WIDTH,
            PLATFORM_HEIGHT
        ));
        platforms.add(new Platform(
            PLATFORM_FOUR_X,
            WORLD_HEIGHT - PLATFORM_FOUR_Y_OFFSET_FROM_BOTTOM,
            PLATFORM_FOUR_WIDTH,
            PLATFORM_HEIGHT
        ));
        platforms.add(new Platform(
            PLATFORM_FIVE_X,
            WORLD_HEIGHT - PLATFORM_FIVE_Y_OFFSET_FROM_BOTTOM,
            PLATFORM_FIVE_WIDTH,
            PLATFORM_HEIGHT
        ));
        platforms.add(new Platform(
            PLATFORM_SIX_X,
            WORLD_HEIGHT - PLATFORM_SIX_Y_OFFSET_FROM_BOTTOM,
            PLATFORM_SIX_WIDTH,
            PLATFORM_HEIGHT
        ));

        coins.add(new Coin(COIN_ONE_X, WORLD_HEIGHT - COIN_ONE_Y_OFFSET_FROM_BOTTOM, COIN_RADIUS, COIN_VALUE));
        coins.add(new Coin(COIN_TWO_X, WORLD_HEIGHT - COIN_TWO_Y_OFFSET_FROM_BOTTOM, COIN_RADIUS, COIN_VALUE));
        coins.add(new Coin(COIN_THREE_X, WORLD_HEIGHT - COIN_THREE_Y_OFFSET_FROM_BOTTOM, COIN_RADIUS, COIN_VALUE));
        coins.add(new Coin(COIN_FOUR_X, WORLD_HEIGHT - COIN_FOUR_Y_OFFSET_FROM_BOTTOM, COIN_RADIUS, COIN_VALUE));
        coins.add(new Coin(COIN_FIVE_X, WORLD_HEIGHT - COIN_FIVE_Y_OFFSET_FROM_BOTTOM, COIN_RADIUS, COIN_VALUE));

        hazards.add(new HazardZone(
            HAZARD_ONE_X,
            WORLD_HEIGHT - FLOOR_HEIGHT,
            HAZARD_WIDTH,
            FLOOR_HEIGHT,
            HAZARD_DAMAGE_PER_SECOND
        ));
        hazards.add(new HazardZone(
            HAZARD_TWO_X,
            WORLD_HEIGHT - FLOOR_HEIGHT,
            HAZARD_WIDTH,
            FLOOR_HEIGHT,
            HAZARD_DAMAGE_PER_SECOND
        ));
    }

    /**
     * Marks any intersecting coins as collected.
     *
     * @param snapshot player snapshot used for collision checks
     */
    private void collectCoins(final PlayerSnapshot snapshot) {
        Objects.requireNonNull(snapshot);
        for (final Coin coin : coins) {
            if (coin.collected) {
                continue;
            }
            if (intersectsCoin(snapshot, coin)) {
                coin.collected = true;
                coinsCollected += coin.value;
            }
        }
    }

    /**
     * Applies damage for hazards intersecting the player.
     *
     * @param snapshot player snapshot used for collision checks
     * @param deltaSeconds elapsed time in seconds since the last update
     */
    private void applyHazards(final PlayerSnapshot snapshot, final double deltaSeconds) {
        double damage = NO_DAMAGE;
        for (final HazardZone hazard : hazards) {
            if (intersectsHazard(snapshot, hazard)) {
                damage += hazard.damagePerSecond * deltaSeconds;
            }
        }
        if (damage > NO_DAMAGE) {
            health = Math.max(MIN_HEALTH, health - damage);
            if (health == MIN_HEALTH) {
                gameOver = true;
            }
        }
    }

    /**
     * Checks whether the player intersects a coin using circle-rectangle overlap.
     *
     * @param snapshot player snapshot
     * @param coin coin to test
     * @return {@code true} when the coin overlaps the player
     */
    private boolean intersectsCoin(final PlayerSnapshot snapshot, final Coin coin) {
        final double closestX = clamp(coin.x, snapshot.getX(), snapshot.getX() + snapshot.getWidth());
        final double closestY = clamp(coin.y, snapshot.getY(), snapshot.getY() + snapshot.getHeight());
        final double dx = coin.x - closestX;
        final double dy = coin.y - closestY;
        return dx * dx + dy * dy <= coin.radius * coin.radius;
    }

    /**
     * Checks whether the player intersects a hazard zone using rectangle overlap.
     *
     * @param snapshot player snapshot
     * @param hazard hazard zone to test
     * @return {@code true} when the hazard overlaps the player
     */
    private boolean intersectsHazard(final PlayerSnapshot snapshot, final HazardZone hazard) {
        final double playerRight = snapshot.getX() + snapshot.getWidth();
        final double playerBottom = snapshot.getY() + snapshot.getHeight();
        final double hazardRight = hazard.x + hazard.width;
        final double hazardBottom = hazard.y + hazard.height;
        return snapshot.getX() < hazardRight
            && playerRight > hazard.x
            && snapshot.getY() < hazardBottom
            && playerBottom > hazard.y;
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

    /**
     * Immutable platform surface used for collision detection.
     */
    public static final class Platform {
        private final double x;
        private final double y;
        private final double width;
        private final double height;

        /**
         * Creates an immutable platform surface.
         *
         * @param x left coordinate
         * @param y top coordinate
         * @param width platform width
         * @param height platform height
         */
        public Platform(final double x, final double y, final double width, final double height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        /**
         * @return the platform's left coordinate.
         */
        public double getX() {
            return x;
        }

        /**
         * @return the platform's top coordinate.
         */
        public double getY() {
            return y;
        }

        /**
         * @return the platform width.
         */
        public double getWidth() {
            return width;
        }

        /**
         * @return the platform height.
         */
        public double getHeight() {
            return height;
        }
    }

    private static final class Coin {
        private final double x;
        private final double y;
        private final double radius;
        private final int value;
        private boolean collected;

        /**
         * Creates a coin with the provided position and value.
         *
         * @param x coin center x coordinate
         * @param y coin center y coordinate
         * @param radius coin radius
         * @param value coin value
         */
        private Coin(final double x, final double y, final double radius, final int value) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.value = value;
        }

        /**
         * Builds a snapshot of this coin.
         *
         * @return immutable coin snapshot
         */
        private CoinSnapshot snapshot() {
            return new CoinSnapshot(x, y, radius, value, collected);
        }
    }

    private static final class HazardZone {
        private final double x;
        private final double y;
        private final double width;
        private final double height;
        private final double damagePerSecond;

        /**
         * Creates a hazard zone that damages the player.
         *
         * @param x left coordinate
         * @param y top coordinate
         * @param width hazard width
         * @param height hazard height
         * @param damagePerSecond damage per second while overlapping
         */
        private HazardZone(
            final double x,
            final double y,
            final double width,
            final double height,
            final double damagePerSecond
        ) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.damagePerSecond = damagePerSecond;
        }

        /**
         * Builds a snapshot of this hazard.
         *
         * @return immutable hazard snapshot
         */
        private HazardSnapshot snapshot() {
            return new HazardSnapshot(x, y, width, height);
        }
    }

    /**
     * Data passed to the view for each coin.
     */
    public static final class CoinSnapshot {
        private final double x;
        private final double y;
        private final double radius;
        private final int value;
        private final boolean collected;

        /**
         * Creates a coin snapshot for the view layer.
         *
         * @param x coin center x coordinate
         * @param y coin center y coordinate
         * @param radius coin radius
         * @param value coin value
         * @param collected {@code true} if the coin is collected
         */
        private CoinSnapshot(
            final double x,
            final double y,
            final double radius,
            final int value,
            final boolean collected
        ) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.value = value;
            this.collected = collected;
        }

        /**
         * @return the coin center x coordinate.
         */
        public double getCenterX() {
            return x;
        }

        /**
         * @return the coin center y coordinate.
         */
        public double getCenterY() {
            return y;
        }

        /**
         * @return the coin radius.
         */
        public double getRadius() {
            return radius;
        }

        /**
         * @return {@code true} when the coin is collected.
         */
        public boolean isCollected() {
            return collected;
        }

        /**
         * @return the coin value.
         */
        public int getValue() {
            return value;
        }
    }

    /**
     * Data passed to the view for each hazard.
     */
    public static final class HazardSnapshot {
        private final double x;
        private final double y;
        private final double width;
        private final double height;

        /**
         * Creates a hazard snapshot for the view layer.
         *
         * @param x left coordinate
         * @param y top coordinate
         * @param width hazard width
         * @param height hazard height
         */
        private HazardSnapshot(
            final double x,
            final double y,
            final double width,
            final double height
        ) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        /**
         * @return the hazard left coordinate.
         */
        public double getX() {
            return x;
        }

        /**
         * @return the hazard top coordinate.
         */
        public double getY() {
            return y;
        }

        /**
         * @return the hazard width.
         */
        public double getWidth() {
            return width;
        }

        /**
         * @return the hazard height.
         */
        public double getHeight() {
            return height;
        }
    }

    /**
     * Transport object containing all data the view needs to render a frame.
     */
    public static final class GameState {
        private final PlayerSnapshot player;
        private final List<Platform> platforms;
        private final List<CoinSnapshot> coins;
        private final List<HazardSnapshot> hazards;
        private final double worldWidth;
        private final double worldHeight;
        private final double health;
        private final double maxHealth;
        private final int coinsCollected;
        private final int totalCoins;
        private final boolean gameOver;

        /**
         * Creates a snapshot of the world for rendering.
         *
         * @param player player snapshot
         * @param platforms platform geometry
         * @param coins coin snapshots
         * @param hazards hazard snapshots
         * @param worldWidth world width
         * @param worldHeight world height
         * @param health current health value
         * @param maxHealth maximum health value
         * @param coinsCollected collected coin count
         * @param totalCoins total coin count
         * @param gameOver {@code true} if the game is over
         */
        private GameState(
            final PlayerSnapshot player,
            final List<Platform> platforms,
            final List<CoinSnapshot> coins,
            final List<HazardSnapshot> hazards,
            final double worldWidth,
            final double worldHeight,
            final double health,
            final double maxHealth,
            final int coinsCollected,
            final int totalCoins,
            final boolean gameOver
        ) {
            this.player = player;
            this.platforms = List.copyOf(platforms);
            this.coins = List.copyOf(coins);
            this.hazards = List.copyOf(hazards);
            this.worldWidth = worldWidth;
            this.worldHeight = worldHeight;
            this.health = health;
            this.maxHealth = maxHealth;
            this.coinsCollected = coinsCollected;
            this.totalCoins = totalCoins;
            this.gameOver = gameOver;
        }

        /**
         * @return the player snapshot.
         */
        public PlayerSnapshot getPlayer() {
            return player;
        }

        /**
         * @return the level platforms.
         */
        public List<Platform> getPlatforms() {
            return platforms;
        }

        /**
         * @return the coin snapshots.
         */
        public List<CoinSnapshot> getCoins() {
            return coins;
        }

        /**
         * @return the hazard snapshots.
         */
        public List<HazardSnapshot> getHazards() {
            return hazards;
        }

        /**
         * @return the world width.
         */
        public double getWorldWidth() {
            return worldWidth;
        }

        /**
         * @return the world height.
         */
        public double getWorldHeight() {
            return worldHeight;
        }

        /**
         * @return the current health.
         */
        public double getHealth() {
            return health;
        }

        /**
         * @return the maximum health.
         */
        public double getMaxHealth() {
            return maxHealth;
        }

        /**
         * @return the number of coins collected.
         */
        public int getCoinsCollected() {
            return coinsCollected;
        }

        /**
         * @return the total number of coins in the level.
         */
        public int getTotalCoins() {
            return totalCoins;
        }

        /**
         * @return {@code true} when the game is over.
         */
        public boolean isGameOver() {
            return gameOver;
        }
    }
}
