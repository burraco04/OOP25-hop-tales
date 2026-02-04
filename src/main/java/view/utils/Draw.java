package view.utils;

import java.awt.Image;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

/**
 * Draw utility class.
 */
public final class Draw {
    private static final Map<String, Image> CACHE = new HashMap<>();
    private static final Map<String, Animation> ANIMATIONS = new ConcurrentHashMap<>();
    private static final int FRAME_DURATION_COIN = 300;
    private static final int FRAME_DURATION_PLAYER = 200;
    private static final int FRAME_DURATION_LAVA = 500;
    private static final String PLAYER = "player";
    private static final Set<String> ANIMATED_TYPES = Set.of("coin", PLAYER, "top_lava", "player_hurt", "top_water");
    private static String playerFrame1 = "img/Player_1_frame_1.png";
    private static String playerFrame2 = "img/Player_1_frame_2.png";

    private Draw() { }

    /**
     * Searches the cache for a static image.
     *
     * @param type the entity type
     * @return the corresponding static image
     */
    public static Image getStatic(final String type) {
        if (CACHE.containsKey(type)) {
            return CACHE.get(type);
        } else {
        return CACHE.computeIfAbsent(type, Draw::loadImage);
        }
    }

    /**
     * Searches the animation cache.
     *
     * @param type the entity type
     * @return the corresponding animation
     */
    public static Animation getAnim(final String type) {
        if (ANIMATIONS.containsKey(type)) {
            return ANIMATIONS.get(type);
        } else {
        return ANIMATIONS.computeIfAbsent(type, Draw::loadAnim);
        }
    }

    /**
     * selects whether the entity is animated or static.
     *
     * @param type the entity type
     * @param t time that had passed since the start of the game
     * @return the correct image
     */
    public static Image get(final String type, final long t) {
        // qui decidi quali type sono animati
        if (isAnimated(type)) {
            return getAnim(type).getFrame(t);
        }
        return getStatic(type);
    }

    /**
     * checks whether the entity is animated.
     *
     * @param type the entity type
     * @return {@code true} if the entity is animated, {@code false} otherwise
     */
    private static boolean isAnimated(final String type) {
    return ANIMATED_TYPES.contains(type);
    }

    /**
     * loads the static image.
     *
     * @param type the entity type
     * @return the corresponding image
     */
    private static Image loadImage(final String type) {
        final String path = switch (type) {
            case "door_top" -> "img/door_open_top.png";
            case "door" -> "img/door_open.png";
            case "brick_castle" -> "img/bricks_castle.png";
            case "lava" -> "img/lava.png";
            case "grass" -> "img/grass.png";
            case "floating_grass" -> "img/floating_grass.png";
            case "floating_grass_left" -> "img/floating_grass_left.png";
            case "floating_grass_right" -> "img/floating_grass_right.png";
            case "powerup_block" -> "img/powerup_block.png";
            case "powerup" -> "img/powerup.png";
            case "green_grass" -> "img/green_grass.png";
            case "brick" -> "img/brick.png";
            case "jumper" -> "img/frog.png";
            case "walker" -> "img/snail.png";
            case "full_heart" -> "img/full_heart.png";
            case "empty_heart" -> "img/empty_heart.png";
            case "block_planks" -> "img/block_planks.png";
            case "dirt_block" -> "img/dirt_block.png";
            case "top_dirt_block" -> "img/top_dirt_block.png";
            case "water" -> "img/water.png";
            case "floating_dirt_middle" -> "img/floating_dirt_middle.png";
            case "floating_dirt_left" -> "img/floating_dirt_left.png";
            case "floating_dirt_right" -> "img/floating_dirt_right.png";
            default -> throw new IllegalArgumentException("Tipo sprite sconosciuto: " + type);
        };

        return loadFromResources(path);
    }

    /**
     * returns the correct animation.
     *
     * @param type the entity type
     * @return corresponding animation
     */
    private static Animation loadAnim(final String type) {
        return switch (type) {
            case "coin" -> new Animation(new Image[] {loadFromResources("img/coin_gold.png"),
            loadFromResources("img/coin_gold_side.png")}, FRAME_DURATION_COIN);
            case PLAYER -> new Animation(new Image[] {loadFromResources(playerFrame1),
            loadFromResources(playerFrame2)}, FRAME_DURATION_PLAYER);
            case "player_hurt" -> new Animation(new Image[] {loadFromResources("img/Player_1_damaged_frame_1.png"),
            loadFromResources("img/Player_1_damaged_frame_2.png")}, FRAME_DURATION_PLAYER);
            case "top_lava" -> new Animation(new Image[] {loadFromResources("img/lava_top.png"),
            loadFromResources("img/lava_top_low.png")}, FRAME_DURATION_LAVA);
            case "top_water" -> new Animation(new Image[] {loadFromResources("img/water_top.png"),
            loadFromResources("img/water_top_low.png")}, FRAME_DURATION_LAVA);
            default -> throw new IllegalArgumentException("Sprite type unknown : " + type);
        };
    }

    /**
     * Loads an image from the resources.
     *
     * @param path the resuorces path
     * @return the loaded image
     */
    private static Image loadFromResources(final String path) {
        try (var in = Draw.class.getClassLoader().getResourceAsStream(path)) {
            if (in == null) {
                throw new IllegalArgumentException("Sprite non trovato in resources: " + path);
            }
            return ImageIO.read(in);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Sets the player skin and invalidates the related caches.
     *
     * @param frame1 the path of the first frame
     * @param frame2 the path of the second frame
     */
    public static void setPlayerSkin(final String frame1, final String frame2) {
        playerFrame1 = frame1;
        playerFrame2 = frame2;
        ANIMATIONS.remove(PLAYER);
    }

}
