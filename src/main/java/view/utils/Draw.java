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

    private Draw() { }

    public static Image getStatic(final String type) {
        if(CACHE.containsKey(type)){
            return CACHE.get(type);
        } else {
        return CACHE.computeIfAbsent(type, Draw::loadImage);
        }
    }

    public static Animation getAnim(final String type) {
        if(ANIMATIONS.containsKey(type)){
            return ANIMATIONS.get(type);
        } else {
        return ANIMATIONS.computeIfAbsent(type, Draw::loadAnim);
        }
    }

    public static Image get(final String type, final long t) {
        // qui decidi quali type sono animati
        if (isAnimated(type)) {
            return getAnim(type).getFrame(t);
        }
        return getStatic(type);
    }

    private static boolean isAnimated(final String type) {
    return "coin".equals(type) || "player".equals(type);   // aggiungi altri type animati qui
    }

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
        } catch (IOException e) {
           throw new UncheckedIOException(e);
        }
    }

    private static Animation loadAnim(final String type) {
        return switch (type) {
            case "coin" -> new Animation(new Image[] {loadFromResources("img/coin_gold.png"), 
            loadFromResources("img/coin_gold_side.png")}, 300);
            case "player" -> new Animation(new Image[] {loadFromResources("img/Player_1_frame_1.png"),
            loadFromResources("img/Player_1_frame_2.png")}, 200);
            default -> throw new IllegalArgumentException("Tipo sprite sconosciuto: " + type);
        };
    }

     private static Image loadFromResources(final String path) {
        try (var in = Draw.class.getClassLoader().getResourceAsStream(path)) {
            if (in == null) {
                throw new IllegalArgumentException("Sprite non trovato in resources: " + path);
            }
            return ImageIO.read(in);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
