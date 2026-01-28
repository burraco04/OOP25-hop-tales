package view.utils;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * used for buy the skin.
 */
public class ShopButton extends JPanel{

    private static final long serialVersionUID = 1L;
    private final Image background;
    private final int ROWS = 2;
    private final int COLS = 2;
    private final int HGAP = 200;
    private final int VGAP = 200;
    private final int TOP = 50;
    private final int LEFT = 300;
    private final int BOTTOM = 50;
    private final int RIGHT = 300;
    
    /**
     * implements the top bar. 
     *
     * @param back button back
     * @return the pannel where put the button
     */
    public ShopButton() {

        this.background = CreateBackground.create("/img/Shopback.png");

        setLayout(new java.awt.GridLayout(ROWS, COLS, HGAP, VGAP)); 
        setBorder(javax.swing.BorderFactory.createEmptyBorder(TOP, LEFT, BOTTOM, RIGHT));

        final JButton skin1 = ShopButtonFactory.build("/img/bozza_player_1_vers_3.png");
        final JButton skin2 = ShopButtonFactory.build("/img/skinsqualo.png");
        final JButton skin3 = ShopButtonFactory.build("/img/skinsqualo.png");
        final JButton skin4 = ShopButtonFactory.build("/img/skinsqualo.png");

        skin1.setText("DEFAULT");
        skin2.setText("10$");
        skin3.setText("5$");
        skin4.setText("15$");

        add(skin1);
        add(skin2);
        add(skin3);
        add(skin4);
    }

    /**
     * cancella e ridipinge il pannello.
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
         g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }
}
