package view.utils;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import model.GameConstants;

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
            case "player"      -> "img/bozza_player_1_vers_3.png";
            case "grass"       -> "img/grass.png"; 
            case "green_grass" -> "img/green_grass.png";
            case "brick"       -> "img/brick.png";
            case "coin"        -> "img/coin_gold.png";
            case "fungo"       -> "img/slime_spike_rest.png";
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
