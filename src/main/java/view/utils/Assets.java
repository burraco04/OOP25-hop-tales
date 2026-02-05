package view.utils;

import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;

//utility per caricare immagini dalle risorse
public final class Assets {
    private Assets() {}

    public static BufferedImage load(String classpathPath) {
        try (InputStream is = Assets.class.getResourceAsStream(classpathPath)) {
            if (is == null) {
                throw new RuntimeException("Resource non trovata: " + classpathPath);
            }
            return ImageIO.read(is);
        } catch (Exception e) {
            throw new RuntimeException("Impossibile caricare immagine: " + classpathPath, e);
        }
    }
}
