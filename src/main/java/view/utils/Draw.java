package view.utils;

import java.awt.Image;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;
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
     * select if the entity is animated or static 
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
    return "coin".equals(type) || "player".equals(type);   // aggiungi altri type animati qui
    }

    /**
     * load the static image.
     *
     * @param type of the object
     * @return the correct image
     */
    private static Image loadImage(final String type) {
        final String path = switch (type) {
            case "lava"      -> "img/lava.png";
            case "top_lava"      -> "img/lava_top.png";
            case "player"      -> "img/Player_1_frame_1.png";
            case "grass"       -> "img/grass.png";
            case "floating_grass" -> "img/floating_grass.png";
            case "floating_grass_left" -> "img/floating_grass_left.png";
            case "floating_grass_right" -> "img/floating_grass_right.png";
            case "powerup_block" -> "img/powerup_block.png";
            case "powerup"     -> "img/powerup.png";
            case "green_grass" -> "img/green_grass.png";
            case "brick"       -> "img/brick.png";
            case "fungo"       -> "img/slime_spike_rest.png";
            case "full_heart"  -> "img/full_heart.png";
            case "empty_heart"  -> "img/empty_heart.png";
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
            case "player" -> new Animation(new Image[] {loadFromResources("img/Player_1_frame_1.png"),
            loadFromResources("img/Player_1_frame_2.png")}, FRAME_DURATION_PLAYER);
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

}
