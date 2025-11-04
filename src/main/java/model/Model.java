package model;

import model.entities.player.api.Player;
import model.entities.player.api.PlayerSnapshot;
import model.entities.player.impl.PlayerImpl;

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

    private static final double PLAYER_START_X = WORLD_WIDTH / 4.0;
    private static final double PLAYER_START_Y = WORLD_HEIGHT - 120.0;
    private static final double PLAYER_WIDTH = 48.0;
    private static final double PLAYER_HEIGHT = 64.0;

    private static final double FLOOR_HEIGHT = 40.0;
    private static final double PLATFORM_HEIGHT = 24.0;
    private static final double COIN_RADIUS = 12.0;
    private static final double HAZARD_DAMAGE_PER_SECOND = 1.5;
    private static final int COIN_VALUE = 1;
    private static final int MAX_HEALTH = 5;

    private final List<Platform> platforms = new ArrayList<>();
    private final List<Coin> coins = new ArrayList<>();
    private final List<HazardZone> hazards = new ArrayList<>();
    private final Player player;

    private PlayerSnapshot lastPlayerSnapshot;
    private double health = MAX_HEALTH;
    private int coinsCollected;
    private boolean gameOver;

    public Model() {
        initialiseLevel();
        this.player = new PlayerImpl(
            PLAYER_START_X,
            PLAYER_START_Y,
            PLAYER_WIDTH,
            PLAYER_HEIGHT,
            WORLD_WIDTH,
            WORLD_HEIGHT,
            platforms
        );
        this.lastPlayerSnapshot = player.snapshot();
    }

    public synchronized void setLeftPressed(final boolean pressed) {
        player.setLeftPressed(pressed);
    }

    public synchronized void setRightPressed(final boolean pressed) {
        player.setRightPressed(pressed);
    }

    public synchronized void queueJump() {
        player.queueJump();
    }

    public synchronized void update(final double deltaSeconds) {
        if (deltaSeconds <= 0.0 || gameOver) {
            return;
        }
        player.update(deltaSeconds);
        lastPlayerSnapshot = player.snapshot();
        collectCoins(lastPlayerSnapshot);
        applyHazards(lastPlayerSnapshot, deltaSeconds);
    }

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

    private void initialiseLevel() {
        final double floorY = WORLD_HEIGHT - FLOOR_HEIGHT;
        platforms.add(new Platform(0.0, floorY, WORLD_WIDTH, FLOOR_HEIGHT));
        platforms.add(new Platform(160.0, WORLD_HEIGHT - 150.0, 160.0, PLATFORM_HEIGHT));
        platforms.add(new Platform(420.0, WORLD_HEIGHT - 240.0, 200.0, PLATFORM_HEIGHT));
        platforms.add(new Platform(740.0, WORLD_HEIGHT - 320.0, 180.0, PLATFORM_HEIGHT));
        platforms.add(new Platform(60.0, WORLD_HEIGHT - 280.0, 140.0, PLATFORM_HEIGHT));
        platforms.add(new Platform(540.0, WORLD_HEIGHT - 110.0, 140.0, PLATFORM_HEIGHT));

        coins.add(new Coin(210.0, WORLD_HEIGHT - 190.0, COIN_RADIUS, COIN_VALUE));
        coins.add(new Coin(470.0, WORLD_HEIGHT - 270.0, COIN_RADIUS, COIN_VALUE));
        coins.add(new Coin(780.0, WORLD_HEIGHT - 340.0, COIN_RADIUS, COIN_VALUE));
        coins.add(new Coin(120.0, WORLD_HEIGHT - 310.0, COIN_RADIUS, COIN_VALUE));
        coins.add(new Coin(600.0, WORLD_HEIGHT - 140.0, COIN_RADIUS, COIN_VALUE));

        hazards.add(new HazardZone(320.0, WORLD_HEIGHT - FLOOR_HEIGHT, 80.0, FLOOR_HEIGHT, HAZARD_DAMAGE_PER_SECOND));
        hazards.add(new HazardZone(660.0, WORLD_HEIGHT - FLOOR_HEIGHT, 80.0, FLOOR_HEIGHT, HAZARD_DAMAGE_PER_SECOND));
    }

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

    private void applyHazards(final PlayerSnapshot snapshot, final double deltaSeconds) {
        double damage = 0.0;
        for (final HazardZone hazard : hazards) {
            if (intersectsHazard(snapshot, hazard)) {
                damage += hazard.damagePerSecond * deltaSeconds;
            }
        }
        if (damage > 0.0) {
            health = Math.max(0.0, health - damage);
            if (health == 0.0) {
                gameOver = true;
            }
        }
    }

    private boolean intersectsCoin(final PlayerSnapshot snapshot, final Coin coin) {
        final double closestX = clamp(coin.x, snapshot.getX(), snapshot.getX() + snapshot.getWidth());
        final double closestY = clamp(coin.y, snapshot.getY(), snapshot.getY() + snapshot.getHeight());
        final double dx = coin.x - closestX;
        final double dy = coin.y - closestY;
        return dx * dx + dy * dy <= coin.radius * coin.radius;
    }

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

        public Platform(final double x, final double y, final double width, final double height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getWidth() {
            return width;
        }

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

        private Coin(final double x, final double y, final double radius, final int value) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.value = value;
        }

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

        public double getCenterX() {
            return x;
        }

        public double getCenterY() {
            return y;
        }

        public double getRadius() {
            return radius;
        }

        public boolean isCollected() {
            return collected;
        }

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

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getWidth() {
            return width;
        }

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

        public PlayerSnapshot getPlayer() {
            return player;
        }

        public List<Platform> getPlatforms() {
            return platforms;
        }

        public List<CoinSnapshot> getCoins() {
            return coins;
        }

        public List<HazardSnapshot> getHazards() {
            return hazards;
        }

        public double getWorldWidth() {
            return worldWidth;
        }

        public double getWorldHeight() {
            return worldHeight;
        }

        public double getHealth() {
            return health;
        }

        public double getMaxHealth() {
            return maxHealth;
        }

        public int getCoinsCollected() {
            return coinsCollected;
        }

        public int getTotalCoins() {
            return totalCoins;
        }

        public boolean isGameOver() {
            return gameOver;
        }
    }
}
