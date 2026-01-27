package view.utils;

import java.awt.BorderLayout;
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
    private static final float BUTTON_SIZE = 50f;
    
    /**
     * implements the top bar. 
     *
     * @param back button back
     * @return the pannel where put the button
     */
    public ShopButton() {
        this.background = CreateBackground.create("/img/Shopback.png");

        setLayout(new java.awt.GridLayout(2, 2, 200, 200)); 
        setBorder(javax.swing.BorderFactory.createEmptyBorder(50, 300, 50, 300));

        JButton skin1 = new JButton("UP-LEFT");
        JButton skin2 = new JButton("UP-RIGHT");
        JButton skin3 = new JButton("DOWN-LEFT");
        JButton skin4 = new JButton("DOWN-RIGHT");

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
