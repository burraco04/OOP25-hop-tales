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
 * calss draw.
 */
public final class Draw {
    private static final Map<String, Image> CACHE = new HashMap<>();
    private static final Map<String, Animation> ANIMATIONS = new ConcurrentHashMap<>();
    private static final int FRAME_DURATION_COIN = 300;
    private static final int FRAME_DURATION_PLAYER = 200;
    private static final int FRAME_DURATION_LAVA = 500;
    private static final Set<String> ANIMATED_TYPES = Set.of("coin", "player", "top_lava", "player_hurt");
    private static String playerFrame1 = "img/Player_1_frame_1.png";
    private static String playerFrame2 = "img/Player_1_frame_2.png";

    private Draw() { }

    /**
     * search in the cache map if type is already present.
     *
     * @param type type of entities
     * @return the correct frame
     */
    public static Image getStatic(final String type) {
        if (CACHE.containsKey(type)) {
            return CACHE.get(type);
        } else {
        return CACHE.computeIfAbsent(type, Draw::loadImage);
        }
    }

    /**
     * search in the animations map if type is already present.
     *
     * @param type type of entities
     * @return the correct frame
     */
    public static Animation getAnim(final String type) {
        if (ANIMATIONS.containsKey(type)) {
            return ANIMATIONS.get(type);
        } else {
        return ANIMATIONS.computeIfAbsent(type, Draw::loadAnim);
        }
    }

    /**
     * select if the entity is animated or static.
     * 
     * @param type of entity
     * @param t time that had passed 
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
     * check if the entity is animated.
     *
     * @param type of entity
     * @return true or false
     */
    private static boolean isAnimated(final String type) {
    return ANIMATED_TYPES.contains(type);   // aggiungi altri type animati qui
    }

    /**
     * load the static image.
     *
     * @param type of the object
     * @return the correct image
     */
    private static Image loadImage(final String type) {
        final String path = switch (type) {
            case "door_top" -> "img/door_open_top.png";
            case "door" -> "img/door_open.png";
            case "brick_castle" -> "img/bricks_castle.png";
            case "lava" -> "img/lava.png";
            case "player" -> playerFrame1;
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
            case "water_top" -> "img/water_top.png";
            case "floating_dirt_middle" -> "img/floating_dirt_middle.png";
            case "floating_dirt_left" -> "img/floating_dirt_left.png";
            case "floating_dirt_right" -> "img/floating_dirt_right.png";
            default -> throw new IllegalArgumentException("Tipo sprite sconosciuto: " + type);
        };

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
     * return the correct animation.
     *
     * @param type of entity
     * @return correct animation
     */
    private static Animation loadAnim(final String type) {
        return switch (type) {
            case "coin" -> new Animation(new Image[] {loadFromResources("img/coin_gold.png"),
            loadFromResources("img/coin_gold_side.png")}, FRAME_DURATION_COIN);
            case "player" -> new Animation(new Image[] {loadFromResources(playerFrame1),
            loadFromResources(playerFrame2)}, FRAME_DURATION_PLAYER);
            case "player_hurt" -> new Animation(new Image[] {loadFromResources("img/Player_1_damaged_frame_1-1.png"),
            loadFromResources("img/Player_1_damaged_frame_1-2.png")}, FRAME_DURATION_PLAYER);
            case "top_lava" -> new Animation(new Image[] {loadFromResources("img/lava_top.png"),
            loadFromResources("img/lava_top_low.png")}, FRAME_DURATION_LAVA);
            default -> throw new IllegalArgumentException("Tipo sprite sconosciuto: " + type);
        };
    }

    /**
     * check if the path is corrected.
     *
     * @param path of the entity
     * @return the correct image
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
     * Imposta la skin del player e invalida le cache.
     *
     * @param frame1 path al primo frame
     * @param frame2 path al secondo frame (se null usa frame1)
     */
    public static void setPlayerSkin(final String frame1, final String frame2) {
        playerFrame1 = frame1;
        playerFrame2 = frame2 == null ? frame1 : frame2;
        CACHE.remove("player");
        ANIMATIONS.remove("player");
    }

}
