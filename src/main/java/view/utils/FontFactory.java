package view.utils;

import java.awt.Component;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 * Font factory.
 */
public final class FontFactory {
    static final Long serialVersionUID = 1L;
    private static final String BASE_FONT = "Arial";

    /**
     * Loads and returns a font.
     *
     * @param nameFont the name of the font
     * @param fontSize  the font size
     * @param component the component used as parent for error dialogs
     * @return return the loaded font 
     */

    public Font getFont(final String nameFont, final float fontSize, final Component component) {
        Font font = new Font(BASE_FONT, Font.PLAIN, (int) fontSize);
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/Font/" + nameFont + ".ttf"))
            .deriveFont(fontSize);
        } catch (FontFormatException | IOException e) {
            JOptionPane.showMessageDialog(component, "non si carica il font.");
        } 
        return font;

    }
}
