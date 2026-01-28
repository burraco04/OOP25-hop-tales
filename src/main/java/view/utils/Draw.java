package view.utils;

import java.awt.Image;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import model.GameConstants;
import model.objects.impl.brick.PowerupBlock;
import model.objects.impl.collectable.Powerup;

/**
 * calss draw.
 */
public final class Draw {
    private static final Map<String, Image> CACHE = new HashMap<>();
    
    private Draw() { }

    public static Image get(final String type) {
        if(CACHE.containsKey(type)){
            return CACHE.get(type);
        } else {
        return CACHE.computeIfAbsent(type, Draw::load);
        }
    }

    private static Image load(final String type) {
        final String path = switch (type) {
            case "lava"      -> "img/lava.png";
            case "top_lava"      -> "img/lava_top.png";
            case "player"      -> "img/bozza_player_1_vers_3.png";
            case "grass"       -> "img/grass.png";
            case "floating_grass" -> "img/floating_grass.png";
            case "floating_grass_left" -> "img/floating_grass_left.png";
            case "floating_grass_right" -> "img/floating_grass_right.png";
            case "powerup_block" -> "img/powerup_block.png";
            case "powerup"     -> "img/powerup.png";
            case "green_grass" -> "img/green_grass.png";
            case "brick"       -> "img/brick.png";
            case "coin"        -> "img/coin_gold.png";
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
}
