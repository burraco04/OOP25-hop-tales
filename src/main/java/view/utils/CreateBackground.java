package view.utils;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public final class CreateBackground {

    /**
     * create the background of the panell.
     *
     * @param path path of the image
     * @return return the image
     */
    public static Image create(final String path ) {
         try (InputStream is = CreateBackground.class.getResourceAsStream(path)) {
            if (is != null) {
                return ImageIO.read(is);
            }
        } catch (final IOException ignored) {
    
        }
        return null;
    }
}
