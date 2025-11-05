package view.impl;

import java.awt.Component;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;

import javax.swing.JOptionPane;

public class FontFactory {
    static final long serialVersionUID = 1l;
    private static final String BASE_FONT = "Arial";

    Font getFont(final String nameFont, final float fontSize, final Component component){
        Font font = new Font(BASE_FONT, Font.PLAIN, (int) fontSize);

        try{
            font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/Font/" + nameFont + ".ttf"))
                    .deriveFont(fontSize);
        } catch(FontFormatException | IOException e){
            JOptionPane.showMessageDialog(component, "non si carica");
        }

        return font;

          



    }
}
