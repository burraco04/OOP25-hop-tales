package view.utils;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 * creates the background for pannel.
 */
public final class CreateBackground {

    private static final Logger LOGGER = Logger.getLogger(CreateBackground.class.getName());

    /**
     * Class constructor.
     */
    private CreateBackground() {
    //create background
    }

    /**
     * Creates a background image.
     *
     * @param path the path of the image
     * @return the loaded image
     */
    public static Image create(final String path) {
         try (InputStream is = CreateBackground.class.getResourceAsStream(path)) {
            if (is != null) {
                return ImageIO.read(is);
            }
        } catch (final IOException e) {
            LOGGER.log(Level.WARNING, "Failed to load background: " + path, e);
        }
        return null;
    }
}
