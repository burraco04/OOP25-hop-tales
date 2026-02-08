package view.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

/**
 * Utility class for loading images from resources.
 */
public final class Assets {

    private Assets() {
    }

    /**
     * Loads an image from the classpath.
     *
     * @param classpathPath resource path
     * @return the loaded image
     */
    public static BufferedImage load(final String classpathPath) {
        try (InputStream input = Assets.class.getResourceAsStream(classpathPath)) {
            if (input == null) {
                throw new IllegalArgumentException("Resource non trovata: " + classpathPath);
            }
            return ImageIO.read(input);
        } catch (final IOException e) {
            throw new IllegalStateException("Impossibile caricare immagine: " + classpathPath, e);
        }
    }
}
