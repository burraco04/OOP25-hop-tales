package view.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.api.Controllermain;

public class Menu extends JPanel {

    private static final String TITLE_FONT = "SuperShiny";
    private static final float TITLE_SIZE = 100f;
    private static final float BUTTON_SIZE = 65f;

    private final FontFactory fontFactory = new FontFactory();
    private BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

    public Menu(final Controllermain controller, final String text) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        final JLabel title = new JLabel("HOP TALES");
        title.setFont(this.fontFactory.getFont(TITLE_FONT, TITLE_SIZE, this));
        title.setAlignmentY(CENTER_ALIGNMENT);

        final JButton start = new JButton(text);
        start.setFont(this.fontFactory.getFont(TITLE_FONT, BUTTON_SIZE, this));
        start.setForeground(Color.GREEN);
        start.setBackground(Color.GRAY);
        start.setAlignmentX(CENTER_ALIGNMENT);

        this.add(title);
        this.add(start);

        
        this.image = tryLoad("/img/Piattaforma_retro_con_personaggio_pixelato.png");
       
        if (this.image == null) {
           
            this.image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        }


    }

    // questa funzione cerca cerca e apre l'immagine
    
    private BufferedImage tryLoad(final String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is != null) {
                return ImageIO.read(is);
            }
        } catch (IOException ignored) { }
        return null;
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
         g.drawImage(this.image, 0, 0, getWidth(), getHeight(), this);
    }
}
