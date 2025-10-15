package model;

import model.entities.player.api.Player;
import model.entities.player.api.PlayerSnapshot;
import model.entities.player.impl.PlayerImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Maintains the platformer world state and resolves physics interactions.
 */
public class Model {
    public static final int WORLD_WIDTH = 960;
    public static final int WORLD_HEIGHT = 540;
    private final Player player;
    private final List<Platform> platforms;

    /**
     * Builds a game model with a default level layout.
     */
    public Model() {
        this.platforms = new ArrayList<>();
        this.createDefaultLevel();
        this.player = new PlayerImpl(
            WORLD_WIDTH / 4.0,
            WORLD_HEIGHT - 120.0,
            48.0,
            64.0,
            WORLD_WIDTH,
            WORLD_HEIGHT,
            this.platforms
        );
    }

    private void createDefaultLevel() {
        // Floor
        platforms.add(new Platform(0.0, WORLD_HEIGHT - 40.0, WORLD_WIDTH, 40.0));
        // Floating platforms
        platforms.add(new Platform(160.0, WORLD_HEIGHT - 150.0, 160.0, 24.0));
        platforms.add(new Platform(420.0, WORLD_HEIGHT - 240.0, 200.0, 24.0));
        platforms.add(new Platform(740.0, WORLD_HEIGHT - 320.0, 180.0, 24.0));
        platforms.add(new Platform(60.0, WORLD_HEIGHT - 280.0, 140.0, 24.0));
        platforms.add(new Platform(540.0, WORLD_HEIGHT - 110.0, 140.0, 24.0));
    }

    /**
     * Updates the state of the left movement input.
     *
     * @param pressed true when the key is held down
     */
    public synchronized void setLeftPressed(final boolean pressed) {
        player.setLeftPressed(pressed);
    }

    /**
     * Updates the state of the right movement input.
     *
     * @param pressed true when the key is held down
     */
    public synchronized void setRightPressed(final boolean pressed) {
        player.setRightPressed(pressed);
    }

    /**
     * Requests the next available jump for the player.
     */
    public synchronized void queueJump() {
        player.queueJump();
    }

    /**
     * Advances the simulation by the given delta.
     *
     * @param deltaSeconds elapsed time in seconds
     */
    public synchronized void update(final double deltaSeconds) {
        if (deltaSeconds <= 0) {
            return;
        }

        player.update(deltaSeconds);
    }

    /**
     * Provides a read-only snapshot of the current game state.
     *
     * @return immutable view of the world entities
     */
    public synchronized GameState snapshot() {
        final PlayerSnapshot playerSnapshot = player.snapshot();
        return new GameState(
            playerSnapshot,
            Collections.unmodifiableList(new ArrayList<>(platforms)),
            WORLD_WIDTH,
            WORLD_HEIGHT
        );
    }

    /**
     * Immutable platform surface description.
     */
    public static final class Platform {
        public final double x;
        public final double y;
        public final double width;
        public final double height;

        public Platform(final double x, final double y, final double width, final double height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    /**
     * Aggregated world information for the view.
     */
    public static final class GameState {
        public final PlayerSnapshot player;
        public final List<Platform> platforms;
        public final double worldWidth;
        public final double worldHeight;

        public GameState(
            final PlayerSnapshot player,
            final List<Platform> platforms,
            final double worldWidth,
            final double worldHeight
        ) {
            this.player = player;
            this.platforms = platforms;
            this.worldWidth = worldWidth;
            this.worldHeight = worldHeight;
        }
    }
}
